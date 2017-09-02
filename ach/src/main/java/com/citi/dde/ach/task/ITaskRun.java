package com.citi.dde.ach.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.citi.dde.ach.task.impl.MasterTask;
import com.citi.dde.common.exception.PauseException;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;

public abstract class ITaskRun implements Runnable {
	
	private Strategy strategy;
	
	@Autowired
	Environment env;
	
	public boolean keepRunning(){
		System.out.println(getThreadName()+DDEConstants.IS_RUNNING);
		return true;
	}
	
	public void setCurrentTheadName(Strategy strategy) {
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
				}
			}
		
	}
	
	public void pause() throws PauseException{
		try {
			Thread.sleep(Integer.parseInt(env.getProperty(this.strategy+DDEConstants.WAIT_TIME)));
		} catch (InterruptedException | NumberFormatException e) {
			try {
				Thread.sleep(Integer.parseInt(env.getProperty(DDEConstants.DEFAULT_PAUSE),300000));
			} catch (NumberFormatException | InterruptedException e1) {
				throw new PauseException(e);
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
