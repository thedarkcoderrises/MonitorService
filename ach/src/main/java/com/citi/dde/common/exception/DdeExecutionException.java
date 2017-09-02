package com.citi.dde.common.exception;

public class DdeExecutionException extends CommonException{

	


/**
	 * 
	 */
	private static final long serialVersionUID = 6890975214170814612L;



public DdeExecutionException(String message, Throwable cause)
  {
    super(message, cause);
  }
  


  public DdeExecutionException(String message)
  {
    super(message);
  }
  


  public DdeExecutionException(Throwable cause)
  {
    super(cause);
  }



public DdeExecutionException(String message, String name, Exception e) {
	super(message,name,e);
}

}
