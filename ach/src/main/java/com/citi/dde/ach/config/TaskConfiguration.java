package com.citi.dde.ach.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.citi.dde.ach.task.impl.GiRecvTask;
import com.citi.dde.ach.task.impl.GiScrubTask;
import com.citi.dde.ach.task.impl.GiSendTask;
import com.citi.dde.ach.task.impl.GiSynchTask;
import com.citi.dde.ach.task.impl.MasterTask;
import com.citi.dde.ach.task.impl.NPCIServcTask;

@Configuration
public class TaskConfiguration {

	
@Bean("MSG_RECV")
@Scope(scopeName="prototype",proxyMode=ScopedProxyMode.TARGET_CLASS)
public GiRecvTask giRecvTask(){
	return new GiRecvTask();
}

@Bean("GI_SCRUB")
@Scope(scopeName="prototype",proxyMode=ScopedProxyMode.TARGET_CLASS)
public GiScrubTask giScrubTask(){
	return new GiScrubTask();
}

@Bean("MSG_SEND")
@Scope(scopeName="prototype",proxyMode=ScopedProxyMode.TARGET_CLASS)
public GiSendTask giSendTask(){
	return new GiSendTask();
}

@Bean("DATA_SYNC")
@Scope(scopeName="prototype",proxyMode=ScopedProxyMode.TARGET_CLASS)
public GiSynchTask giSynchTask(){
	return new GiSynchTask();
}

@Bean("NPCI_SVC")
@Scope(scopeName="prototype",proxyMode=ScopedProxyMode.TARGET_CLASS)
public NPCIServcTask npciServcTask(){
	return new NPCIServcTask();
}
	
@Bean("MASTER")
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public MasterTask masterTask(){
	return new MasterTask();
}

}
