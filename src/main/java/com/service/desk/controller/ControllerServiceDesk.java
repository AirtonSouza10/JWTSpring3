package com.service.desk.controller;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.utils.MensagemUtils;

@RestController
@RequestMapping(RequestMappingConstants.HEALTH_ENO_POINT)
public class ControllerServiceDesk {
	@Autowired
	private MensagemUtils mensagemUtils;
	
	protected ResponseServiceDesk responseSucesso(MensagemEnum mensagem, Object resposta) {
		var response = new ResponseServiceDesk();
		response.setMsgSucesso(Arrays.asList(mensagemUtils.getTextoMensagem(mensagem.getKey())));
		if(Objects.nonNull(resposta)) {
			response.setResposta(resposta);
		}
		return response;
	}
	
	protected ResponseServiceDesk responseSucesso(MensagemEnum mensagem) {
		return responseSucesso(mensagem, null);
	}
}