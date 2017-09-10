package com.citi.dde.ach.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.service.GiScrubTaskService;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;


@Component("GI_SCRUB")
@Scope("prototype")
public class GiScrubTask extends ITaskRun {

	@Autowired
	Environment env;
	
	@Autowired
	GiScrubTaskService giScrubTaskService;
	
	@Autowired
	LoggingAspect log;
	
	@Override
	public void run() {
			process();
	}

	public Integer process() {
		boolean failSafe = true;
		try{
			setCurrentTheadName(Strategy.GI_SCRUB);
			while(keepRunning()){
				giScrubTaskService.executeTask();
				pause();
			}	
		}catch(Exception e){
			failSafe = false;
			log.interceptException(e);
		}finally {
			if(!failSafe){
				updateThreadStatus(getThreadName(),DDEConstants.DEACTIVE);
			}
			System.out.println("Stop.."+ getThreadName());
		}
		
	return 0;
	}
}
