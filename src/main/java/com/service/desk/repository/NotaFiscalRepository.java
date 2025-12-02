package com.service.desk.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.NotaFiscal;

@Repository
public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {
	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
    List<NotaFiscal> findByNumeroAndSerieAndFornecedorId(String numero, String serie, Long fornecedorId);
    
	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
	List<NotaFiscal> findByNumeroAndSerieAndFornecedorIdAndIdNot(String numero, String serie, Long fornecedorId,
			Long id);
	
	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
	List<NotaFiscal> findByNumeroAndFornecedorId(String numero, Long fornecedorId);
	
	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
    Page<NotaFiscal> findByNumeroContainingAndFornecedorId(String numero, Long fornecedorId, Pageable pageable);

	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
    Page<NotaFiscal> findAll(Pageable pageable);
    
	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
    @Query("SELECT n FROM NotaFiscal n " +
    	       "WHERE n.filial.id = :idFilial " +
    	       "AND n.dtCompra BETWEEN :dataInicial AND :dataFinal ORDER BY n.dtCompra ASC")
    	List<NotaFiscal> findByFilialAndPeriodo(Long idFilial, LocalDate dataInicial, LocalDate dataFinal);

	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
    @Query("SELECT n FROM NotaFiscal n " +
    	       "WHERE n.dtCompra BETWEEN :dataInicial AND :dataFinal ORDER BY n.dtCompra ASC")
    	List<NotaFiscal> findByPeriodo(LocalDate dataInicial, LocalDate dataFinal);

	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
    List<NotaFiscal> findByNumeroContainingIgnoreCase(String numero);
    
	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
    Page<NotaFiscal> findByNumeroContainingIgnoreCase(String numero, Pageable pageable);
    
	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
    Page<NotaFiscal> findByNumeroContainingIgnoreCaseOrFornecedorNomeContainingIgnoreCase(
            String numero, String fornecedorNome, Pageable pageable);
	
	@EntityGraph(attributePaths = {"parcelasPrevistas","fornecedor","tipo","filial","formaPagamento","duplicata"})
    List<NotaFiscal> findByDuplicataId(Long id);

}
