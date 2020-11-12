package org.kwrx.builder.interp;

public class ParsingException extends RuntimeException {

    public ParsingException(Token token) {
        System.err.printf("Syntax error: %s: Unexpected token: '%s' at line %d, column %d%n", getClass().getSimpleName(), token.getLexeme(), token.getLine(), token.getColumn());
    }

    public ParsingException(Token token, String message) {
        System.err.printf("Syntax error: %s: Unexpected token: '%s' at line %d, column %d: %s%n", getClass().getSimpleName(), token.getLexeme(), token.getLine(), token.getColumn(), message);
    }

}
