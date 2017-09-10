/**
 * 
 */
package com.citi.dde.common.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.entity.JobWatcherVO;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.exception.MonitorException;
import com.citi.dde.common.exception.PauseException;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;

/**
 * @author Core Cash Team
 * 
 *         Created at Jul 22, 2016
 * 
 */
@Component("mq-monitor")
public class Monitor implements IMonitorDef  {


	private IMonitorConfig config;
	
	int SYSTEM_EXIT=0;

	private static final Logger logger = LogManager.getLogger(Monitor.class);
	
	@Autowired
	private Environment env;

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private ConcurrentTaskExecutor taskExecutor;
	
	private Collection<Runnable> allTasks = new ArrayList<>();


	public Monitor(IMonitorConfig config) {
		this.config=config;
	}

	public void execute() throws MonitorException {
		boolean flag= true;
		try{
//			int maxPoolSize = ITaskRun.getJobDetailMap().get(DDEConstants.MASTER).getThreadCount();
//			System.out.println("Max Thread Pool :"+maxPoolSize);
			 ExecutorService executor = Executors.newCachedThreadPool();//newFixedThreadPool(maxPoolSize);
			 taskExecutor.setConcurrentExecutor(executor);
		}catch(Exception e){
			flag = false;
		}
		if(flag)
		for(Runnable task : allTasks) {
			taskExecutor.submit(task);
		}
	}

	public void init() throws MonitorException {
		load(this.config);
	}

	public void stop() throws MonitorException {
		logger.info("Stopping service...");
		System.exit(SYSTEM_EXIT);
	}

	public void run() {		
		try {
			init();
			execute();
				try {
					logger.info("pausing for sometime");
					pause();
				} catch (PauseException e) {
					logger.error("Exception Triggered in MQMonitor > run >"+e.getMessage(),e);
					try {
						Thread.sleep(10000);
					} catch (InterruptedException ie) {
						logger.error("Exception Triggered in MQMonitor > run >"+ie.getMessage());
					}
				}
			logger.info("=====================================================================================================================");
		} catch (MonitorException e) {
			logger.error("Exception Triggered in MQMonitor > run >"+e.getMessage(),e);
		} catch (Exception e) {
			logger.error("Exception Triggered in MQMonitor > run >"+e.getMessage());
			try {
				stop();
			} catch (MonitorException e1) {
				logger.error("Exception Triggered in MQMonitor > run >"+e1.getMessage(),e);
			}
		}
	}

	public void pause() throws PauseException {
		try {
			Thread.sleep(Integer.parseInt(env.getProperty(System.getProperty(DDEConstants.APP).toUpperCase()
					+DDEConstants.WAIT_TIME)));
		} catch (InterruptedException | NumberFormatException e) {
			try {
				//It will wait for $default.waittime seconds otherwise 5 minutes
				Thread.sleep(Integer.parseInt(env.getProperty("default.waittime"),300000));
			} catch (NumberFormatException | InterruptedException e1) {
				throw new PauseException(e);
			}
		}
	}

	public String getName() {
		return "monitor";
	}

	public void load(IMonitorConfig config) throws MonitorException {
		Collection<Strategy> apps = config.getStrategyType();
		for(Strategy app : apps) {
			Runnable task = context.getBean(app.name(), Runnable.class);
			allTasks.add(task);
			logger.info("Executing Task ----->>"+app.name().toUpperCase());
		}
		
	}
}
