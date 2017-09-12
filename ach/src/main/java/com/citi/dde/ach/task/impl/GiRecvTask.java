package com.citi.dde.ach.task.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.citi.dde.ach.service.GiRecvTaskService;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.util.DDEConstants;

public class GiRecvTask extends ITaskRun{

	@Autowired
	Environment env;
	
	@Autowired
	GiRecvTaskService giRecvTaskService;
	
	Logger log = Logger.getLogger(DDEConstants.GI_RECV_TASK);
	


	@Override
	public void run() {
		process();
	}

	public Integer process(){
		boolean isComplete = true;
		String threadName = DDEConstants.EMPTY_STRING;
		try{
			threadName=setCurrentTheadName(DDEConstants.GI_RECV_TASK);
			while(keepRunning(threadName)){
				giRecvTaskService.executeTask(threadName);
				pause(DDEConstants.MSG_RECV_PAUSE);
			}
		}catch(Exception e){
			isComplete = false;
			log.error(e,e);
		}finally {
			if(!isComplete){
				updateThreadStatus(threadName,DDEConstants.DEACTIVE);
			}
			System.out.println("Stop.."+ threadName);
		}
			
	return 0;
	}
	
}
