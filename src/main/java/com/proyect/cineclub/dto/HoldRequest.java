package com.proyect.cineclub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class HoldRequest {
    @NotEmpty
    private List<Long> butacasIds;

    //TTL minimo y maximo
    @Min(60)
    @Max(900)
    private Integer ttlSeconds;

    public List<Long> getButacasIds() {
        return butacasIds;
    }

    public void setButacasIds(List<Long> butacasIds) {
        this.butacasIds = butacasIds;
    }

    public Integer getTtlSeconds() {
        return ttlSeconds;
    }

    public void setTtlSeconds(Integer ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }
}
