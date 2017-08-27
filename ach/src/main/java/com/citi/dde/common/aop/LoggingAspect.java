package com.citi.dde.common.aop;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.citi.dde.common.exception.CommonException;
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
		taskLog.debug(logCase + joinPoint.getSignature());
	}
	
	
	
	private Logger getLogger(String taskName) {
		Logger taskLog;
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

	@AfterThrowing(pointcut = "execution(* com.citi.dde.ach.*.*.*(..))", throwing = "ex")
	public void errorInterceptor(Exception ex) {
		Logger taskLog;
		if(ex instanceof CommonException){
			taskLog = getLogger(((CommonException)ex).getClassName());
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
}
