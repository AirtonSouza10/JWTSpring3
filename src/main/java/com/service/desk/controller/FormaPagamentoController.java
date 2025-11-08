package com.service.desk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.service.desk.dto.FormaPagamentoRequestDTO;
import com.service.desk.dto.FormaPagamentoResponseDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.FormaPagamentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.FORMA_PAGAMENTO_END_POINT)
@Tag(name = "Formas de pagamento", description = "End points de Formas de pagamento")
public class FormaPagamentoController extends ControllerServiceDesk{

	@Autowired
    private FormaPagamentoService formaPagamentoService;

	@Operation(summary = "Retorna a lista de formas de pagamento")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarFormasPagamento() {
		return new ResponseServiceDesk(formaPagamentoService.listarFormasPagamento());
    }
	
	@Operation(summary = "Listar formas de pagamento paginadas")
	@GetMapping("/paginados")
	@ResponseStatus(HttpStatus.OK)
	public ResponseServiceDesk listarFormasPagamentoPaginadas(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Page<FormaPagamentoResponseDTO> pagina = formaPagamentoService.listarFormasPagamentoPaginadas(page, size);
	    return new ResponseServiceDesk(pagina);
	}
	
    @Operation(summary = "Retorna uma forma de pagamento por ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk buscarPorId(@PathVariable Long id) {
        return new ResponseServiceDesk(formaPagamentoService.buscarPorId(id));
    }

	@Operation(summary = "Salvar nova forma de pagmaneto")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseServiceDesk salvarFormaPagamento(@Valid @RequestBody FormaPagamentoRequestDTO dto) {
		formaPagamentoService.salvarFormaPagamento(dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Atualiza dados da forma de pagamento")
	@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk atualizarFormaPagamento(@PathVariable Long id , @Valid @RequestBody FormaPagamentoRequestDTO dto) {
		formaPagamentoService.atualizarFormaPagamento(id,dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }
	
    @Operation(summary = "Remove uma forma de pagamento pelo ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseServiceDesk deletarFormaPagamento(@PathVariable Long id) {
        formaPagamentoService.deletarFormaPagamento(id);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS003));
    }
}
