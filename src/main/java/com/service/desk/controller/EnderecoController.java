package com.service.desk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.service.desk.service.service.EnderecoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.ENDERECO_END_POINT)
@Tag(name = "Endereços", description = "End points de endereços")
public class EnderecoController extends ControllerServiceDesk{
	
	@Autowired
    private EnderecoService enderecoService;

	@Operation(summary = "Retorna a lista de tipos de endereço")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarTiposEndereco() {
		return new ResponseServiceDesk(enderecoService.listarTiposEndereco());
    }
	
    @Operation(summary = "Consulta endereço pelo CEP via ViaCEP")
    @GetMapping("/cep/{cep}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk buscarPorCep(@PathVariable String cep) {
        var endereco = enderecoService.buscarPorCep(cep);
        return new ResponseServiceDesk(endereco);
    }
}
