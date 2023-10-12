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

    /*
    private String type; // 항공, 식당, 관광지 등등...
    비행정보를 받아와서 저장해줄 필드
    private String airlineImg;
    private String airlineName;
    private String departureTime;
    private String departureAirport;
    private String arrivalTime;
    private String arrivalAirport;
    private String price;
    */

}
