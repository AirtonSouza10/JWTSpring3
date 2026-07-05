package com.service.desk.dto.brapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrapiTickerDTO {

    private String symbol;

    private String name;

    private String longName;

    private String assetType;

    private String subType;

    private String exchange;

    private String currency;

    private String sector;

    private Boolean isActive;

    private String logoUrl;

    private QuoteDTO quote;
}
