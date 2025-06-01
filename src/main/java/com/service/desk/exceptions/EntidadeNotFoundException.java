package com.service.desk.exceptions;

import lombok.Getter;

@Getter
public class EntidadeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1883432232831525020L;

    private Object[] args;
    private String codigo;
    private String mensagem;

    public EntidadeNotFoundException(String key, Object... args) {
        super(key);
        this.codigo = key;
        this.mensagem = key;
        this.args = args;
    }

    public EntidadeNotFoundException(String key, Throwable cause) {
        super(key, cause);
    }
}
