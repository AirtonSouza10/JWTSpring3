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

import com.service.desk.dto.TipoPagamentoRequestDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.TipoPagamentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.TIPO_PAGAMENTO_END_POINT)
@Tag(name = "Tipo de pagamento", description = "End points de tipos de pagamento")
public class TipoPagamentoController extends ControllerServiceDesk{

	@Autowired
    private TipoPagamentoService tipoPagamentoService;

	@Operation(summary = "Retorna a lista de tipos de pagamento")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarTickets() {
		return new ResponseServiceDesk(tipoPagamentoService.listarTiposPagamento());
    }

	@Operation(summary = "Salvar novo tipo de pagamento")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseServiceDesk salvarTipoPagamento(@Valid @RequestBody TipoPagamentoRequestDTO dto) {
		tipoPagamentoService.salvarTipoPagamento(dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Atualiza dados do tipo de pagamento")
	@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk atualizarTipoPagamento(@PathVariable Long id , @Valid @RequestBody TipoPagamentoRequestDTO dto) {
		tipoPagamentoService.atualizarTipoPagamento(id,dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }
}
