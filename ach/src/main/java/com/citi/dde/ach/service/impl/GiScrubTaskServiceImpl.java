package com.citi.dde.ach.service.impl;

import org.springframework.stereotype.Service;

import com.citi.dde.ach.service.GiScrubTaskService;

@Service
public class GiScrubTaskServiceImpl implements GiScrubTaskService {

	@Override
	public Integer executeTask() {
		System.out.println("GiScrubTask..");
		return 0;
	}

}
