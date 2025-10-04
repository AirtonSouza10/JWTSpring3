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

import com.service.desk.dto.FornecedorRequestDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.FornecedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.FORNECEDOR_END_POINT)
@Tag(name = "Fornecedor", description = "End points de Fornecedor")
public class FornecedorController extends ControllerServiceDesk{

	@Autowired
    private FornecedorService fornecedorService;

	@Operation(summary = "Retorna a lista de fornecedores")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarTickets() {
		return new ResponseServiceDesk(fornecedorService.listarFornecdores());
    }

	@Operation(summary = "Salvar novo fornecedor")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseServiceDesk salvarFornecedor(@Valid @RequestBody FornecedorRequestDTO dto) {
		fornecedorService.salvarFornecedores(dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Atualiza dados do fornecedor")
	@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk atualizarFornecedor(@PathVariable Long id , @Valid @RequestBody FornecedorRequestDTO dto) {
		fornecedorService.atualizarFornecedores(id,dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Retorna um fornecedor pelo ID")
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseServiceDesk buscarFornecedorPorId(@PathVariable Long id) {
	    return new ResponseServiceDesk(fornecedorService.buscarPorId(id));
	}

	@Operation(summary = "Atualiza status (ativo/inativo) do fornecedor")
	@PutMapping("/{id}/status")
	@ResponseStatus(HttpStatus.OK)
	public ResponseServiceDesk atualizarStatusFornecedor(@PathVariable Long id, @RequestBody Boolean ativo) {
	    fornecedorService.atualizarStatus(id, ativo);
	    return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
	}
}
