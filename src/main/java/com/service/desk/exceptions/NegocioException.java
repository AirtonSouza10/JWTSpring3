package com.service.desk.exceptions;

import lombok.Getter;
import org.apache.el.parser.BooleanNode;

@Getter
public class NegocioException extends RuntimeException{
    private Object[] args;
    private String codigo;
    private String mensgem;
    private boolean printLog;

    public NegocioException(String key, Object... args) {
        super(key);
        this.args = args;
        this.codigo = key;
        this.mensgem = key;
        this.printLog = true;
    }

    public NegocioException(String key, Boolean printLog, String... args) {
        super(key);
        this.args = args;
        this.codigo = key;
        this.mensgem = key;
        this.printLog = printLog;
    }

    public NegocioException(String key, Throwable cause) {
        super(key,cause);
        this.printLog = true;
    }
}
