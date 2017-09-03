package com.citi.dde.ach.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.citi.dde.ach.task.impl.MasterTask;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.exception.DdeExecutionException;
import com.citi.dde.common.exception.PauseException;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;

public abstract class ITaskRun implements Runnable {
	
	private Strategy strategy;
	
	@Autowired
	Environment env;
	
	@Autowired
	LoggingAspect log;
	
	public boolean keepRunning(){
		System.out.println(getThreadName()+DDEConstants.IS_RUNNING);
		log.info(getThreadName()+DDEConstants.IS_RUNNING);
		return true;
	}
	
	public void setCurrentTheadName(Strategy strategy){
		try{
			String threadName =ITaskRun.getThreadName();
			synchronized (MasterTask.getActiveTaskMap()) {
					try{
						this.strategy = strategy;
						if(threadName.contains(strategy.getStrategy())){
							return;
						}else{
							String threadNo = threadName.split(DDEConstants.THREAD_DELIMETER)[1];
							threadName = strategy.getStrategy()+DDEConstants.UNDERSCORE+threadNo;
							Thread.currentThread().setName(threadName);
						}
					}finally {
						MasterTask.getActiveTaskMap().put(threadName, DDEConstants.ACTIVE);
						System.out.println("1."+MasterTask.getActiveTaskMap());
						log.info("1."+MasterTask.getActiveTaskMap().toString());
					}
				}
			
		}catch(Exception e){
			log.error(e.getMessage());			
		}
		
	}
	
	public void pause(){
		try {
			Thread.sleep(Integer.parseInt(env.getProperty(this.strategy+DDEConstants.WAIT_TIME)));
		} catch (InterruptedException | NumberFormatException e) {
			log.error(e.getMessage());
			try {
				Thread.sleep(Integer.parseInt(env.getProperty(DDEConstants.DEFAULT_PAUSE),300000));
			} catch (NumberFormatException | InterruptedException e1) {
				log.error(e.getMessage());
			}
		}
	}

	public void setStrategy(Strategy name) {
		this.strategy = name;
	}

	public Strategy getStrategy() {
		return strategy;
	}
	
	public static String getThreadName() {
		return Thread.currentThread().getName();
	}
	
}
