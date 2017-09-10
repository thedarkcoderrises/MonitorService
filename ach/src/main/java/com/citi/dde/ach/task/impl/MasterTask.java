package com.citi.dde.ach.task.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.service.JobWatcherService;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;


@Component("MASTER")
public class MasterTask extends ITaskRun {

	@Autowired
	private ConcurrentTaskExecutor taskExecutor;
	
	@Autowired
	LoggingAspect log;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	JobWatcherService jobWatcherService;
	
	@Autowired
	Environment env;
	
	private static volatile Map<String,String> activeTaskMap;
	
	private Integer executeAllTaskAsThread(){
		int allTask=0;
		try{
			List<Strategy> startergies = Strategy.MASTER.getSlaveStrategies();
//			boolean notEligibleFlag= false;
//			String stratergies = DDEConstants.EMPTY_STRING;
			for (Strategy strategy : startergies) {
				String strategyCode = strategy.getStrategy();
				int threadCnt = getCount(strategyCode);
				if((threadCnt>0) && canSchedule(strategyCode)){
					for(int i=0;i<threadCnt;i++){
						Runnable task = context.getBean(strategy.name(), Runnable.class);
							taskExecutor.execute(task);	
						allTask++;
					}
				}else{
//					notEligibleFlag=true;
//					if(stratergies.isEmpty()){
//						stratergies =strategyCode;
//					}else{
//						stratergies+=", "+strategyCode;
//					}
				}
			}
//			if(notEligibleFlag){
//				System.out.println("Strategies {"+stratergies+"} are Not Eligible");
//			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return allTask;
	}


	private int getCount(String strategyCode) {
		Integer toThreadCnt = getJobDetailMap().get(strategyCode).getThreadCount();
		if(toThreadCnt == null || toThreadCnt==0 ){
			return 0;
		}
		int currentThreadCnt =0;
		for (String thread : getActiveTaskMap().keySet()) {
			String status = getActiveTaskMap().get(thread);
			if(thread.contains(strategyCode) && DDEConstants.ACTIVE.equalsIgnoreCase(status)){
				++currentThreadCnt;
			}
		}
		return (toThreadCnt-currentThreadCnt);
	}

	private void monitorAllThread() {
		try{
			while(true){
				try{
					executeAllTaskAsThread();
//					System.out.println("1."+MasterTask.getActiveTaskMap());
				}finally {
					System.out.println("Master Paused");
					pause();
					refreshJobDetailMap();
					System.out.println("Refreshed...");
				}
			}	
		}catch(Exception e){
			log.interceptException(new TaskException(e.getMessage(),ITaskRun.getThreadName(),e));
		}
		
	}

	@PostConstruct
	public void init() throws TaskException {
		this.activeTaskMap = new TreeMap<String,String>();
		refreshJobDetailMap();
	}


	public Integer process() {
		try{
			setCurrentTheadName(Strategy.MASTER);
			monitorAllThread();
		}catch(Exception e){
			log.interceptException(e);
		}
		 return 0;		 
	}

	public static Map<String, String> getActiveTaskMap() {
		return activeTaskMap;
	}

	@Override
	public void run() {
			process();
	}

	
	
}
