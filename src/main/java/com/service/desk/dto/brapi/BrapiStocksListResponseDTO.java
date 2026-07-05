package com.service.desk.dto.brapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrapiStocksListResponseDTO {

    @JsonProperty("stocks")
    private List<BrapiStockDTO> stocks;

    public BrapiStocksListResponseDTO() {
    }

    public List<BrapiStockDTO> getStocks() {
        return stocks;
    }

    public void setStocks(List<BrapiStockDTO> stocks) {
        this.stocks = stocks;
    }
}

