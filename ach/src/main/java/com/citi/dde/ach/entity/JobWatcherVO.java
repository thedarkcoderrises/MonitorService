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

public class JobWatcherVO implements Serializable {
	
	/**
	 * generated Serial number
	 */
	private static final long serialVersionUID = -4646822336281063352L;
	
	/**
	 * The job Id
	 */
	private String jobid;
	
	/**
	 * The workflow id
	 */
	private String workflow_id;
	
	/**
	 * The Job Type
	 */
	private String job_type;
	
	/**
	 * The job start time
	 */
	private String start_time;
	
	/**
	 * The Job End Time
	 */
	private String end_time;
	
	/**
	 * The Job Interval
	 */
	private Integer interval;
	
	/**
	 * The job Triger time in case of job type is adhoc
	 */
	private String job_time;
	
	/**
	 * The job Status
	 */
	private String job_status;
	
	/**
	 * The Job Description
	 */
	private String job_desc;
	
	/**
	 * The Run Status
	 */
	private String run_status ;
	
	/**
	 * The Next run time
	 */
	private String next_run_time;

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
	 * @return the workflow_id
	 */
	public String getWorkflow_id() {
		return workflow_id;
	}

	/**
	 * @param workflow_id the workflow_id to set
	 */
	public void setWorkflow_id(String workflow_id) {
		this.workflow_id = workflow_id;
	}

	/**
	 * @return the job_type
	 */
	public String getJob_type() {
		return job_type;
	}

	/**
	 * @param job_type the job_type to set
	 */
	public void setJob_type(String job_type) {
		this.job_type = job_type;
	}

	/**
	 * @return the start_time
	 */
	public String getStart_time() {
		return start_time;
	}

	/**
	 * @param start_time the start_time to set
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	/**
	 * @return the interval
	 */
	public Integer getInterval() {
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	/**
	 * @return the job_time
	 */
	public String getJob_time() {
		return job_time;
	}

	/**
	 * @param job_time the job_time to set
	 */
	public void setJob_time(String job_time) {
		this.job_time = job_time;
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

	/**
	 * @return the next_run_time
	 */
	public String getNext_run_time() {
		return next_run_time;
	}

	/**
	 * @param next_run_time the next_run_time to set
	 */
	public void setNext_run_time(String next_run_time) {
		this.next_run_time = next_run_time;
	}

	 
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JobWatcherVO [jobid=");
		builder.append(jobid);
		builder.append(", workflow_id=");
		builder.append(workflow_id);
		builder.append(", job_type=");
		builder.append(job_type);
		builder.append(", start_time=");
		builder.append(start_time);
		builder.append(", end_time=");
		builder.append(end_time);
		builder.append(", interval=");
		builder.append(interval);
		builder.append(", job_time=");
		builder.append(job_time);
		builder.append(", job_status=");
		builder.append(job_status);
		builder.append(", job_desc=");
		builder.append(job_desc);
		builder.append(", run_status=");
		builder.append(run_status);
		builder.append(", next_run_time=");
		builder.append(next_run_time);
		builder.append("]");
		return builder.toString();
	}
	
	
	 
	

}
