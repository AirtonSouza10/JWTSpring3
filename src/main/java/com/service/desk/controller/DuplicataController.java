package com.service.desk.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
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

import com.service.desk.dto.BaixaParcelaRequestDTO;
import com.service.desk.dto.DuplicataRequestDTO;
import com.service.desk.dto.DuplicataResponseDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.DuplicataService;

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
@RequestMapping(RequestMappingConstants.DUPLICATA_END_POINT)
@Tag(name = "Duplicata", description = "End points de duplicata")
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
	
	@Operation(summary = "Baixa parcela por id")
	@PutMapping("/baixa")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk baixarParcela(@Valid @RequestBody BaixaParcelaRequestDTO dto) {
		duplicataService.baixarParcela(dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }
	
	@Operation(summary = "buscar Parcela por id")
	@GetMapping("/buscar-parcela/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk buscarParcelaPorId(@PathVariable Long id) {
        return new ResponseServiceDesk(duplicataService.buscarParcelaPorId(id));
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
    
    @Operation(summary = "Buscar duplicata dia")
    @GetMapping("/dia")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk obterContasPagarDia() {
        return new ResponseServiceDesk(duplicataService.obterContasPagarDia());
    }
        
    @Operation(summary = "Buscar conta a pagar vencida")
    @GetMapping("/vencida")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk obterContasPagarVencida() {
        return new ResponseServiceDesk(duplicataService.obterContasPagarVencida());
    }
   
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Gera relatório de contas a pagar do dia em PDF")
    @GetMapping("/relatorio-dia")
    public ResponseEntity<byte[]> gerarRelatorioDia() throws JRException, IOException {
        var contasDoDiaVencidas = duplicataService.obterContasPagarDiaAndVencidas();

        JRBeanCollectionDataSource dataSourceDia = new JRBeanCollectionDataSource(contasDoDiaVencidas.getDia());
        JRBeanCollectionDataSource dataSourceVencidas = new JRBeanCollectionDataSource(contasDoDiaVencidas.getVencidos());

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("TITULO_RELATORIO", "Relatório de Contas a Pagar do Dia");
        parametros.put("DATA_EMISSAO", new Date());
        parametros.put("DATASOURCE_DIA", dataSourceDia);
        parametros.put("DATASOURCE_VENCIDAS", dataSourceVencidas);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
            new ClassPathResource("relatorios/ContasPagarDia.jasper").getInputStream()
        );

        adicionarParametroBase(parametros, null);
        // Aqui usamos JREmptyDataSource porque os dados reais virão dos sub-relatórios
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());

        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("relatorio-contas-dia.pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @Operation(summary = "Relatório de contas a pagar em aberto por filial")
    @GetMapping("/relatorio-aberto-filial/{idFilial}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk relatorioContasEmAbertoPorFilial(@PathVariable Long idFilial) {
        return new ResponseServiceDesk(duplicataService.gerarRelatorioContasEmAbertoPorFilial(idFilial));
    }
    
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Gera relatório de contas a pagar em aberto por filial em PDF")
    @GetMapping("/relatorio-contas-pagar-filial/{idFilial}")
    public ResponseEntity<byte[]> gerarRelatorioContasEmAbertoPorFilialPDF(@PathVariable Long idFilial)
            throws JRException, IOException {

        var relatorio = duplicataService.gerarRelatorioContasEmAbertoPorFilial(idFilial);

        JRBeanCollectionDataSource dataSourceMeses = new JRBeanCollectionDataSource(relatorio.getMeses());

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("TITULO_RELATORIO", "Relatório de Contas a Pagar - " + relatorio.getFilial());
        parametros.put("DATA_EMISSAO", new Date());
        parametros.put("TOTAL_GERAL", relatorio.getTotalGeralFormatado());
        parametros.put("DATASOURCE_MESES", dataSourceMeses);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
            new ClassPathResource("relatorios/ContasPagarFilial.jasper").getInputStream()
        );

        adicionarParametroBase(parametros, null);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());

        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("relatorioContasPagarFilial.pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
