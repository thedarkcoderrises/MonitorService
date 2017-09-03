package com.citi.dde.ach.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.task.ITaskDef;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.Strategy;

@Component("MSG_SEND")
@Scope("prototype")
public class GiSendTask extends ITaskRun implements ITaskDef<Integer>{

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
			process();
	}


	@Override
	public Integer process(){
			setCurrentTheadName(Strategy.MSG_SEND);
			while(keepRunning()){
				pause();
			}
	return 0;
	}
}
