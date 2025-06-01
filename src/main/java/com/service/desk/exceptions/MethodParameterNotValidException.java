package com.service.desk.exceptions;

public class MethodParameterNotValidException extends RuntimeException {

    private static final long serialVersionUID = 9029527460918997276L;

    public MethodParameterNotValidException(String message) {
        super(message);
    }
}
