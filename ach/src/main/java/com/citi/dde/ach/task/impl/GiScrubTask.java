package com.citi.dde.ach.task.impl;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.service.GiScrubTaskService;
import com.citi.dde.ach.task.ITaskDef;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.exception.PauseException;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;


@Component("GI_SCRUB")
@Scope("prototype")
public class GiScrubTask extends ITaskRun implements ITaskDef<Integer>{

	@Autowired
	Environment env;
	
	@Autowired
	GiScrubTaskService giScrubTaskService;
	
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
		try {
			process();
		} catch (TaskException e) {
			e.printStackTrace();
		}
	}


	@Override
	public Integer process() throws TaskException {
		try {
			setStrategy(Strategy.GI_SCRUB);
			while(keepRunning()){
				giScrubTaskService.executeTask();
				pause();
			}
		} catch (Exception e) {
			throw new TaskException(e.getMessage());
		}
	return 0;
	}
}
