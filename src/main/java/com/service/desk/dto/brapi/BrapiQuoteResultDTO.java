package com.service.desk.dto.brapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrapiQuoteResultDTO {

    private String symbol;

    @JsonProperty("shortName")
    private String shortName;

    @JsonProperty("longName")
    private String longName;

    private String currency;

    @JsonProperty("regularMarketPrice")
    private BigDecimal regularMarketPrice;

    @JsonProperty("regularMarketDayHigh")
    private BigDecimal regularMarketDayHigh;

    @JsonProperty("regularMarketDayLow")
    private BigDecimal regularMarketDayLow;

    @JsonProperty("regularMarketChangePercent")
    private BigDecimal regularMarketChangePercent;

    public BrapiQuoteResultDTO() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRegularMarketPrice() {
        return regularMarketPrice;
    }

    public void setRegularMarketPrice(BigDecimal regularMarketPrice) {
        this.regularMarketPrice = regularMarketPrice;
    }

    public BigDecimal getRegularMarketDayHigh() {
        return regularMarketDayHigh;
    }

    public void setRegularMarketDayHigh(BigDecimal regularMarketDayHigh) {
        this.regularMarketDayHigh = regularMarketDayHigh;
    }

    public BigDecimal getRegularMarketDayLow() {
        return regularMarketDayLow;
    }

    public void setRegularMarketDayLow(BigDecimal regularMarketDayLow) {
        this.regularMarketDayLow = regularMarketDayLow;
    }

    public BigDecimal getRegularMarketChangePercent() {
        return regularMarketChangePercent;
    }

    public void setRegularMarketChangePercent(BigDecimal regularMarketChangePercent) {
        this.regularMarketChangePercent = regularMarketChangePercent;
    }
}

