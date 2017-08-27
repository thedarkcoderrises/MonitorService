package com.citi.dde.ach.task;

import java.io.FileNotFoundException;
import java.util.concurrent.Callable;

import com.citi.dde.common.exception.TaskException;

public abstract interface ITaskDef<T>
  extends Callable<T>
{
  public abstract void preInit()
    throws TaskException, FileNotFoundException;
  
  public abstract void init()
    throws TaskException;
  
  public abstract void preStart()
    throws TaskException;
  
  public abstract void start()
    throws TaskException;
  
  public abstract T finish()
    throws TaskException;
  
  public abstract boolean isRunning();
  
  public abstract T process()
		    throws TaskException;
}
