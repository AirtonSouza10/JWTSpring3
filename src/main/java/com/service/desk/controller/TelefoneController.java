package com.service.desk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.service.desk.service.service.TelefoneService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.TELEFONE_END_POINT)
@Tag(name = "Telefones", description = "End points de telefones")
public class TelefoneController extends ControllerServiceDesk{

	@Autowired
    private TelefoneService telefoneService;

	@Operation(summary = "Retorna a lista de formas de pagamento")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarTiposRelefone() {
		return new ResponseServiceDesk(telefoneService.listarTiposTelefone());
    }
}
