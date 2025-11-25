package com.service.desk.utils;

import com.service.desk.enumerator.MensagemEnum;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component
public class MensagemUtils {
    @Autowired
    private MessageSource messageSource;

    public String getMensagem(final String key, final Object... args){
        final String MSG_NAO_ENCONTARDA = "{0} -> Mensagem não encontrada.";
        try {
            return messageSource.getMessage(key,args, Locale.forLanguageTag("pt-BR"));
            } catch (Exception e) {
            LogManager.getLogger(MensagemEnum.class).warn(MessageFormat.format(MSG_NAO_ENCONTARDA,key));
        }
        return key;
    }

    public String getTextoMensagem(final String key, final Object... args){
        return messageSource.getMessage(key,args,Locale.getDefault());
    }
}
