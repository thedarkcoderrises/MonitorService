package com.citi.dde.ach;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.citi.dde.ach.config.ACHConfig;
import com.citi.dde.common.monitor.IMonitorDef;
import com.citi.dde.common.monitor.MQMonitor;


/**
 * Hello world!
 *
 */
public class MainProcess 
{
	static int THREAD_SLEEP=1000;
	
	
    public static void main( String[] args )
    {
    	
    	try{
    	ApplicationContext context = new AnnotationConfigApplicationContext(ACHConfig.class);
    	
    	IMonitorDef monitor = context.getBean("mq-monitor", MQMonitor.class);
		Thread mainThread = new Thread(monitor);
		mainThread.start();
		
		
		}catch(Exception e)
		{			
			System.out.println(e);
		}
		try {
			Thread.sleep(THREAD_SLEEP);
		} catch (InterruptedException e) {
			System.out.println(e);
		}

    	
    }
}
