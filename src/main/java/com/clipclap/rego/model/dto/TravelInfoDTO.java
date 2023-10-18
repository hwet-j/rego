package com.clipclap.rego.model.dto;


import lombok.Data;

@Data
public class TravelInfoDTO {
    private String type;
    private String userEmail;
    private String content;
    private String startDate;
    private String endDate;
    private String numberOfPeople;
}