package org.kwrx.builder.interp;

public class ScanningException extends Exception {
    public ScanningException(int line, int column, String message) {
        System.err.printf("Syntax error: %s: %s at line %d, column %d.%n", getClass().getSimpleName(), message, line, column);
    }
}
