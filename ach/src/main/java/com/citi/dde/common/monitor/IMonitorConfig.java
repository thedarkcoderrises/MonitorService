package com.citi.dde.common.monitor;

import java.util.Collection;

import com.citi.dde.common.util.CountryCode;
import com.citi.dde.common.util.Strategy;

public interface IMonitorConfig
{
  public static final String TRANTYPE = "trantype";
  public static final String FILETYPEID = "filetypeid";
  public static final String APP = "app";
  public static final String COUNTRY = "country";
  public static final String SPLIND = "splind";
  
  public abstract CountryCode getCountryCode();
  
  public abstract Collection<Strategy> getStrategyType();
  
  public abstract String[] getTypeIDs();
  
  public abstract String getSpecialIndicator();
  
}
