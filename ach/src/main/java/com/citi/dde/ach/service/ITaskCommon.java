package com.citi.dde.ach.service;

public interface ITaskCommon<T> {

	public T executeTask(String threadName) throws Exception;
}
