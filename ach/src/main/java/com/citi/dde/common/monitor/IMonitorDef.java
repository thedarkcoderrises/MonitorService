package com.citi.dde.common.monitor;

import com.citi.dde.common.exception.MonitorException;

public abstract interface IMonitorDef
  extends Runnable
{
  public abstract void init()
    throws MonitorException;
  
  public abstract void execute()
    throws MonitorException;
  
  public abstract void stop()
    throws MonitorException;
}
