package org.kwrx.builder.interp;

public class ParsingException extends Exception {

    private final String message;

    public ParsingException(Token token) {
        this.message = String.format("Syntax error: %s: unexpected token: '%s' at line %d, column %d%n", getClass().getSimpleName(), token.getLexeme(), token.getLine(), token.getColumn());
    }

    public ParsingException(Token token, String message) {
        this.message = String.format("Syntax error: %s: unexpected token: '%s' at line %d, column %d: %s%n", getClass().getSimpleName(), token.getLexeme(), token.getLine(), token.getColumn(), message);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
