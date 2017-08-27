package com.citi.dde.common.monitor;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.citi.dde.common.exception.MonitorException;
import com.citi.dde.common.exception.PauseException;







public abstract class AbstractMonitor
  implements IMonitorDef
{
  private static final int MILLI_FACTOR = 1000;
  private final Logger log = LogManager.getLogger(AbstractMonitor.class);
  private IMonitorConfig config;
  
  public AbstractMonitor(IMonitorConfig config) {
    this.config = config;
  }
  
  public abstract String getName();
  
  public abstract void load(IMonitorConfig paramIMonitorConfig) throws MonitorException;
  
  public void pause(int argRefreshtime) throws PauseException
  {
    try {
      log.debug("Waiting for " + argRefreshtime);
      Thread.sleep(argRefreshtime * 1000);
    } catch (InterruptedException e) {
      throw new PauseException(e);
    }
  }
  
  public void setConfig(IMonitorConfig config)
  {
    this.config = config;
  }
  
  public IMonitorConfig getConfig() {
    return config;
  }
  


  public void load()
    throws MonitorException
  {
    load(config);
  }
}
