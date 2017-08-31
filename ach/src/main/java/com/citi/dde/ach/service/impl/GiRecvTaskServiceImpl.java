package com.citi.dde.ach.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.citi.dde.ach.service.GiRecvTaskService;
import com.citi.dde.common.exception.TaskException;

@Service
public class GiRecvTaskServiceImpl implements GiRecvTaskService {

	static boolean firstThread = true;
	
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
	public Integer executeTask() throws TaskException {
		synchronized (this) {
			try{
				if(firstThread){
					throw new NullPointerException();
				}	
			}catch(Exception e){
				firstThread=false;
				throw new TaskException(e.getMessage(), Thread.currentThread().getName(), e);
			}
		}
		
//		processCommand();	
		return 0;
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
                		System.out.println(Thread.currentThread().getName()+" Start. File Archiving  = "+fileEntry);
                		  Thread.sleep(2000);
                		  System.out.println(Thread.currentThread().getName()+" End. File Archiving  = "+fileEntry);
                	}
            }
          
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
   
	
	
}
