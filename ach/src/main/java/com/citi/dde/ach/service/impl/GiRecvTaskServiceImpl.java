package com.citi.dde.ach.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citi.dde.ach.service.GiRecvTaskService;
import com.citi.dde.ach.task.ITaskRun;
import com.citi.dde.common.aop.LoggingAspect;
import com.citi.dde.common.exception.DdeExecutionException;
import com.citi.dde.common.exception.TaskException;

@Service
public class GiRecvTaskServiceImpl implements GiRecvTaskService {

	static boolean firstThread = true;
	
	@Autowired
	LoggingAspect log;
	
	static List<String> temp= new ArrayList<String>();
	List<String> files= new ArrayList<String>();
	
	@PostConstruct
	void init(){
		if(files.isEmpty()){	
			for(int i =1;i<11;i++){
				files.add("F"+i);	
			}
		}
	}
	@Override
	public Integer executeTask() throws Exception {
		synchronized (this) {
				if(firstThread){
					firstThread=false;
					throw new NullPointerException(); //unpredictable exception
				}else{
					try{
						businessLogic1();	
					}catch(DdeExecutionException e){
						log.ddeExecutionException(e);
						businessLogic2();
					}
				}	
		}
//		processCommand();
		return 0;
	}

	private void businessLogic2() {
		return;
	}
	private void businessLogic1() throws DdeExecutionException {
		throw new DdeExecutionException("throwing Business Error");		
	}
	private void processCommand() {
        try {
        	for (final String fileEntry : files) {
        		boolean startReading = false;
        		synchronized (temp) {
        			if(!temp.contains(fileEntry)){
        				temp.add(fileEntry);
                		startReading=true;
                	}
        		}
                	if(startReading){
                		System.out.println(ITaskRun.getThreadName()+" Start. File Archiving  = "+fileEntry);
                		  Thread.sleep(2000);
                		  System.out.println(ITaskRun.getThreadName()+" End. File Archiving  = "+fileEntry);
                	}
            }
          
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
   
	
	
}
