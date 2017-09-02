package com.citi.dde.ach.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.service.GiRecvTaskService;
import com.citi.dde.ach.task.ITaskDef;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.exception.DdeExecutionException;
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
	public void init() throws TaskException {
	}


	@Override
	public void run() {
			process();
	}


	@Override
	public Integer process() {
		try {
			setCurrentTheadName(Strategy.MSG_RECV);
			while(keepRunning()){
				giRecvTaskService.executeTask();
				pause();
			}
		}catch (TaskException  e) {
			log.taskException(e);
		}
	return 0;
	}
	
}
