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
		MasterTask.getActiveTaskMap().put(getCurrentTheadName(), DDEConstants.ACTIVE);
		return true;
	}
	
	private String getCurrentTheadName() {
		String threadName =Thread.currentThread().getName();
		if(threadName.contains(this.strategy.getStrategy())){
			return threadName;
		}else{
			String threadNo = threadName.split(DDEConstants.THREAD_DELIMETER)[1];
			threadName = this.strategy.getStrategy()+DDEConstants.UNDERSCORE+threadNo;
			Thread.currentThread().setName(threadName);
		}
		return threadName;
	}
	
	public void pause() throws PauseException{
		try {
			Thread.sleep(Integer.parseInt(env.getProperty(getStrategy()+DDEConstants.WAIT_TIME)));
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
	
	
	
}
