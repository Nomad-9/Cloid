package com.arpacell.cloid.store;


/**
 * 
 * @author Nomad
 *
 */
public class OperationException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public OperationException() {
		super();
	}

	public OperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperationException(String message) {
		super(message);
	}

	public OperationException(Throwable cause) {
		super(cause);
	}

}
