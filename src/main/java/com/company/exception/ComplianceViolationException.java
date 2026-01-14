package com.company.exception;

public class ComplianceViolationException extends RuntimeException {
    public ComplianceViolationException(String message) {
        super(message);
    }
}
