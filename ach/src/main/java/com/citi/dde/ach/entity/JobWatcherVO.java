/*#######################################################################################################
# Country               :       India                                                                   #
# FileName              :       JobWatcherVO.java                                                       #
# Author                :       Abhijeet Krishnamurthy                                                  #
# Created On            :       27 Aug 2017                                                             #
#                                                                                                       #
# Program Details       :                                                                               #
# Input Parameters      :       None                                                                    #
# Output Values         :       None                                                                    #
# Invoked From          :                                                                               #
# Log Files Generated   :                                                   							#
# Output Files Generated:                                                                               #
# Reports Generated     :       None                                                                    #
#                                                                                                       #
# Modification History                                                                                  #
# S.No        Modified By             Date            Description                                       #
#########################################################################################################*/
package com.citi.dde.ach.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="JB_TM_MONITOR_CONFIG")
public class JobWatcherVO implements Serializable {
	
	/**
	 * generated Serial number
	 */
	private static final long serialVersionUID = -4646822336281063352L;
	
	/**
	 * The job Id
	 */
	@Id
	@Column(name="JOB_ID")
	private String jobid;
	
	/**
	 * The job start time
	 */
	@Column(name="START_TIME")
	private Integer start_time;
	
	/**
	 * The Job End Time
	 */
	@Column(name="END_TIME")
	private Integer end_time;
	
	/**
	 * The job Status
	 */
	@Column(name="JOB_STATUS")
	private String job_status;
	
	/**
	 * The Job Description
	 */
	@Column(name="JOB_DESC")
	private String job_desc;
	
	/**
	 * The Run Status
	 */
	@Column(name="RUN_STATUS")
	private String run_status ;
	
	/**
	 * The threadCount
	 */
	@Column(name="THREAD_CNT")
	private Integer threadCount;
	
	/**
	 * @return the jobid
	 */
	public String getJobid() {
		return jobid;
	}

	/**
	 * @param jobid the jobid to set
	 */
	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	/**
	 * @return the start_time
	 */
	public Integer getStart_time() {
		return start_time;
	}

	/**
	 * @param start_time the start_time to set
	 */
	public void setStart_time(Integer start_time) {
		this.start_time = start_time;
	}

	/**
	 * @return the end_time
	 */
	public Integer getEnd_time() {
		return end_time;
	}

	/**
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(Integer end_time) {
		this.end_time = end_time;
	}


	/**
	 * @return the job_status
	 */
	public String getJob_status() {
		return job_status;
	}

	/**
	 * @param job_status the job_status to set
	 */
	public void setJob_status(String job_status) {
		this.job_status = job_status;
	}

	/**
	 * @return the job_desc
	 */
	public String getJob_desc() {
		return job_desc;
	}

	/**
	 * @param job_desc the job_desc to set
	 */
	public void setJob_desc(String job_desc) {
		this.job_desc = job_desc;
	}

	/**
	 * @return the run_status
	 */
	public String getRun_status() {
		return run_status;
	}

	/**
	 * @param run_status the run_status to set
	 */
	public void setRun_status(String run_status) {
		this.run_status = run_status;
	}



	public Integer getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(Integer threadCount) {
		this.threadCount = threadCount;
	}


}
