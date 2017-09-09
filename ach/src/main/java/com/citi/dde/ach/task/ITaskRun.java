package com.citi.dde.ach.task;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.citi.dde.ach.entity.JobWatcherVO;
import com.citi.dde.ach.service.JobWatcherService;
import com.citi.dde.ach.task.impl.MasterTask;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.DDEConstants;
import com.citi.dde.common.util.Strategy;

public abstract class ITaskRun implements Runnable {
	
	private Strategy strategy;
	
	@Autowired
	Environment env;
	
	@Autowired
	JobWatcherService jobWatcherService;
	
	static volatile Integer timer =1;
	
	@Autowired
	LoggingAspect log;
	
	public boolean keepRunning(){
		System.out.println(getThreadName()+DDEConstants.IS_RUNNING);
		log.info(getThreadName()+DDEConstants.IS_RUNNING);
		return canSchedule(getThreadName());
	}
	
	private static volatile  Map<String,JobWatcherVO> jobDetailMap;
	
	public void setCurrentTheadName(Strategy strategy){
		try{
			String threadName =ITaskRun.getThreadName();
			synchronized (MasterTask.getActiveTaskMap()) {
					try{
						this.strategy = strategy;
						if(threadName.contains(strategy.getStrategy())){
							return;
						}else{
							String threadNo = threadName.split(DDEConstants.THREAD_DELIMETER)[1];
							threadName = strategy.getStrategy()+DDEConstants.UNDERSCORE+threadNo;
							Thread.currentThread().setName(threadName);
						}
					}finally {
						MasterTask.getActiveTaskMap().put(threadName, DDEConstants.ACTIVE);
						System.out.println("1."+MasterTask.getActiveTaskMap());
						log.info("1."+MasterTask.getActiveTaskMap().toString());
					}
				}
			
		}catch(Exception e){
			log.error(e.getMessage());			
		}
		
	}
	
	public void pause(){
		try {
			Thread.sleep(Integer.parseInt(env.getProperty(this.strategy+DDEConstants.WAIT_TIME)));
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

	public void setStrategy(Strategy name) {
		this.strategy = name;
	}

	public Strategy getStrategy() {
		return strategy;
	}
	
	public static String getThreadName() {
		return Thread.currentThread().getName();
	}
	
	public void updateThreadStatus(String threadName,String status) {
		synchronized (MasterTask.getActiveTaskMap()) { // Reschedule Task
				MasterTask.getActiveTaskMap().put(threadName,status);
				System.out.println("3."+MasterTask.getActiveTaskMap());
				log.info("3."+MasterTask.getActiveTaskMap().toString());
		}
	}
	
	public void refreshJobDetailMap() {
		try {
			setJobDetailMap(jobWatcherService.getAllJobDetailMap());
		} catch (TaskException e) {
			log.interceptException(e);
		}
	}

	public boolean canSchedule(String strategy){
		String threadName= strategy;
		strategy = strategy.split(DDEConstants.UNDERSCORE)[0];
		JobWatcherVO temp =jobDetailMap.get(strategy);
		boolean flag = false;
		try{
			return flag = temp.getThreadCount()>0;
		}finally {
			if(!flag){
				log.info("4.NE as per JD of strategy :"+threadName);
				System.out.println("4.NE as per JD of strategy :"+threadName);
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
