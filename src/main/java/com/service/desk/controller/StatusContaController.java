package com.service.desk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.service.desk.dto.StatusContaRequestDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.StatusContaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.STATUS_CONTA_END_POINT)
@Tag(name = "Situação das contas", description = "End points de situacao das contas")
public class StatusContaController extends ControllerServiceDesk{

	@Autowired
    private StatusContaService statusService;

	@Operation(summary = "Retorna a lista de Situações")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarSituacoesConta() {
		return new ResponseServiceDesk(statusService.listarSituacoesConta());
    }

	@Operation(summary = "Salvar nova situação")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseServiceDesk salvarSituacao(@Valid @RequestBody StatusContaRequestDTO dto) {
		statusService.salvarStatus(dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Atualiza dados da situação")
	@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk atualizarFormaPagamento(@PathVariable Long id , @Valid @RequestBody StatusContaRequestDTO dto) {
		statusService.atualizarStatus(id,dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }
}
