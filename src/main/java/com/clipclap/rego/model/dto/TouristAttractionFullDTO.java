package com.clipclap.rego.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TouristAttractionFullDTO {
    private Integer touristAttractionId;
    private String address;
    private String touristAttractionName;
    private String image;
    private String introduction;
    private double latitude;
    private double longitude;
    private String contentType;
    private String cityName;
    private String countryName;

}