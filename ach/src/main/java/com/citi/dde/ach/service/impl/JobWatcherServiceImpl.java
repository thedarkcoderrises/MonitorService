package com.citi.dde.ach.service.impl;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citi.dde.ach.dao.JobWatcherDao;
import com.citi.dde.ach.entity.JobWatcherVO;
import com.citi.dde.ach.service.JobWatcherService;
import com.citi.dde.common.exception.TaskException;
@Service
@Transactional(readOnly=true)
public class JobWatcherServiceImpl implements JobWatcherService {
	
	@Autowired
	JobWatcherDao<JobWatcherVO> jobWatcherDao;
	
	@Override
	public JobWatcherVO getJobDetailForStrategy(String strategy) throws TaskException {
		return getAllJobDetailMap().get(strategy);
	}

	@Override
	public Map<String, JobWatcherVO> getAllJobDetailMap() throws TaskException {
		return jobWatcherDao.getAllJobDetailMap();
	}

}
