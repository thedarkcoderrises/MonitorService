package com.citi.dde.ach.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.util.DDEConstants;

public class GiSynchTask extends ITaskRun {

	
	@Autowired
	Environment env;
	
	@Override
	public void run() {
			process();
	}

	public Integer process() {
			boolean failSafe = true;
			String threadName = DDEConstants.EMPTY_STRING;
			try{
				threadName =setCurrentTheadName(DDEConstants.GI_SYNCH_TASK);
				while(keepRunning(threadName)){
					pause(DDEConstants.DATA_SYNC_PAUSE);
				}	
			}catch(Exception e){
				failSafe = false;
			}finally {
				if(!failSafe){
					updateThreadStatus(threadName,DDEConstants.DEACTIVE);
				}
				System.out.println("Stop.."+ threadName);
			}	
			
	return 0;
	}
}
