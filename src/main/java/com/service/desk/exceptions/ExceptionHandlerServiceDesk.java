package com.service.desk.exceptions;

import com.service.desk.controller.ResponseServiceDesk;
import com.service.desk.enumerator.MensagemEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
@Log4j2
public class ExceptionHandlerServiceDesk extends ResponseEntityExceptionHandler {
    private Locale locale = new Locale("pt","BR");
    private final static String ERRO = "Erro : ";

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = NegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<ResponseServiceDesk> negocioExceptionHandler(NegocioException negocioException) {
        final ResponseServiceDesk response = new ResponseServiceDesk();
        response.getMsgErro().add(messageSource.getMessage(negocioException.getMessage(), negocioException.getArgs(), locale));
        if (negocioException.isPrintLog()) {
            log.error(ERRO, negocioException);
        } else {
            if (negocioException.getCause() instanceof NullPointerException) {
                log.error(ERRO, negocioException);
            } else {
                log.error(negocioException.getMessage());
            }
        }
        return new HttpEntity<>(response);
    }

    @ExceptionHandler(value = AlertaException.class)
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<ResponseServiceDesk> alertaExceptionHandler(AlertaException alertaException) {
        final ResponseServiceDesk response = new ResponseServiceDesk();
        response.getMsgAlerta()
                .add(messageSource.getMessage(alertaException.getMessage(), alertaException.getArgs(), locale));
        log.error(ERRO, alertaException);
        return new HttpEntity<>(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseServiceDesk genericExceptionHandler(Exception exception) {
        log.error("Erro: ", exception);
        ResponseServiceDesk response = new ResponseServiceDesk();
        String mensagem = messageSource.getMessage(MensagemEnum.MSGE001.key, new Object[]{exception.getMessage()}, locale);
        response.getMsgErro().add(mensagem);
        return response;
    }

    @ExceptionHandler(MethodParameterNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodParameterNotValidException ex) {
        final ResponseServiceDesk response = new ResponseServiceDesk();
        response.getMsgErro().add(ex.getMessage());
        log.error(ERRO, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EntidadeNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<ResponseServiceDesk> handleEntidadeNotFoundException(
            EntidadeNotFoundException entidadeNotFoundException) {
        final ResponseServiceDesk response = new ResponseServiceDesk();
        response.getMsgErro().add(messageSource.getMessage(entidadeNotFoundException.getMessage(),
                entidadeNotFoundException.getArgs(), locale));
        log.error(ERRO, entidadeNotFoundException);
        return new HttpEntity<>(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<ResponseServiceDesk> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        final ResponseServiceDesk response = new ResponseServiceDesk();

        String mensagem = messageSource.getMessage(MensagemEnum.MSGE003.getKey(), new Object[]{ex.getMessage()}, locale);
        response.getMsgErro().add(mensagem);

        log.error(ERRO, ex);
        return new HttpEntity<>(response);
    }

    @ExceptionHandler(JpaSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HttpEntity<ResponseServiceDesk> handleJpaSystemException(JpaSystemException ex) {
        final ResponseServiceDesk response = new ResponseServiceDesk();

        String mensagem = messageSource.getMessage(MensagemEnum.MSGE004.getKey(), new Object[]{ex.getMessage()}, locale);
        response.getMsgErro().add(mensagem);

        log.error(ERRO, ex);
        return new HttpEntity<>(response);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HttpEntity<ResponseServiceDesk> handleSQLException(SQLException ex) {
        final ResponseServiceDesk response = new ResponseServiceDesk();

        String mensagem = messageSource.getMessage(MensagemEnum.MSGE005.getKey(), new Object[]{ex.getMessage()}, locale);
        response.getMsgErro().add(mensagem);

        log.error(ERRO, ex);
        return new HttpEntity<>(response);
    }
}
