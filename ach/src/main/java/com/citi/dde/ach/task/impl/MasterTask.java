package com.citi.dde.ach.task.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.citi.dde.ach.task.ITaskDef;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.exception.PauseException;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;


@Component("MASTER")
public class MasterTask implements ITaskDef<Integer>{

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
	

	private Integer executeAllTaskAsThread() throws TaskException {
		try{
			Strategy[] startergies = Strategy.MASTER.getAllStratergies();
			for (Strategy strategy : startergies) {
				if(!Strategy.MASTER.name().equalsIgnoreCase(strategy.name())){//Exclude MASTER Strategy
					int size = getThreadSize(strategy);
						for(int i=0;i<size;i++){
							Runnable task = context.getBean(strategy.name(), Runnable.class);
							taskExecutor.submit(task);
						}
				}
			}
			
		}catch(Exception e){
			throw new TaskException(e.getMessage(),DDEConstants.MASTER_TASK,e);
		}
		return 0;
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

	private void monitorAllThread() throws TaskException {
		try{
			while(true){
				pause();
				Map<String,Integer> failThreadMap = getFailThreads();
				if(CollectionUtils.isEmpty(failThreadMap)){
					log.info("All Threads Spawned", DDEConstants.MASTER_TASK);
				}else{
					executeFailTaskAsThread(failThreadMap);
				}
			}	
		}catch(Exception e){
			throw new TaskException(e.getMessage(),DDEConstants.MASTER_TASK,e);
		}
		
	}

	private void executeFailTaskAsThread(Map<String, Integer> failThreadMap) throws TaskException{
		try{
			for (String strategyCode : failThreadMap.keySet()) {
				int size = failThreadMap.get(strategyCode);
				for(int i=0;i<size;i++){
					Runnable task = context.getBean(Strategy.getStrategy(strategyCode).name(), Runnable.class);
					taskExecutor.submit(task);
				}
			}
		}catch(Exception e){
			throw new TaskException(e.getMessage(),DDEConstants.MASTER_TASK,e);
		}	
	}

	private Map<String,Integer> getFailThreads() {
		Set<String> activeThreadSet = getActiveTaskMap().keySet();
		Map<String,Integer> failThreadMap = new HashMap<>();
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
				getActiveTaskMap().put(threadName, DDEConstants.NOT_APPLICABLE);
			}else if(DDEConstants.ACTIVE.equals(isRunning)){
				log.info("Thread: "+threadName+DDEConstants.IS_RUNNING, DDEConstants.MASTER_TASK);
			}
		}
		
		return failThreadMap;
	}


	@Override
	@PostConstruct
	public void init() throws TaskException {
		this.activeTaskMap = new HashMap<String,String>();
	}


	@Override
	public Integer process() throws TaskException {
		 executeAllTaskAsThread();
		 monitorAllThread();
		 return 0;
	}

	public static Map<String, String> getActiveTaskMap() {
		return activeTaskMap;
	}

	public void pause() throws PauseException{
		try {
			Thread.sleep(Integer.parseInt(env.getProperty(DDEConstants.MASTER_PAUSE)));
		} catch (InterruptedException | NumberFormatException e) {
			try {
				Thread.sleep(Integer.parseInt(env.getProperty(DDEConstants.DEFAULT_PAUSE),300000));
			} catch (NumberFormatException | InterruptedException e1) {
				throw new PauseException(e);
			}
		}
	}

}
