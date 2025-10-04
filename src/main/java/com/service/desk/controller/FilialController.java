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

import com.service.desk.dto.FilialRequestDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.FilialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.FILIAL_END_POINT)
@Tag(name = "Filial", description = "End points de Filial")
public class FilialController extends ControllerServiceDesk{

	@Autowired
    private FilialService filialService;

	@Operation(summary = "Retorna a lista de filiais")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarFiliais() {
		return new ResponseServiceDesk(filialService.listarFiliais());
    }
	
	@Operation(summary = "Retorna uma filial pelo ID")
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseServiceDesk buscarFilialPorId(@PathVariable Long id) {
	    return new ResponseServiceDesk(filialService.buscarPorId(id));
	}

	@Operation(summary = "Salvar nova filial")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseServiceDesk salvarFilial(@Valid @RequestBody FilialRequestDTO dto) {
		filialService.salvarFilial(dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Atualiza dados da filial")
	@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk atualizarFilial(@PathVariable Long id , @Valid @RequestBody FilialRequestDTO dto) {
		filialService.atualizarFilial(id,dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Atualiza status (ativo/inativo) da filial")
	@PutMapping("/{id}/status")
	@ResponseStatus(HttpStatus.OK)
	public ResponseServiceDesk atualizarStatusFilial(@PathVariable Long id, @RequestBody Boolean ativo) {
	    filialService.atualizarStatus(id, ativo);
	    return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
	}
}
