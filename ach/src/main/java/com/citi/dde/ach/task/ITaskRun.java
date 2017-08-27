package com.citi.dde.ach.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.citi.dde.common.exception.PauseException;
import com.citi.dde.common.util.DDEConstants;

public abstract class ITaskRun implements Runnable {
	
	private String strategy;
	
	@Autowired
	Environment env;
	
	public boolean keepRunning(){
//		System.out.println("KeepRunning :"+getStrategy()+" Current Thread :"+Thread.currentThread().getName());
		return true;
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

	public void setStrategy(String name) {
		this.strategy = name.toUpperCase();
	}

	public String getStrategy() {
		return strategy;
	}
	
	
	
}
