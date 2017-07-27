package com.company.dento.service.exception;

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class DataDoesNotExistException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public DataDoesNotExistException(String message) {
		super(message);
	}

}
