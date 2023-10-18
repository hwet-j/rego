package com.clipclap.rego.model.dto;

import lombok.Data;

@Data
public class DetailPlanDTO {
    private Integer detailPlanId;
    private Integer planId;
    private String content;
    private String startTime;
    private String endTime;
    private boolean allDay;
    private Integer touristAttractionId;
    private Integer price;


    /* 항공정보 */
    private String airlineImg;
    private String airlineName;
    private String departureTime;
    private String departureAirport;
    private String arrivalTime;
    private String arrivalAirport;

}
