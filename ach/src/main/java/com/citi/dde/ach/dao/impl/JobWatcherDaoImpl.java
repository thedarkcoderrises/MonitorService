package com.citi.dde.ach.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.citi.dde.ach.dao.JobWatcherDao;
import com.citi.dde.ach.entity.JobWatcherVO;

@Repository
public class JobWatcherDaoImpl extends GenericDaoImpl<JobWatcherVO> implements JobWatcherDao<JobWatcherVO>{

	
	@Override
	public Map<String, JobWatcherVO> getAllJobDetailMap() {
		List<JobWatcherVO> lst = super.getAllDetails(JobWatcherVO.class);
		Map<String, JobWatcherVO> resultMap = new HashMap<>();
		for (JobWatcherVO jobWatcherVO : lst) {
			resultMap.put(jobWatcherVO.getJobid(), jobWatcherVO);
		}
		return resultMap;
	}

}
