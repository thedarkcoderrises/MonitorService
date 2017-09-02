package com.citi.dde.common.monitor;

import java.util.Collection;

import com.citi.dde.common.util.CountryCode;
import com.citi.dde.common.util.Strategy;

public interface IMonitorConfig
{
  public abstract CountryCode getCountryCode();
  public abstract Collection<Strategy> getStrategyType();
  
}
