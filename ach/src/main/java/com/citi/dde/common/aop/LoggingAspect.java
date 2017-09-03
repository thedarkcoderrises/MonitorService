package com.citi.dde.common.aop;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.ach.task.impl.MasterTask;
import com.citi.dde.common.exception.CommonException;
import com.citi.dde.common.exception.DdeExecutionException;
import com.citi.dde.common.exception.TaskException;
import com.citi.dde.common.util.DDEConstants;

@Aspect
@Component
public class LoggingAspect {

	Logger log = LogManager.getLogger(this.getClass());
	
	
	@After("execution(* com.citi.dde.ach.*.*.*(..))")
	public void after(JoinPoint joinPoint) {
		logBeforeAndAfter(joinPoint, DDEConstants.AFTER_LOG);
	}
	
	@Before("execution(* com.citi.dde.ach.*.*.*(..))")
	public void before(JoinPoint joinPoint) {
		logBeforeAndAfter(joinPoint,DDEConstants.BEFORE_LOG);
	}

	private void logBeforeAndAfter(JoinPoint joinPoint, String logCase) {
		Logger taskLog =getLogger(joinPoint.getTarget().getClass().getName());
		taskLog.debug(logCase + "Target Class :" +joinPoint.getTarget().getClass().getName() +", Method :"+joinPoint.getSignature().getName());
	}
	
	
	
	private Logger getLogger(String taskName) {
		Logger taskLog;
		if(taskName == null || taskName.isEmpty()){
			return log;
		}
		synchronized (taskName) {
			if(taskName.contains(DDEConstants.MASTER_TASK_CLASS)){
				taskLog = Logger.getLogger(DDEConstants.MASTER_TASK_CLASS);
			}else if(taskName.contains(DDEConstants.TASK1_CLASS)){
				taskLog = Logger.getLogger(DDEConstants.TASK1_CLASS);
			}else if(taskName.contains(DDEConstants.TASK2_CLASS)){
				taskLog = Logger.getLogger(DDEConstants.TASK2_CLASS);
			}else if(taskName.contains(DDEConstants.TASK3_CLASS)){
				taskLog = Logger.getLogger(DDEConstants.TASK3_CLASS);
			}else if(taskName.contains(DDEConstants.TASK4_CLASS)){
				taskLog = Logger.getLogger(DDEConstants.TASK4_CLASS);
			}else if(taskName.contains(DDEConstants.TASK5_CLASS)){
				taskLog = Logger.getLogger(DDEConstants.TASK5_CLASS);
			}else{
				taskLog =log;
			}
			return taskLog;
		}
	}

//	@AfterThrowing(pointcut = "execution(* com.citi.dde.ach.*.*.*(..))", throwing = "ex")
	private void errorInterceptor(Exception ex) {
		Logger taskLog;
		if(ex instanceof CommonException){
			String loggerName = ITaskRun.getThreadName();
			taskLog = getLogger(loggerName);
		}else{
			taskLog = log;
		}
	    if (taskLog.isDebugEnabled()) {
	    	taskLog.debug("Error Message Interceptor started");
	    }
	    // DO SOMETHING HERE WITH EX
	    taskLog.debug("Cause :"+ex.getMessage());
	    taskLog.debug("Exception @ :"+ex.getStackTrace()[0]);
	    if (taskLog.isDebugEnabled()) {
	    	taskLog.debug("Error Message Interceptor finished.");
	    }
	}
	
	public void debug(String info){
		log(info,getLogger(ITaskRun.getThreadName()),DDEConstants.DEBUG);
	}
	
	public void info(String info){
		log(info,getLogger(ITaskRun.getThreadName()),DDEConstants.INFO);
	}
	
	public void error(String info){
		log(info,getLogger(ITaskRun.getThreadName()),DDEConstants.ERROR);
	}
	
	
	private void log(String info, Logger logger, String logType) {
		switch(logType){
			case DDEConstants.DEBUG : logger.debug(info); break;
			case DDEConstants.INFO : logger.info(info); break;
			case DDEConstants.ERROR : logger.error(info); break;
			default :logger.debug(info); break;
		}
	}

//	@AfterThrowing(pointcut = "execution(* com.citi.dde.ach.*.*.*(..))", throwing = "ex")
	public void interceptException(Exception ex) {
		if(ex instanceof DdeExecutionException){
			errorInterceptor((DdeExecutionException)ex);
		}else if(ex instanceof TaskException){
			errorInterceptor((TaskException)ex);
		}else{
			synchronized (MasterTask.getActiveTaskMap()) { // Reschedule Task
				String threadName = ITaskRun.getThreadName();
				String isRunning = MasterTask.getActiveTaskMap().get(threadName);
				if (DDEConstants.ACTIVE.equals(isRunning)){
					MasterTask.getActiveTaskMap().put(threadName,DDEConstants.DEACTIVE);
					System.out.println("2."+MasterTask.getActiveTaskMap());
					info("2."+MasterTask.getActiveTaskMap().toString());
					raiseVT();
				}else{
					error("Invalid Thread :"+threadName+" its status :"+isRunning);
				}
			}
			errorInterceptor(ex);
		}
	}
	
	public void ddeExecutionException(DdeExecutionException ex) {
		errorInterceptor(ex);
	}

	private void raiseVT() {
		info("VT Raised");
	}
}
