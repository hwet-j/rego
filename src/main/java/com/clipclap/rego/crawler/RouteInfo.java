package com.clipclap.rego.crawler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteInfo {
    private String airlineImg;
    private String airlineName;
    private String departureTime;
    private String departureAirport;
    private String arrivalTime;
    private String arrivalAirport;
    private String duration;
}
