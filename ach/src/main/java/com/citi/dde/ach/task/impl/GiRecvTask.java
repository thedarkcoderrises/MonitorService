package com.citi.dde.ach.task.impl;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.service.GiRecvTaskService;
import com.citi.dde.ach.task.ITaskDef;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.exception.PauseException;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.Strategy;

@Component("MSG_RECV")
@Scope("prototype")
public class GiRecvTask extends ITaskRun implements ITaskDef<Integer>{

	@Autowired
	Environment env;
	
	@Autowired
	GiRecvTaskService giRecvTaskService;
	
	@Autowired
	LoggingAspect log;
	
	@Override
	public Integer call() throws Exception {
		return process();
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
	public void run() {
			process();
	}


	@Override
	public Integer process() {
		try {
			setStrategy(Strategy.MSG_RECV);
			while(keepRunning()){
				giRecvTaskService.executeTask();
				pause();
			}
		} catch (TaskException | PauseException e  ) {
			//do something
		}
	return 0;
	}
	
}
