package com.service.desk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.service.desk.dto.TipoRequestDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.TipoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.TIPO_END_POINT)
@Tag(name = "Tipo de Conta a pagar", description = "End points de tipos de conta a pagar")
public class TipoController extends ControllerServiceDesk{

	@Autowired
    private TipoService tipo;

	@Operation(summary = "Retorna a lista de tipos de pagamento")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarTiposNota() {
		return new ResponseServiceDesk(tipo.listarTipos());
    }

	@Operation(summary = "Salvar novo tipo de nota")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseServiceDesk salvarTipoNota(@Valid @RequestBody TipoRequestDTO dto) {
		tipo.salvarTipo(dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Atualiza dados do tipo de nota")
	@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk atualizarTipoNota(@PathVariable Long id , @Valid @RequestBody TipoRequestDTO dto) {
		tipo.atualizarTipo(id,dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }
	
    @Operation(summary = "Remove um tipo de nota pelo ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk deletarTipoNota(@PathVariable Long id) {
        tipo.deletarTipo(id);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS003));
    }
    
    @Operation(summary = "Busca tipo de nota pelo ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk buscarTipoPorId(@PathVariable Long id) {
        var tipoDuplicata = tipo.buscarPorId(id);
        return new ResponseServiceDesk(tipoDuplicata);
    }
}
