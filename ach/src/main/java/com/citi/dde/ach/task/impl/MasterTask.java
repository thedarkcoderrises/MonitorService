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

import com.citi.dde.ach.service.JobWatcherService;
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
	JobWatcherService jobWatcherService;
	
	@Autowired
	Environment env;
	
	@Override
	public Integer call() throws Exception {
		return process();
	}
	
	private static volatile Map<String,String> activeTaskMap;
	

	private Integer executeAllTaskAsThread(){
		int allTask=0;
		try{
			List<Strategy> startergies = Strategy.MASTER.getSlaveStrategies();
			for (Strategy strategy : startergies) {
				String strategyCode = strategy.getStrategy();
				if(canSchedule(strategyCode)){
					int size = getJobDetailMap().get(strategyCode).getThreadCount();
					for(int i=0;i<size;i++){
						Runnable task = context.getBean(strategy.name(), Runnable.class);
						taskExecutor.submit(task);
						allTask++;
					}
				}else{
					log.info("NE as per JD of strategy :"+strategyCode);
				}
			}
			pause();
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return allTask;
	}


	private void monitorAllThread() {
		try{
			while(true){
				try{
					System.out.println(getThreadName()+DDEConstants.IS_RUNNING);
					Map<String,Integer> failThreadMap = getFailThreads();
					if(CollectionUtils.isEmpty(failThreadMap)){
						log.info("All Threads Spawned");
					}else{
						executeFailTaskAsThread(failThreadMap);
					}	
				}finally {
					pause();
					refreshJobDetailMap();
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
					if(canSchedule(strategyCode)){
						Runnable task = context.getBean(strategy, Runnable.class);
						taskExecutor.submit(task);
					}else{
						log.info("NE as per JD of strategy :"+strategy);
					}
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
				String strategyCode  = threadName.split(DDEConstants.UNDERSCORE)[0];
				if(DDEConstants.DEACTIVE.equals(isRunning)){
					if(!failThreadMap.containsKey(strategyCode)){
						failThreadMap.put(strategyCode, 1);
					}else{
						count = failThreadMap.get(strategyCode);
						failThreadMap.put(strategyCode, ++count);
					}
					log.error("Thread execution fail: "+threadName);
					log.error("Thread execution fail: "+threadName);
					getActiveTaskMap().put(threadName, DDEConstants.RE_SCHEDULE);
				}else if(DDEConstants.NOT_ELIGIBLE.equals(isRunning)){
					getActiveTaskMap().put(threadName, DDEConstants.RE_SCHEDULE);
					int threadSize = getJobDetailMap().get(strategyCode).getThreadCount();
					failThreadMap.put(strategyCode, threadSize);
				}else if(DDEConstants.RE_SCHEDULE.equals(isRunning)){
					int threadSize = getJobDetailMap().get(strategyCode).getThreadCount();
					failThreadMap.put(strategyCode, threadSize);
				}else if(DDEConstants.ACTIVE.equals(isRunning)){
					log.info("Thread: "+threadName+DDEConstants.IS_RUNNING);
				}
			}
		}
		System.out.println("3."+getActiveTaskMap());
		log.info("3."+getActiveTaskMap().toString());
		return failThreadMap;
	}


	@Override
	@PostConstruct
	public void init() throws TaskException {
		this.activeTaskMap = new TreeMap<String,String>();
		refreshJobDetailMap();
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
