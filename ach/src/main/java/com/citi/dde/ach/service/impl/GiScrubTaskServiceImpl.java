package com.citi.dde.ach.service.impl;

import org.springframework.stereotype.Service;

import com.citi.dde.ach.service.GiScrubTaskService;
import com.citi.dde.common.exception.TaskException;

@Service
public class GiScrubTaskServiceImpl<Integer> implements GiScrubTaskService<Integer> {

	@Override
	public Integer executeTask() throws TaskException {
		try{
			if(true){
				throw new NullPointerException();
			}	
		}catch(Exception e){
			throw new TaskException(e.getMessage(), Thread.currentThread().getName(), e);
		}
		return null;
	}

}
