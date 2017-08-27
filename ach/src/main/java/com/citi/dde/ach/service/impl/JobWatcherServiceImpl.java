package com.citi.dde.ach.service.impl;

import java.util.Map;

import com.citi.dde.ach.entity.JobWatcherVO;
import com.citi.dde.ach.service.JobWatcherService;
import com.citi.dde.common.exception.TaskException;

public class JobWatcherServiceImpl implements JobWatcherService {

	@Override
	public JobWatcherVO getJobDetailForStrategy(String strategy) throws TaskException {
		return null;
	}

	@Override
	public Map<String, JobWatcherVO> getAllJobDetailMap() throws TaskException {
		return null;
	}

}
