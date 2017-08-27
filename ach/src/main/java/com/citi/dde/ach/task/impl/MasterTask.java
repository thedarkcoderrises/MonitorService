package com.citi.dde.ach.task.impl;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.task.ITaskDef;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.Strategy;


@Component("MASTER")
public class MasterTask implements ITaskDef<Integer>{

	@Autowired
	private ConcurrentTaskExecutor taskExecutor;
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	public Integer call() throws Exception {
		return process();
	}

	private Integer executeAllTaskAsThread() throws TaskException {
		try{
			Strategy[] startergies = Strategy.MASTER.getAllStratergies();
			for (Strategy strategy : startergies) {
				if(!Strategy.MASTER.name().equalsIgnoreCase(strategy.name())){//Exclude MASTER Strategy
					Runnable task = context.getBean(strategy.name(), Runnable.class);
//					((ITaskRun)task).setStrategy(strategy.name());;
					taskExecutor.submit(task);
				}
			}
		}catch(Exception e){
			throw new TaskException(e.getMessage(),this.getClass().getName(),e);
		}
		return null;
	}

	@Override
	public void preInit() throws TaskException, FileNotFoundException {
	}

	@Override
	public void init() throws TaskException {
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


}
