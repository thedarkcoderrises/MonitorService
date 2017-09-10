package com.citi.dde.ach.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;

@Component("DATA_SYNC")
@Scope("prototype")
public class GiSynchTask extends ITaskRun {

	
	@Autowired
	Environment env;
	
	@Override
	public void run() {
			process();
	}

	public Integer process() {
			boolean failSafe = true;
			try{
				setCurrentTheadName(Strategy.DATA_SYNC);
				while(keepRunning()){
					pause();
				}	
			}catch(Exception e){
				failSafe = false;
			}finally {
				if(!failSafe){
					updateThreadStatus(getThreadName(),DDEConstants.DEACTIVE);
				}
				System.out.println("Stop.."+ getThreadName());
			}	
			
	return 0;
	}
}
