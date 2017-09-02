package com.citi.dde.ach.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.task.ITaskDef;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.Strategy;


@Component("NPCI_SVC")
@Scope("prototype")
public class NPCIServcTask extends ITaskRun implements ITaskDef<Integer>{

	@Autowired
	Environment env;
	
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
			e.printStackTrace();
		}
	}


	@Override
	public Integer process() throws TaskException {
		try {
			setCurrentTheadName(Strategy.NPCI_SVC);
			while(keepRunning()){
				pause();
			}
		} catch (Exception e) {
			throw new TaskException(e.getMessage(),ITaskRun.getThreadName(),e);
		}
	return 0;
	}
}
