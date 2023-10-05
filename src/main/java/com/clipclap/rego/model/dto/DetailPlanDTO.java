package com.clipclap.rego.model.dto;

import lombok.Data;

@Data
public class DetailPlanDTO {
    private Integer detailPlanId;
    private String content;
    private String startTime;
    private String endTime;
    private boolean allDay;
    private Integer touristAttractionId;

}
