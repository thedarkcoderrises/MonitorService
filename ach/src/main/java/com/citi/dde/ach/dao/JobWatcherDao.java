package com.citi.dde.ach.dao;

import java.util.Map;

import com.citi.dde.ach.entity.JobWatcherVO;

public interface JobWatcherDao<JobWatcherVO> extends GenericDao<JobWatcherVO> {

	Map<String, JobWatcherVO> getAllJobDetailMap();

}
