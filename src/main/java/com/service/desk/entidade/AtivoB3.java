package com.service.desk.entidade;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ativos_b3", uniqueConstraints = {@UniqueConstraint(columnNames = {"symbol"})})
public class AtivoB3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String symbol;

    private String shortName;

    private String longName;

    private String currency;

    private String exchange;

    private String country;

    private String sector;

    private String industry;

    private String type;

    private String region;

    private String logo;

    private String isin;

    private Boolean active;

    // ===============================
    // COTAÇÃO
    // ===============================

    private BigDecimal regularMarketPrice;

    private BigDecimal regularMarketOpen;

    private BigDecimal regularMarketPreviousClose;

    private BigDecimal regularMarketDayHigh;

    private BigDecimal regularMarketDayLow;

    private BigDecimal regularMarketChange;

    private BigDecimal regularMarketChangePercent;

    private BigDecimal fiftyTwoWeekHigh;

    private BigDecimal fiftyTwoWeekLow;

    private BigDecimal marketCap;

    private Long regularMarketVolume;

    private LocalDateTime updatedAt;
}

