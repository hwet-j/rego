package com.clipclap.rego.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightInfo {
    private List<RouteInfo> routes;
    private String price;
    private String departureDate;
    private String arrivalDate;

    public FlightInfo(List<RouteInfo> routes, String price) {
        this.routes = routes;
        this.price = price;
    }
}
