package com.citi.dde.ach.config;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.citi.dde.ach.routeBuilder.CamelRouteBuilder;
import com.citi.dde.common.monitor.IMonitorConfig;
import com.citi.dde.common.util.DDEConstants;



@Configuration
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@EnableJms
@ComponentScan(basePackages= {"com.citi.dde.ach.*","com.citi.dde.common.*"} )
@PropertySources({@PropertySource("classpath:/MQ.properties"),@PropertySource("classpath:/achApp.properties")})
@ImportResource("classpath:/ach-config.xml")
@EnableTransactionManagement
public class ACHConfig implements ApplicationContextAware{

	@Autowired
	Environment prop;
	
	private ApplicationContext context;
	
	@Bean(name="camelContext")
	public CamelContext  defaultCamelContext( ConnectionFactory activeMQConnectionFactory ) {
		CamelContext cc = new DefaultCamelContext();
		try {
			cc.addRoutes(new CamelRouteBuilder());
			cc.addComponent(DDEConstants.JMS, JmsComponent.jmsComponentAutoAcknowledge(activeMQConnectionFactory));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cc;
	}
	
	@Bean(name="connectionFactory")
	public ConnectionFactory activeMQConnectionFactory(){
		return new ActiveMQConnectionFactory(prop.getProperty(DDEConstants.MQ_BROKER_URL));
	} 
	
	@Bean(name="mq-config")
	public IMonitorConfig monitorConfig() {
		return new MQMonitorConfig();
	}
	
	
	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
		return new HibernateTransactionManager(sessionFactory);
	}
	
	
	@Bean
	@Autowired
//	@Lazy(true)
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setDataSource(dataSource);
		Properties hibernateProps = new Properties();
		hibernateProps.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		hibernateProps.put("hibernate.hbm2ddl.auto", "nothing");
		hibernateProps.put("hibernate.show_sql", false);
		hibernateProps.put("hibernate.format_sql", false);
		hibernateProps.put("hibernate.generate_statistics", false);		
		hibernateProps.put("hibernate.jdbc.batch_size", 100);
		hibernateProps.put("hibernate.use_sql_comments", true);
		hibernateProps.put("hibernate.cache.use_second_level_cache", false);
		hibernateProps.put("hibernate.cache.use_query_cache", false);
		hibernateProps.put("hibernate.connection.release_mode", "after_transaction");
		bean.setHibernateProperties(hibernateProps);
		bean.setPackagesToScan("com.citi.dde.ach.entity");
		return bean;
	}
	
	

	@Bean
	public DataSource dataSource() throws Exception {
		DriverManagerDataSource dmdc = new DriverManagerDataSource();
		dmdc.setDriverClassName("com.mysql.jdbc.Driver");
		dmdc.setUrl("jdbc:mysql://localhost:3306/java_brains");
		dmdc.setUsername("root");
		dmdc.setPassword("sql@123");
		return dmdc;
	}
	
	@Bean
  	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
  		return new PropertySourcesPlaceholderConfigurer();
  	}
	
	@Bean
	public ConcurrentTaskExecutor taskExecutor(){
		 return new ConcurrentTaskExecutor();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.context = ctx;
	}
	
	@Bean 
	public ApplicationContext applicationContext(){
		return this.context;
	}
}
