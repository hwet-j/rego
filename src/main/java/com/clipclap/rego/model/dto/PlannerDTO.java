package com.clipclap.rego.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlannerDTO {

    private Integer planId;
    private String userEmail;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfPeople;
    private String type;

}
