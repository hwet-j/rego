package com.clipclap.rego.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PreviewDTO {

    private String cityName;

    private String detailPlanId;

    private String tourAttractionName;

    private String image;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


    public LocalDateTime getStartTimePlus9Hours() {
        return startTime.plusHours(9);
    }

    // endTime에 9 시간 추가
    public LocalDateTime getEndTimePlus9Hours() {
        return endTime.plusHours(9);
    }

}
