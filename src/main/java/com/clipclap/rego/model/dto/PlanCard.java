package com.clipclap.rego.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class PlanCard {

    private Integer planId;

    private String userEmail;

    private String content;

    private LocalDate startDate;

    private LocalDate endDate;

    private int numberOfPeople;

    private String type;

    private String imagePath;

    private String flightPrice;

    private String withoutFlightPrice;

    private Set<String> voter;
}
