/**
 * 
 */
package com.citi.dde.common.monitor;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.task.ITaskDef;
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
public class MQMonitor extends AbstractMonitor  {


	private static boolean taskSuccess = false;
	
	private boolean masterTask = false;
	
	int SYSTEM_EXIT=0;

	private static final Logger logger = LogManager.getLogger(MQMonitor.class);
	
	@Autowired
	private Environment env;

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private ConcurrentTaskExecutor taskExecutor;
	
	private Collection<ITaskDef<?>> allTasks = new ArrayList<>();


	public MQMonitor(IMonitorConfig config) {
		super(config);		
	}

	public void execute() throws MonitorException {
		for(ITaskDef<?> task : allTasks) {
			taskExecutor.submit(task);
		}
	}

	public void init() throws MonitorException {
		
	}

	public boolean keepRunning() throws MonitorException {
		/*String path = env.getProperty(DDEConstants.PATH_CTL_FILES);
		
		if(path == null) {
			throw new CriticalException("Environment variable PATH_CTL_FILES not found",null, false, true);
		}
		Path pathToCtl = new File(new File(path), "CS_SH_MQMONITOR_" + System.getProperty("app") + ".CTL").toPath();
		boolean fileExists = Files.exists(pathToCtl, LinkOption.NOFOLLOW_LINKS);
		if(!fileExists) {
			logger.warn("CTL file for the service " + pathToCtl.getFileName() + " does not exist");
		}
		return fileExists;*/
		return this.masterTask;
	}

	public void stop() throws MonitorException {
		logger.info("Stopping service...");
		System.exit(SYSTEM_EXIT);
	}

	public void run() {		
		try {
			
				init();
				load();
				while (keepRunning()) {
					execute();
					if (!taskSuccess) {
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
					}
					
					if(this.masterTask){
						this.masterTask=false;
					}
				}
			logger.info("Seems like services are down, it will not run further");
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
			Thread.sleep(Integer.parseInt(env.getProperty(System.getProperty("app").toUpperCase()+".waittime")));
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

	@Override
	public void load(IMonitorConfig config) throws MonitorException {
		Collection<Strategy> apps = config.getStrategyType();
		for(Strategy app : apps) {
			ITaskDef<?> task = context.getBean(app.name(), ITaskDef.class);
			allTasks.add(task);
			if(DDEConstants.MASTER.equalsIgnoreCase( app.name())){
				this.masterTask=true;
			}
				logger.info("Executing Task ----->>"+app.name().toUpperCase());
		}
		
	}
}
