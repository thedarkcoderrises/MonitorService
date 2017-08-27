package com.citi.dde.ach.config;

import java.util.Collection;

import com.citi.dde.common.monitor.IMonitorConfig;
import com.citi.dde.common.util.CountryCode;
import com.citi.dde.common.util.Strategy;

public class MQMonitorConfig implements IMonitorConfig {

	@Override
	public CountryCode getCountryCode() {
		return CountryCode.getCountry(System.getProperty("country"));
	}

	@Override
	public Collection<Strategy> getStrategyType() {
		return Strategy.getStrategy(System.getProperty("app"));
	}

	@Override
	public String[] getTypeIDs() {
		return null;
	}

	@Override
	public String getSpecialIndicator() {
		return null;
	}
}
