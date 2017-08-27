package com.citi.dde.common.exception;




public class TaskException  extends CommonException
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 6115442270060648870L;



public TaskException(String message, Throwable cause)
  {
    super(message, cause);
  }
  


  public TaskException(String message)
  {
    super(message);
  }
  


  public TaskException(Throwable cause)
  {
    super(cause);
  }



public TaskException(String message, String name, Exception e) {
	super(message,name,e);
}

}
