package com.citi.dde.ach.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.service.GiRecvTaskService;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;

@Component("MSG_RECV")
@Scope("prototype")
public class GiRecvTask extends ITaskRun{

	@Autowired
	Environment env;
	
	@Autowired
	GiRecvTaskService giRecvTaskService;
	
	@Autowired
	LoggingAspect log;
	


	@Override
	public void run() {
		process();
	}

	public Integer process(){
		boolean isComplete = true;
		try{
			setCurrentTheadName(Strategy.MSG_RECV);
			while(keepRunning()){
				giRecvTaskService.executeTask();
				pause();
			}
		}catch(Exception e){
			isComplete = false;
			log.interceptException(e);
		}finally {
			if(!isComplete){
				updateThreadStatus(getThreadName(),DDEConstants.DEACTIVE);
			}
			System.out.println("Stop.."+ getThreadName());
//			removeTaskFromPool();
		}
			
	return 0;
	}
	
}
