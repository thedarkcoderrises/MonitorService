package com.citi.dde.ach.task.impl;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.citi.dde.ach.service.GiScrubTaskService;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.util.DDEConstants;


public class GiScrubTask extends ITaskRun {

	@Autowired
	Environment env;
	
	@Autowired
	GiScrubTaskService giScrubTaskService;
	
	Logger log = Logger.getLogger(DDEConstants.GI_SCRUB_TASK);	
	@Override
	public void run() {
			process();
	}

	public Integer process() {
		boolean failSafe = true;
		String threadName = DDEConstants.EMPTY_STRING;
		try{
			threadName=	setCurrentTheadName(DDEConstants.GI_SCRUB_TASK);
			while(keepRunning(threadName)){
				giScrubTaskService.executeTask(threadName);
				pause(DDEConstants.GI_SCRUB_PAUSE);
			}	
		}catch(Exception e){
			failSafe = false;
			log.error(e);
		}finally {
			if(!failSafe){
				updateThreadStatus(threadName,DDEConstants.DEACTIVE);
			}
			System.out.println("Stop.."+threadName);
		}
		
	return 0;
	}
}
