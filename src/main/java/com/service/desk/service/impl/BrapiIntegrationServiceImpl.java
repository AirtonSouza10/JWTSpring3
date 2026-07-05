package com.service.desk.service.impl;

import com.service.desk.dto.brapi.BrapiStocksListResponseDTO;
import com.service.desk.dto.brapi.BrapiTickerDTO;
import com.service.desk.dto.brapi.BrapiTickersResponseDTO;
import com.service.desk.entidade.AtivoB3;
import com.service.desk.repository.AtivoB3Repository;
import com.service.desk.service.BrapiIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrapiIntegrationServiceImpl implements BrapiIntegrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrapiIntegrationServiceImpl.class);

    private final AtivoB3Repository ativoB3Repository;
    private final RestTemplate restTemplate;

    @Value("${brapi.token}")
    private String brapiToken;

    @Value("${brapi.url}")
    private String brapiUrl;

    @Value("${brapi.list.url}")
    private String brapiListUrl;

    @Value("${brapi.tickers.url}")
    private String brapiTickersUrl;

    public BrapiIntegrationServiceImpl(AtivoB3Repository ativoB3Repository, RestTemplate restTemplate) {
        this.ativoB3Repository = ativoB3Repository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public void sincronizarAtivos() {
        sincronizarAtivosInternal();
    }

    @Override
    public void sincronizarTodosOsTickers() {
        try {
            LOGGER.info("Starting sync of ALL available tickers from BRAPI");
            List<String> allTickers = descobrirTodosOsTickers();

            if (allTickers.isEmpty()) {
                LOGGER.warn("No tickers found to synchronize");
                return;
            }

            LOGGER.info("Found {} tickers to synchronize", allTickers.size());
            sincronizarAtivosInternal();
            LOGGER.info("Successfully synchronized all {} tickers", allTickers.size());

        } catch (Exception ex) {
            LOGGER.error("Error synchronizing all tickers: {}", ex.getMessage(), ex);
        }
    }

    private void sincronizarAtivosInternal() {

        URI uri = UriComponentsBuilder
                .fromUriString(brapiTickersUrl)
                .queryParam("sortOrder", "desc")
                .queryParam("limit", "23000")
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        if (brapiToken != null && !brapiToken.isBlank()) {
            headers.setBearerAuth(brapiToken);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {

            ResponseEntity<BrapiTickersResponseDTO> response =
                    restTemplate.exchange(
                            uri,
                            HttpMethod.GET,
                            entity,
                            BrapiTickersResponseDTO.class);

            BrapiTickersResponseDTO body = response.getBody();

            if (body == null || body.getResults() == null || body.getResults().isEmpty()) {
                LOGGER.warn("BRAPI retornou resposta sem resultados.");
                return;
            }

            LOGGER.info("Total de ativos retornados pela BRAPI: {}", body.getResults().size());

            for (BrapiTickerDTO dto : body.getResults()) {

                if (dto == null || dto.getSymbol() == null || dto.getSymbol().isBlank()) {
                    continue;
                }

                AtivoB3 ativo = ativoB3Repository
                        .findBySymbol(dto.getSymbol())
                        .orElseGet(AtivoB3::new);

                ativo.setSymbol(dto.getSymbol());
                ativo.setShortName(dto.getName());
                ativo.setLongName(dto.getLongName());
                ativo.setCurrency(dto.getCurrency());
                ativo.setSector(dto.getSector());
                ativo.setExchange(dto.getExchange());
                ativo.setType(dto.getAssetType());
                ativo.setLogo(dto.getLogoUrl());
                ativo.setActive(dto.getIsActive() != null ? dto.getIsActive() : true);

                if (dto.getQuote() != null) {
                    ativo.setRegularMarketPrice(dto.getQuote().getLastPrice());
                    ativo.setRegularMarketChangePercent(dto.getQuote().getChangePercent());
                    ativo.setRegularMarketVolume(dto.getQuote().getVolume());
                    ativo.setMarketCap(dto.getQuote().getMarketCap());
                }

                ativo.setUpdatedAt(LocalDateTime.now());

                ativoB3Repository.save(ativo);
            }

            LOGGER.info("Sincronização finalizada com sucesso. {} ativos processados.",
                    body.getResults().size());

        } catch (Exception ex) {
            LOGGER.error("Erro ao sincronizar ativos da BRAPI: {}", ex.getMessage(), ex);
        }
    }

    @Override
    public List<String> descobrirTickersPorTipo(String tipo) {
        List<String> allTickers = descobrirTodosOsTickers();
        return filtrarPorTipo(allTickers, tipo);
    }

    @Override
    public List<String> descobrirTodosOsTickers() {
        try {
            LOGGER.info("Fetching all available tickers from BRAPI");

            URI uri = UriComponentsBuilder.fromUriString(brapiListUrl)
                    .build(true)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            if (brapiToken != null && !brapiToken.isBlank()) {
                headers.setBearerAuth(brapiToken);
            }

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<BrapiStocksListResponseDTO> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity, BrapiStocksListResponseDTO.class);

            BrapiStocksListResponseDTO body = response.getBody();
            if (body == null || body.getStocks() == null) {
                LOGGER.warn("BRAPI returned empty stocks list");
                return new ArrayList<>();
            }

            return body.getStocks().stream()
                    .filter(stock -> stock != null && stock.getStock() != null)
                    .map(stock -> stock.getStock().trim().toUpperCase())
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            LOGGER.error("Error fetching tickers from BRAPI: {}", ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    private List<String> filtrarPorTipo(List<String> tickers, String tipo) {
        if (tickers.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            LOGGER.info("Filtering {} tickers by type: {}", tickers.size(), tipo);

            List<String> brapiTypes = mapTipoToBrapiTypes(tipo);

            URI uri = UriComponentsBuilder.fromUriString(brapiListUrl)
                    .build(true)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            if (brapiToken != null && !brapiToken.isBlank()) {
                headers.setBearerAuth(brapiToken);
            }

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<BrapiStocksListResponseDTO> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity, BrapiStocksListResponseDTO.class);

            BrapiStocksListResponseDTO body = response.getBody();
            if (body == null || body.getStocks() == null) {
                LOGGER.warn("BRAPI returned empty stocks list");
                return new ArrayList<>();
            }

            List<String> filtered = body.getStocks().stream()
                    .filter(stock -> stock != null && stock.getStock() != null && stock.getType() != null)
                    .filter(stock -> brapiTypes.contains(stock.getType().toLowerCase()))
                    .filter(stock -> tickers.contains(stock.getStock().trim().toUpperCase()))
                    .map(stock -> stock.getStock().trim().toUpperCase())
                    .distinct()
                    .collect(Collectors.toList());

            LOGGER.info("Found {} tickers of type {}", filtered.size(), tipo);
            return filtered;

        } catch (Exception ex) {
            LOGGER.error("Error filtering tickers by type {}: {}", tipo, ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    private List<String> mapTipoToBrapiTypes(String tipo) {
        String upperTipo = tipo.toUpperCase().trim();
        return switch (upperTipo) {
            case "AÇÃO", "AÇÕES", "STOCK" -> List.of("stock");
            case "ETF", "FUNDO", "FUND" -> List.of("fund");
            case "FII", "REITS" -> List.of("reits");
            case "BDR" -> List.of("bdr");
            case "TODOS", "ALL" -> List.of("stock", "fund", "reits", "bdr");
            default -> List.of(upperTipo.toLowerCase());
        };
    }
}
