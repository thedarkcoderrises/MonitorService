package com.citi.dde.ach.task.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.citi.dde.ach.task.ITaskDef;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;


@Component("MASTER")
public class MasterTask extends ITaskRun implements ITaskDef<Integer>{

	@Autowired
	private ConcurrentTaskExecutor taskExecutor;
	
	@Autowired
	LoggingAspect log;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	Environment env;
	
	@Override
	public Integer call() throws Exception {
		return process();
	}
	
	private static Map<String,String> activeTaskMap;
	

	private Integer executeAllTaskAsThread(){
		int allTask=0;
		try{
			List<Strategy> startergies = Strategy.MASTER.getSlaveStrategies();
			for (Strategy strategy : startergies) {
					int size = getThreadSize(strategy);
						for(int i=0;i<size;i++){
							Runnable task = context.getBean(strategy.name(), Runnable.class);
							taskExecutor.submit(task);
							allTask++;
						}
			}
			pause();
		}catch(Exception e){
			log.interceptException(new TaskException(e.getMessage(),DDEConstants.MASTER_TASK,e));
		}
		return allTask;
	}

	private int getThreadSize(Strategy strategy) {
		String count = env.getProperty(strategy.getStrategy()+DDEConstants.THREAD_SIZE);
		int cnt =0;
		if(!StringUtils.isEmpty(count)){
			try{
				cnt= Integer.parseInt(count);	
			}catch(NumberFormatException e){
				cnt =DDEConstants.DEFAULT_THREAD_COUNT;
			}finally{
				log.info(strategy.getStrategy()+" Thread Count : "+cnt, DDEConstants.MASTER_TASK);
			}
		}
		return cnt;
	}

	private void monitorAllThread() {
		try{
			while(keepRunning()){
				try{
					Map<String,Integer> failThreadMap = getFailThreads();
					if(CollectionUtils.isEmpty(failThreadMap)){
						log.info("All Threads Spawned", DDEConstants.MASTER_TASK);
					}else{
						executeFailTaskAsThread(failThreadMap);
					}	
				}finally {
					pause();
				}
			}	
		}catch(Exception e){
			log.interceptException(new TaskException(e.getMessage(),ITaskRun.getThreadName(),e));
		}
		
	}

	private void executeFailTaskAsThread(Map<String, Integer> failThreadMap) throws TaskException{
		try{
			for (String strategyCode : failThreadMap.keySet()) {
				int size = failThreadMap.get(strategyCode);
				for(int i=0;i<size;i++){
					String strategy = Strategy.getStrategy(strategyCode).name();
					Runnable task = context.getBean(strategy, Runnable.class);
					taskExecutor.submit(task);
				}
			}
		}catch(Exception e){
			throw new TaskException(e.getMessage(),DDEConstants.MASTER_TASK,e);
		}	
	}

	private Map<String,Integer> getFailThreads() {
		Map<String,Integer> failThreadMap = new HashMap<>();
		synchronized (getActiveTaskMap()) {
			Set<String> activeThreadSet = getActiveTaskMap().keySet();
			int count =0;
			for (String threadName : activeThreadSet) {
				String isRunning = getActiveTaskMap().get(threadName);
				if(DDEConstants.DEACTIVE.equals(isRunning)){
					String strategyCode  = threadName.split(DDEConstants.UNDERSCORE)[0];
					if(!failThreadMap.containsKey(strategyCode)){
						failThreadMap.put(strategyCode, 1);
					}else{
						count = failThreadMap.get(strategyCode);
						failThreadMap.put(strategyCode, ++count);
					}
					log.error("Thread execution fail: "+threadName, threadName);
					log.error("Thread execution fail: "+threadName, DDEConstants.MASTER_TASK);
					getActiveTaskMap().put(threadName, DDEConstants.RE_SCHEDULE);
				}else if(DDEConstants.ACTIVE.equals(isRunning)){
					log.info("Thread: "+threadName+DDEConstants.IS_RUNNING, DDEConstants.MASTER_TASK);
				}
			}
		}
		System.out.println("3."+getActiveTaskMap());
		log.info("3."+getActiveTaskMap().toString(),DDEConstants.MASTER_TASK);
		return failThreadMap;
	}


	@Override
	@PostConstruct
	public void init() throws TaskException {
		this.activeTaskMap = new TreeMap<String,String>();
	}


	@Override
	public Integer process() {
		try{
			setCurrentTheadName(Strategy.MASTER);
			 executeAllTaskAsThread();
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
