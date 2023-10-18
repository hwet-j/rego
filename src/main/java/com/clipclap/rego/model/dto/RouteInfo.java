package com.clipclap.rego.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteInfo {
    @JsonProperty("airlineImg")
    private String airlineImg;
    @JsonProperty("airlineName")
    private String airlineName;
    @JsonProperty("departureTime")
    private String departureTime;
    @JsonProperty("departureAirport")
    private String departureAirport;
    @JsonProperty("arrivalTime")
    private String arrivalTime;
    @JsonProperty("arrivalAirport")
    private String arrivalAirport;
    @JsonProperty("duration")
    private String duration;
}
