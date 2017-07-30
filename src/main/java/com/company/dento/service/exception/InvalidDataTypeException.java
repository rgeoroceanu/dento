package com.company.dento.service.exception;

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class InvalidDataTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidDataTypeException(String message) {
		super(message);
	}

}
