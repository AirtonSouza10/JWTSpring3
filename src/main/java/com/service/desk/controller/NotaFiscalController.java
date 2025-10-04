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

import com.service.desk.dto.NotaFiscalRequestDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.NotaFiscalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.NOTA_FISCAL_END_POINT)
@Tag(name = "nota Fiscal", description = "End points de nota fiscal")
public class NotaFiscalController extends ControllerServiceDesk{

	@Autowired
    private NotaFiscalService notaFiscalService;

	@Operation(summary = "Retorna a lista de notas Fiscais")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarNotas() {
		return new ResponseServiceDesk(notaFiscalService.listarNotasFiscais());
    }

	@Operation(summary = "Salvar nova nota")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseServiceDesk salvarNota(@Valid @RequestBody NotaFiscalRequestDTO dto) {
		notaFiscalService.salvarNotaFiscal(dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Atualiza dados da nota")
	@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk atualizarNota(@PathVariable Long id , @Valid @RequestBody NotaFiscalRequestDTO dto) {
		notaFiscalService.atualizarNotaFiscal(id,dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }
}
