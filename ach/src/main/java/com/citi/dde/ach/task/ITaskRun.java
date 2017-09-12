package com.citi.dde.ach.task;

import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.citi.dde.ach.entity.JobWatcherVO;
import com.citi.dde.ach.service.JobWatcherService;
import com.citi.dde.ach.task.impl.MasterTask;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.DDEConstants;

public abstract class ITaskRun implements Runnable {
	
	@Autowired
	Environment env;
	
	@Autowired
	JobWatcherService jobWatcherService;
	
	static volatile Integer timer =1;
	
	Logger log = Logger.getLogger(DDEConstants.MASTER_TASK);
	
	public boolean keepRunning(String threadName){
		synchronized (Thread.currentThread()) {
			System.out.println(threadName+DDEConstants.IS_RUNNING);
			return canSchedule(threadName);	
		}
	}
	
	private static volatile  Map<String,JobWatcherVO> jobDetailMap;
	
	public String setCurrentTheadName(String task){
			String poolThreadName =Thread.currentThread().getName();
			String threadName = null;
			synchronized (MasterTask.getActiveTaskMap()) {
					try{
						String[] array = poolThreadName.split(DDEConstants.THREAD_DELIMETER);
							String threadNo = array[array.length-1];
							threadName = task+DDEConstants.UNDERSCORE+threadNo;
					}finally {
						if(!StringUtils.isEmpty(threadName)){
							updateThreadStatus(threadName, DDEConstants.ACTIVE);
						}else{
							// throw exception;
						}
					}
				}
		return threadName;
	}
	
	public void pause(String task){
		try {
			Thread.sleep(Integer.parseInt(env.getProperty(task)));
		} catch (InterruptedException | NumberFormatException e) {
			log.error(e.getMessage());
			try {
				Thread.sleep(Integer.parseInt(env.getProperty(DDEConstants.DEFAULT_PAUSE),300000));
			} catch (NumberFormatException | InterruptedException e1) {
				log.error(e.getMessage());
			}
		}
		++timer;
	}

	public void updateThreadStatus(String threadName,String status) {
		synchronized (MasterTask.getActiveTaskMap()) { // Reschedule Task
				MasterTask.getActiveTaskMap().put(threadName,status);
				System.out.println("1."+MasterTask.getActiveTaskMap());
		}
	}
	
	public void refreshJobDetailMap() {
		try {
			setJobDetailMap(jobWatcherService.getAllJobDetailMap());
		} catch (TaskException e) {
			log.error(e,e);
		}
	}

	
	
	
	public boolean canSchedule(String threadName){
		String taskName = threadName.split(DDEConstants.UNDERSCORE)[0];
		JobWatcherVO temp =jobDetailMap.get(taskName);
		boolean flag = false;
		try{
			return flag = DDEConstants.ACTIVE.equalsIgnoreCase(temp.getJob_status());
		}finally {
			if(!flag){
				updateThreadStatus(threadName,DDEConstants.NOT_ELIGIBLE);
			}
		}
	}

	public static Map<String, JobWatcherVO> getJobDetailMap() {
		return jobDetailMap;
	}

	public static void setJobDetailMap(Map<String, JobWatcherVO> jobDetailMap) {
		ITaskRun.jobDetailMap = jobDetailMap;
	}
	
	
	
	
}
