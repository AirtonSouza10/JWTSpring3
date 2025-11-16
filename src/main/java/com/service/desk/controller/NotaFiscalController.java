package com.service.desk.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.service.desk.dto.NotaFiscalRequestDTO;
import com.service.desk.dto.NotaFiscalResponseDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.NotaFiscalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

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
	
	@Operation(summary = "Retorna a lista de notas Fiscais paginadas")
	@GetMapping("/por-numero-fornecedor")
    @ResponseStatus(HttpStatus.OK)
	public ResponseServiceDesk listarNotas(
	        @RequestParam(required = false) String numero,
	        @RequestParam(required = false) Long fornecedorId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size) {

	    Pageable pageable = PageRequest.of(page, size);
	    Page<NotaFiscalResponseDTO> notas = notaFiscalService.listarNotasFiscaisByNumeroAndFornecedor(numero, fornecedorId, pageable);

	    return new ResponseServiceDesk(notas);
	}

	@Operation(summary = "Retorna a lista de notas Fiscais paginadas")
	@GetMapping("/paginadas")
	@ResponseStatus(HttpStatus.OK)
	public ResponseServiceDesk listarNotas(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size) {

	    Pageable pageable = PageRequest.of(page, size);
	    Page<NotaFiscalResponseDTO> notas = notaFiscalService.listarNotasFiscaisWithPaginacao(pageable);

	    return new ResponseServiceDesk(notas);
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
	
    @Operation(summary = "Exclui uma nota fiscal pelo ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirNota(@PathVariable Long id) {
        notaFiscalService.excluirNotaFiscal(id);
    }
    
    @Operation(summary = "Buscar nota fiscal por ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk buscarPorId(@PathVariable Long id) {
        return new ResponseServiceDesk(notaFiscalService.buscarNotaFiscalPorId(id));
    }
    
    @Operation(summary = "Buscar nota fiscal pelo número e fornecedor")
    @GetMapping("/buscar")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk buscarPorNumeroEFornecedor(
            @RequestParam String numero,
            @RequestParam Long fornecedorId) {

        return new ResponseServiceDesk(
                notaFiscalService.buscarPorNumeroEFornecedor(numero, fornecedorId)
        );
    }
    
    @Operation(summary = "Lista notas fiscais por filial e período")
    @GetMapping("/por-filial-periodo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarNotasPorFilialEPeriodo(
            @RequestParam(required = false) Long idFilial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        return new ResponseServiceDesk(
                notaFiscalService.listarRelatorioFilialPeriodo(idFilial, dataInicial, dataFinal)
        );
    }
    
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Gera relatório de protocolos de notas fiscais por filial e período em PDF")
    @GetMapping("/relatorio-notas-filial-periodo")
    public ResponseEntity<byte[]> gerarRelatorioNotasFilialPeriodoPDF(
            @RequestParam(required = false) Long idFilial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) throws JRException, IOException {

       	var dtoProtocolo = notaFiscalService.listarRelatorioFilialPeriodo(idFilial, dataInicial, dataFinal);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dtoProtocolo.getNotaFiscal());

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("TITULO_RELATORIO", "PROTOCOLO DE ENVIO PARA CONTABILIDADE");
        parametros.put("DATA_EMISSAO", new Date());
        parametros.put("DATA_INICIAL", dataInicial);
        parametros.put("DATA_FINAL", dataFinal);
        parametros.put("FILIAL", dtoProtocolo.getFilial().getNome());
        parametros.put("FILIAL_ID", dtoProtocolo.getFilial().getIdentificacao());
        parametros.put("DATASET_NOTAS", dataSource);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
                new ClassPathResource("relatorios/ProtocoloNotas.jasper").getInputStream()
        );

        adicionarParametroBase(parametros, null);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());

        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("relatorioProtocoloNotas.pdf")
                .build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

}
