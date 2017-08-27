package com.citi.dde.ach.service;

import java.util.Map;

import com.citi.dde.ach.entity.JobWatcherVO;
import com.citi.dde.common.exception.TaskException;

public interface JobWatcherService {

	public JobWatcherVO getJobDetailForStrategy(String strategy) throws TaskException;
	
	public Map<String,JobWatcherVO> getAllJobDetailMap() throws TaskException;
	
	
}
