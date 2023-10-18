package com.clipclap.rego.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightInfo {
    @JsonProperty("routes")
    private List<RouteInfo> routes;
    @JsonProperty("price")
    private String price;
    @JsonProperty("departureDate")
    private String departureDate;
    @JsonProperty("arrivalDate")
    private String arrivalDate;

    public FlightInfo(List<RouteInfo> routes, String price) {
        this.routes = routes;
        this.price = price;
    }
}
