package com.company.dento.service.exception;

/**
 *
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class CannotGenerateReportException extends Exception {
    private static final long serialVersionUID = 1L;

    public CannotGenerateReportException(String message) {
        super(message);
    }

    public CannotGenerateReportException(String message, Exception source) {
        super(message, source);
    }

}