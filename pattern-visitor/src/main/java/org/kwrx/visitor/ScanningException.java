package org.kwrx.visitor;

public class ScanningException extends Exception {

    private final String message;

    public ScanningException(int line, int column, String message) {
        this.message = String.format("Syntax error: %s: %s at line %d, column %d.%n", getClass().getSimpleName(), message, line, column);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
