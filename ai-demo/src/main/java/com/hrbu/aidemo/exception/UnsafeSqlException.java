package com.hrbu.aidemo.exception;

/**
 * @author Say my name
 */
public class UnsafeSqlException extends RuntimeException {
    public UnsafeSqlException(String message) {
        super(message);
    }
}