package com.citi.dde.ach.task.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import com.citi.dde.ach.service.JobWatcherService;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.exception.MonitorException;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;


public class MasterTask extends ITaskRun {

	@Autowired
	private ConcurrentTaskExecutor taskExecutor;
	
	Logger log = Logger.getLogger(DDEConstants.MASTER_TASK);	
	@Autowired
	private ApplicationContext context;
	
	private String masterThreadName;
	
	@Autowired
	JobWatcherService jobWatcherService;
	
	@Autowired
	Environment env;
	
	private static volatile Map<String,String> activeTaskMap;
	
	private Integer executeAllTaskAsThread(){
		int allTask=0;
		try{
			List<Strategy> startergies = Strategy.MASTER.getSlaveStrategies();
			for (Strategy strategy : startergies) {
				String taskName = strategy.getStrategy();
				int threadCnt = getCount(taskName);
				if((threadCnt>0) && canSchedule(taskName)){
					for(int i=0;i<threadCnt;i++){
						Runnable task = context.getBean(strategy.name(), Runnable.class);
							taskExecutor.submit(task);	
						allTask++;
					}
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return allTask;
	}

	private int getCount(String taskName) {
		Integer toThreadCnt = getJobDetailMap().get(taskName).getThreadCount();
		if(toThreadCnt == null || toThreadCnt==0 ){
			return 0;
		}
		int currentThreadCnt =0;
		for (String thread : getActiveTaskMap().keySet()) {
			String status = getActiveTaskMap().get(thread);
			if(thread.contains(taskName) && DDEConstants.ACTIVE.equalsIgnoreCase(status)){
				++currentThreadCnt;
			}
		}
		return (toThreadCnt-currentThreadCnt);
	}

	private void monitorAllThread() {
		try{
			while(keepRunning(getMasterThreadName())){
				try{
					executeAllTaskAsThread();
				}finally {
					System.out.println("Master Paused");
					pause(DDEConstants.MASTER_PAUSE);
					refreshJobDetailMap();
					System.out.println("Refreshed...");
				}
			}	
		}catch(Exception e){
			//TODO
		}finally {
			ExecutorService es = ((ExecutorService)taskExecutor.getConcurrentExecutor());
			es.shutdownNow();
			es.shutdown();
			if(es.isShutdown()){
				System.out.println("Monitor shutdown...");
				pause(DDEConstants.MASTER_PAUSE);
				System.exit(DDEConstants.SYSTEM_EXIT);	
			}
			
		}
		
	}

	
	@PostConstruct
	public void init() throws TaskException {
		refreshJobDetailMap();
	}


	public Integer process() {
		try{
			setCurrentTheadName(DDEConstants.MASTER_TASK);
			monitorAllThread();
		}catch(Exception e){
			log.error(e,e);
		}
		 return 0;		 
	}

	public static Map<String, String> getActiveTaskMap() {
		if(activeTaskMap == null){
			activeTaskMap = new TreeMap<String,String>();
		}
		return activeTaskMap;
	}

	@Override
	public void run() {
			process();
	}

	public String getMasterThreadName() {
		return masterThreadName;
	}

	public void setMasterThreadName(String masterThreadName) {
		this.masterThreadName = masterThreadName;
	}

	
	
}
