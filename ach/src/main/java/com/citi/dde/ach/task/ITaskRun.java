package com.citi.dde.ach.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

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
	
//	@Autowired
//	ConcurrentTaskExecutor taskExecutor;

	static volatile Integer timer =1;
	
	@Autowired
	LoggingAspect log;
	
	public boolean keepRunning(){
		String currentThread = getThreadName();
		synchronized (currentThread) {
			System.out.println(currentThread+DDEConstants.IS_RUNNING);
			log.info(currentThread+DDEConstants.IS_RUNNING);
			return canSchedule(currentThread);	
		}
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
//						log.info("1."+MasterTask.getActiveTaskMap().toString());
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
		}
	}
	
	/*public void removeTaskFromPool() {
		Thread t = Thread.currentThread();
		synchronized (t) {
			ThreadPoolExecutor tpe = (ThreadPoolExecutor) taskExecutor.getConcurrentExecutor();
			try{
				boolean flag =tpe.getQueue().remove(t);
				if(flag){
					System.out.println("removed from pool :"+getThreadName());
				}else{
					System.out.println("fail to remove from pool :"+getThreadName());
				}
			}catch(Exception e){
				e.printStackTrace();
			}			
		}
	}*/

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
