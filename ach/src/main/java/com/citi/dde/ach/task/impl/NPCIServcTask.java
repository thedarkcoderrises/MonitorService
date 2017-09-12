package com.citi.dde.ach.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;


public class NPCIServcTask extends ITaskRun{

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
				threadName =setCurrentTheadName(DDEConstants.NPCI_SVC_TASK);
				while(keepRunning(threadName)){
					pause(DDEConstants.NPCI_SVC_PAUSE);
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
