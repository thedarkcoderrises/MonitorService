package com.citi.dde.ach.service.impl;

import org.springframework.stereotype.Service;

import com.citi.dde.ach.service.GiRecvTaskService;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.DDEConstants;

@Service
public class GiRecvTaskServiceImpl implements GiRecvTaskService {

	static boolean firstThread = true;
	
	@Override
	public Integer executeTask() throws TaskException {
		synchronized (this) {
			try{
				System.out.println("GiRecvTask..");
				if(firstThread){
					throw new NullPointerException();
				}	
			}catch(Exception e){
				firstThread=false;
				throw new TaskException(e.getMessage(), Thread.currentThread().getName(), e);
			}
		}
		
		return 0;
	}

	
	
}
