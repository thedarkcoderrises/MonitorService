package com.citi.dde.ach.service.impl;

import org.springframework.stereotype.Service;

import com.citi.dde.ach.service.GiScrubTaskService;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.exception.TaskException;

@Service
public class GiScrubTaskServiceImpl implements GiScrubTaskService {

	@Override
	public Integer executeTask() throws TaskException {
		try{
			if(true){
				Thread.sleep(4000);
				throw new NullPointerException();
			}	
		}catch(Exception e){
			throw new TaskException(e.getMessage(), ITaskRun.getThreadName(), e);
		}
		return null;
	}

}
