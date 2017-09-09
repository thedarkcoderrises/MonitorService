package com.citi.dde.ach.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.citi.dde.ach.dao.JobWatcherDao;
import com.citi.dde.ach.entity.JobWatcherVO;
import com.citi.dde.common.util.DDEConstants;

@Repository
public class JobWatcherDaoImpl extends GenericDaoImpl<JobWatcherVO> implements JobWatcherDao<JobWatcherVO>{

	
	@Override
	public Map<String, JobWatcherVO> getAllJobDetailMap() {
		List<JobWatcherVO> lst = super.getAllDetails(JobWatcherVO.class);
		Map<String, JobWatcherVO> resultMap = new HashMap<>();
		int totalThreadCount =0;
		for (JobWatcherVO jobWatcherVO : lst) {
			resultMap.put(jobWatcherVO.getJobid(), jobWatcherVO);
			totalThreadCount+=jobWatcherVO.getThreadCount();
		}
		JobWatcherVO masterJob = new JobWatcherVO();
		masterJob.setThreadCount(totalThreadCount+1);//including Master for Executor pool size
		resultMap.put(DDEConstants.MASTER,masterJob);
		return resultMap;
	}

}
