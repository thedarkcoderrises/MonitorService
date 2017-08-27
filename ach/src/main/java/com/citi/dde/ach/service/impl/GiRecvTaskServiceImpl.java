package com.citi.dde.ach.service.impl;

import org.springframework.stereotype.Service;

import com.citi.dde.ach.service.GiRecvTaskService;

@Service
public class GiRecvTaskServiceImpl implements GiRecvTaskService {

	@Override
	public Integer executeTask() {
		System.out.println("GiRecvTask..");
		if(true){
			throw new NullPointerException();
		}
		return 0;
	}

}
