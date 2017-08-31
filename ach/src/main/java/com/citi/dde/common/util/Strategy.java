package com.citi.dde.common.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public enum Strategy
{
  GI_SCRUB(DDEConstants.GI_SCRUB_TASK), 
  MSG_SEND(DDEConstants.GI_SEND_TASK),
  MSG_RECV(DDEConstants.GI_RECV_TASK), 
  DATA_SYNC(DDEConstants.GI_SYNCH_TASK), 
  NPCI_SVC(DDEConstants.NPCI_SVC_TASK), 
  MASTER("ALL");
  
  private String strategy;
  
  private static Map<String,Strategy> strategyMap = new HashMap<>(values().length, 1);;
  
  private Strategy(String strategyCode)
  {
    strategy = strategyCode;
  }
  
  static {
	    for (Strategy strategy : values()) strategyMap.put(strategy.getStrategy(), strategy);
	  }

  public String getStrategy()
  {
    return strategy;
  }
  

  public Strategy[] getAllStratergies(){
	 return Strategy.values();
  }
  



  public static Strategy getStrategy(String strategyCode)
  {
	  return strategyMap.get(strategyCode);
  }

  public static Collection<Strategy> getStrategies(String strategyCode)
  {
    if (strategyCode == null) {
      return Arrays.asList(values());
    }
    if (strategyCode.contains(",")) {
      String[] codes = strategyCode.split(",");
      Collection<Strategy> strategies = new LinkedList();
      for (String code : codes) {
        Strategy strategy = valueOf(code.toUpperCase());
        strategies.add(strategy);
      }
      return strategies;
    }
    try {
      Strategy strategy = valueOf(strategyCode.toUpperCase());
      return Arrays.asList(new Strategy[] { strategy });
    } catch (IllegalArgumentException e) {}
    return Arrays.asList(values());
  }
}
