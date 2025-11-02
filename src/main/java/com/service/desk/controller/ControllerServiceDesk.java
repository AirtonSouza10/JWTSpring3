package com.service.desk.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.utils.MensagemUtils;

import net.sf.jasperreports.engine.JRDataSource;

@RestController
@RequestMapping(RequestMappingConstants.HEALTH_ENO_POINT)
public class ControllerServiceDesk {
	private static final String PATH_SUBREPORT = "relatorios/subreport/";
	
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
	
	protected void adicionarParametroBase(Map<String, Object> parameters, JRDataSource dataSource) throws IOException {
		parameters.put("SUBREPORT_PATH", PATH_SUBREPORT);
		parameters.put("datasource", dataSource);
	}
}