package com.service.desk.service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.service.desk.dto.BaixaParcelaRequestDTO;
import com.service.desk.dto.DuplicataBuscaGeralDTO;
import com.service.desk.dto.DuplicataDiaResponseDTO;
import com.service.desk.dto.DuplicataDiaVencidoResponseDTO;
import com.service.desk.dto.DuplicataRequestDTO;
import com.service.desk.dto.DuplicataResponseDTO;
import com.service.desk.dto.FiltroRelatorioCustomizadoDTO;
import com.service.desk.dto.ParcelaBuscaGeralDTO;
import com.service.desk.dto.ParcelaResponseDTO;
import com.service.desk.dto.RelatorioContasAbertasResponseDTO;
import com.service.desk.dto.RelatorioCustomizadoResponseDTO;
import com.service.desk.dto.RelatorioParcelasPagasPorTipoDTO;

public interface DuplicataService {

	List<DuplicataResponseDTO> listarDuplicatas();

	DuplicataResponseDTO buscarDuplicataPorId(Long id);

	void salvarDuplicata(DuplicataRequestDTO dto);

	void atualizarDuplicata(Long id, DuplicataRequestDTO dto);

	void excluirDuplicata(Long id);

	Page<DuplicataResponseDTO> listarDuplicatasPaginadas(int pagina, int tamanho);

	Page<DuplicataResponseDTO> buscarDuplicatasPorNumeroPaginadas(String numero, int pagina, int tamanho);

	List<DuplicataResponseDTO> buscarDuplicataPorDescricao(String descricao);

	List<DuplicataDiaResponseDTO> obterContasPagarDia();

	List<DuplicataDiaResponseDTO> obterContasPagarVencida();

	DuplicataDiaVencidoResponseDTO obterContasPagarDiaAndVencidas();

	RelatorioContasAbertasResponseDTO gerarRelatorioContasEmAbertoPorFilial(Long idFilial);

	void baixarParcela(BaixaParcelaRequestDTO dto);

	ParcelaResponseDTO buscarParcelaPorId(Long id);

	List<RelatorioParcelasPagasPorTipoDTO> gerarRelatorioParcelasPagasPorTipo(Long idFilial, LocalDate dataInicial,
			LocalDate dataFinal);

	List<RelatorioCustomizadoResponseDTO> gerarRelatorioCustomizado(FiltroRelatorioCustomizadoDTO f);

	Page<DuplicataBuscaGeralDTO> buscarGeral(String termo, int pagina, int tamanho);

	Page<ParcelaBuscaGeralDTO> buscarGeralParcela(String termo, int pagina, int tamanho);

	Page<ParcelaBuscaGeralDTO> buscarGeralParcelaAtivas(String termo, int pagina, int tamanho);

}
