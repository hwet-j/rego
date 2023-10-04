package com.clipclap.rego.model.dto;

import lombok.Data;

@Data
public class DetailPlanDTO {
    private Long detailPlanId;
    private String content;
    private String startTime;
    private String endTime;
    private boolean allDay;
    private Long touristAttractionId;

}
