package com.citi.dde.common.util;

public interface DDEConstants {

	
	String MQ_BROKER_URL="activemq.broker.url";
	String JMS = "jms";
	String PATH_CTL_FILES="dde.ach.ctlFilePath";
	/*String GI_SCRUB_PAUSE="GI_SCRUB.waittime";
	String DATA_SYNC_PAUSE="DATA_SYNC.waittime";
	String MSG_SEND_PAUSE ="MSG_SEND.waittime";
	String MSG_RECV_PAUSE="MSG_RECV.waittime";
	String NPCI_SVC_PAUSE="NPCI_SVC.waittime";*/
	String MASTER_PAUSE="NPCI_SVC.waittime";
	String DEFAULT_PAUSE = "default.waittime";
	String WAIT_TIME=".waittime";
	String MASTER = "MASTER"; 
	String TASK1_CLASS="GiRecvTask";
	String TASK2_CLASS="GiScrubTask";
	String TASK3_CLASS="GiSynchTask";
	String MASTER_TASK_CLASS="MasterTask";
	String TASK4_CLASS="NPCIServcTask";
	String TASK5_CLASS="GiSendTask";
	String BEFORE_LOG = "IN :";
	String AFTER_LOG = "OUT :";
	String EMPTY_STRING = "";
	String DEBUG = "D";
	String ERROR = "E";
	String INFO = "I";
	String GI_RECV_TASK="GiRecvTask";
	String GI_SEND_TASK="GiSendTask";
	String GI_SCRUB_TASK="GiScrubTask";
	String GI_SYNCH_TASK="GiSynchTask";
	String NPCI_SVC_TASK="NPCIServcTask";
	String MASTER_TASK="MasterTask";
	String ACTIVE = "Y";
	String DEACTIVE = "N";
	String UNDERSCORE = "_";
	String THREAD_DELIMETER = "-thread-";
	String THREAD_SIZE=".thread.size";
	String IS_RUNNING = "...";
	String NOT_APPLICABLE = "NA";
	Integer DEFAULT_THREAD_COUNT = 1;
	Integer DEFAULT_THREAD_MAX_CORE_SIZE = 1;
}
