package com.citi.dde.ach.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.service.GiScrubTaskService;
import com.citi.dde.ach.task.ITaskDef;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.Strategy;


@Component("GI_SCRUB")
@Scope("prototype")
public class GiScrubTask extends ITaskRun implements ITaskDef<Integer>{

	@Autowired
	Environment env;
	
	@Autowired
	GiScrubTaskService giScrubTaskService;
	
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
		try {
			process();
		} catch (TaskException e) {
			//
		}
	}


	@Override
	public Integer process() throws TaskException {
		try {
			setCurrentTheadName(Strategy.GI_SCRUB);
			while(keepRunning()){
				giScrubTaskService.executeTask();
				pause();
			}
		} catch (TaskException e) {
			log.taskException(e);
		}
	return 0;
	}
}
