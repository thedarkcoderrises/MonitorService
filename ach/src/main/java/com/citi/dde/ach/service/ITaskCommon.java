package com.citi.dde.ach.service;

import com.citi.dde.common.exception.TaskException;

public interface ITaskCommon<T> {

	public T executeTask() throws TaskException;
}
