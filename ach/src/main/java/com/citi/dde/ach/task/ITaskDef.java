package com.citi.dde.ach.task;

import java.util.concurrent.Callable;

public abstract interface ITaskDef<T>
  extends Callable<T>
{
	public abstract void init()
    throws Exception;
  
  public abstract T process();
}
