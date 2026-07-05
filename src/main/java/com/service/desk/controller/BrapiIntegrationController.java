package com.service.desk.controller;

import com.service.desk.service.BrapiIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/integracao/brapi")
public class BrapiIntegrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrapiIntegrationController.class);

    private final BrapiIntegrationService brapiIntegrationService;

    public BrapiIntegrationController(BrapiIntegrationService brapiIntegrationService) {
        this.brapiIntegrationService = brapiIntegrationService;
    }

    /**
     * POST /api/integracao/brapi/sincronizar
     * Inicia sincronização manual de ativos para os tickers fornecidos
     *
     * Body example:
     * {
     *   "tickers": ["PETR4", "VALE3", "KNRI11"]
     * }
     */
    @PostMapping("/sincronizar")
    public ResponseEntity<Map<String, Object>> sincronizarAtivos(@RequestBody Map<String, List<String>> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<String> tickers = request.get("tickers");
            if (tickers == null || tickers.isEmpty()) {
                response.put("status", "error");
                response.put("message", "Nenhum ticker fornecido");
                return ResponseEntity.badRequest().body(response);
            }

            LOGGER.info("Manual sync started for {} tickers", tickers.size());
            brapiIntegrationService.sincronizarAtivos();

            response.put("status", "success");
            response.put("message", "Sincronização iniciada para " + tickers.size() + " ticker(s)");
            response.put("tickers", tickers);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            LOGGER.error("Error during manual BRAPI sync: {}", ex.getMessage(), ex);
            response.put("status", "error");
            response.put("message", "Erro na sincronização: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * POST /api/integracao/brapi/sincronizar-todos
     * Sincroniza TODOS os tickers disponíveis na BRAPI
     * Atenção: Esta operação pode levar vários minutos!
     */
    @PostMapping("/sincronizar-todos")
    public ResponseEntity<Map<String, Object>> sincronizarTodos() {
        Map<String, Object> response = new HashMap<>();

        try {
            LOGGER.info("Starting full sync of ALL available tickers");
            brapiIntegrationService.sincronizarTodosOsTickers();

            response.put("status", "success");
            response.put("message", "Sincronização de TODOS os tickers iniciada (operação pode levar vários minutos)");
            return ResponseEntity.accepted().body(response);

        } catch (Exception ex) {
            LOGGER.error("Error during full BRAPI sync: {}", ex.getMessage(), ex);
            response.put("status", "error");
            response.put("message", "Erro na sincronização: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * GET /api/integracao/brapi/descobrir-tickers?tipo=AÇÕES
     * Descobre tickers de um tipo específico
     *
     * Tipos aceitos: AÇÕES, STOCK, ETF, FII, REITS, BDR, TODOS
     */
    @GetMapping("/descobrir-tickers")
    public ResponseEntity<Map<String, Object>> descobrirTickersPorTipo(
            @RequestParam(value = "tipo", defaultValue = "AÇÕES") String tipo) {

        Map<String, Object> response = new HashMap<>();

        try {
            LOGGER.info("Discovering tickers by type: {}", tipo);
            List<String> tickers = brapiIntegrationService.descobrirTickersPorTipo(tipo);

            response.put("status", "success");
            response.put("tipo", tipo);
            response.put("total", tickers.size());
            response.put("tickers", tickers);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            LOGGER.error("Error discovering tickers by type {}: {}", tipo, ex.getMessage(), ex);
            response.put("status", "error");
            response.put("message", "Erro ao descobrir tickers: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * GET /api/integracao/brapi/listar-tickers
     * Lista todos os tickers disponíveis
     */
    @GetMapping("/listar-tickers")
    public ResponseEntity<Map<String, Object>> listarTodosTickers() {
        Map<String, Object> response = new HashMap<>();

        try {
            LOGGER.info("Listing all available tickers");
            List<String> tickers = brapiIntegrationService.descobrirTodosOsTickers();

            response.put("status", "success");
            response.put("total", tickers.size());
            response.put("tickers", tickers);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            LOGGER.error("Error listing tickers: {}", ex.getMessage(), ex);
            response.put("status", "error");
            response.put("message", "Erro ao listar tickers: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * POST /api/integracao/brapi/sincronizar-por-tipo
     * Descobre e sincroniza automaticamente todos os ativos de um tipo específico
     *
     * Body example:
     * {
     *   "tipo": "AÇÕES"
     * }
     */
    @PostMapping("/sincronizar-por-tipo")
    public ResponseEntity<Map<String, Object>> sincronizarPorTipo(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String tipo = request.get("tipo");
            if (tipo == null || tipo.isBlank()) {
                response.put("status", "error");
                response.put("message", "Tipo não fornecido");
                return ResponseEntity.badRequest().body(response);
            }

            LOGGER.info("Discovering and syncing tickers by type: {}", tipo);
            List<String> tickers = brapiIntegrationService.descobrirTickersPorTipo(tipo);

            if (tickers.isEmpty()) {
                response.put("status", "success");
                response.put("message", "Nenhum ticker encontrado para o tipo: " + tipo);
                response.put("type", tipo);
                response.put("total", 0);
                return ResponseEntity.ok(response);
            }

            brapiIntegrationService.sincronizarAtivos();

            response.put("status", "success");
            response.put("message", "Sincronização iniciada para " + tickers.size() + " ticker(s) do tipo " + tipo);
            response.put("type", tipo);
            response.put("total", tickers.size());
            response.put("tickers", tickers);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            LOGGER.error("Error during type-based BRAPI sync: {}", ex.getMessage(), ex);
            response.put("status", "error");
            response.put("message", "Erro na sincronização: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

