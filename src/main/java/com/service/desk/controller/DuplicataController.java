package com.service.desk.controller;

import java.util.List;

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

import com.service.desk.dto.DuplicataRequestDTO;
import com.service.desk.dto.DuplicataResponseDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.DuplicataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.DUPLICATA_END_POINT)
@Tag(name = "nota Fiscal", description = "End points de nota fiscal")
public class DuplicataController extends ControllerServiceDesk{

	@Autowired
    private DuplicataService duplicataService;

	@Operation(summary = "Retorna a lista de notas Fiscais")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarNotas() {
		return new ResponseServiceDesk(duplicataService.listarDuplicatas());
    }

    @Operation(summary = "Listar todas duplicatas paginadas")
    @GetMapping("/paginadas")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarDuplicatasPaginadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<DuplicataResponseDTO> pagina = duplicataService.listarDuplicatasPaginadas(page, size);
        return new ResponseServiceDesk(pagina);
    }

    @Operation(summary = "Buscar duplicatas por número/descrição com paginação")
    @GetMapping("/por-numero")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk buscarDuplicatasPorNumero(
            @RequestParam String numero,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanho) {
        Page<DuplicataResponseDTO> page = duplicataService.buscarDuplicatasPorNumeroPaginadas(numero, pagina, tamanho);
        return new ResponseServiceDesk(page);
    }

	@Operation(summary = "Salvar nova Duplicata")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseServiceDesk salvarNota(@Valid @RequestBody DuplicataRequestDTO dto) {
		duplicataService.salvarDuplicata(dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Atualiza dados da Duplicata")
	@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk atualizarNota(@PathVariable Long id , @Valid @RequestBody DuplicataRequestDTO dto) {
		duplicataService.atualizarDuplicata(id,dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }
	
    @Operation(summary = "Exclui uma nota fiscal pelo ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirNota(@PathVariable Long id) {
        duplicataService.excluirDuplicata(id);
    }
    
    @Operation(summary = "Buscar duplicata por ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk buscarPorId(@PathVariable Long id) {
        return new ResponseServiceDesk(duplicataService.buscarDuplicataPorId(id));
    }
    
    @Operation(summary = "Buscar duplicata por descrição (sem paginação)")
    @GetMapping("/descricao")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk buscarPorDescricaoSemPaginacao(@RequestParam String descricao) {
        List<DuplicataResponseDTO> duplicatas = duplicataService.buscarDuplicataPorDescricao(descricao);
        return new ResponseServiceDesk(duplicatas);
    }
}
