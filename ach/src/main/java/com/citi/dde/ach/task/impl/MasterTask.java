package com.citi.dde.ach.task.impl;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.task.ITaskDef;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;


@Component("MASTER")
public class MasterTask implements ITaskDef<Integer>{

	@Autowired
	private ConcurrentTaskExecutor taskExecutor;
	
	@Autowired
	LoggingAspect log;
	
//	@Autowired
//	ThreadPoolTaskExecutor tpte;
	
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
			int threadCount=0;
			for (Strategy strategy : startergies) {
				if(!Strategy.MASTER.name().equalsIgnoreCase(strategy.name())){//Exclude MASTER Strategy
					int size = Integer.parseInt(env.getProperty(strategy.getStrategy()+DDEConstants.THREAD_SIZE));
						for(int i=0;i<size;i++){
							Runnable task = context.getBean(strategy.name(), Runnable.class);
							taskExecutor.submit(task);
							threadCount++;
						}
				}
			}
			if(activeThreads()==threadCount){
				log.info("All Threads Spawned", DDEConstants.MASTER_TASK);
			}else{
				
			}
			
		}catch(Exception e){
			throw new TaskException(e.getMessage(),DDEConstants.MASTER_TASK,e);
		}
		return null;
	}

	private int activeThreads() {
		//TODO
		return getActiveTaskMap().size();
	}

	@Override
	public void preInit() throws TaskException, FileNotFoundException {
		
	}

	@Override
	@PostConstruct
	public void init() throws TaskException {
		this.activeTaskMap = new HashMap<String,String>();
	}

	@Override
	public void preStart() throws TaskException {
	}

	@Override
	public void start() throws TaskException {
		
	}

	@Override
	public Integer finish() throws TaskException {
		return null;
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public Integer process() throws TaskException {
		return executeAllTaskAsThread();
	}

	public static Map<String, String> getActiveTaskMap() {
		return activeTaskMap;
	}


}
