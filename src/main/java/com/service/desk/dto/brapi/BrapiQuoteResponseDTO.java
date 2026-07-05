package com.service.desk.dto.brapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrapiQuoteResponseDTO {

    @JsonProperty("results")
    private List<BrapiQuoteResultDTO> results;

    @JsonProperty("requestedAt")
    private String requestedAt;

    public BrapiQuoteResponseDTO() {
    }

    public List<BrapiQuoteResultDTO> getResults() {
        return results;
    }

    public void setResults(List<BrapiQuoteResultDTO> results) {
        this.results = results;
    }

    public String getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(String requestedAt) {
        this.requestedAt = requestedAt;
    }
}

