package com.company.dento.service.exception;

/**
 *
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class TooManyResultsException extends Exception {
    private static final long serialVersionUID = 1L;

    public TooManyResultsException(String message) {
        super(message);
    }

    public TooManyResultsException(String message, Exception source) {
        super(message, source);
    }

}