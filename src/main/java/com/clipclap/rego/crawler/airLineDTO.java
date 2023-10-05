package com.clipclap.rego.crawler;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class airLineDTO {
    private String airline;
    private String departureTime;
    private String departureAirport;
    private String arrivalTime;
    private String arrivalAirport;
    private String duration;
}
