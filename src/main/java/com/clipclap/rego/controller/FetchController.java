package com.clipclap.rego.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Controller
public class FetchController {
    @Autowired
    RestTemplate restTemplate;

            @GetMapping("/fetch")
            public String fetchData(Model model) throws URISyntaxException {
                String apiUrl = "http://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList?serviceKey=fseInZ0ZQr4ZrGEN%2BCiOTMGmIGlJ4R2AF7tX4HuvG5L06fVdnF%2FexCvt7DTDb23WFiI0A27opoDiuztovK1%2FkA%3D%3D";
                URI uri = new URI(apiUrl);
                Map<String,Object> result = restTemplate.getForObject(uri, Map.class);

                // 직접 URI에 담는 방식
                String apiUrlDirect = "http://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList?serviceKey=fseInZ0ZQr4ZrGEN%2BCiOTMGmIGlJ4R2AF7tX4HuvG5L06fVdnF%2FexCvt7DTDb23WFiI0A27opoDiuztovK1%2FkA%3D%3D";
                URI uriDirect = new URI(apiUrlDirect);
                System.out.println("Direct URI: " + uriDirect);
                
                //빌더방식
                String apiUrlBuilder = "http://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList";
                String originalServiceKey = "2Vs3dl/lYz9rYaHiX9Ph8OUHj4U7iDnYTK9AkVgdKAYBja9JNOkBrJhd/AqJZ//XHKnb45nT2sReRaIaJL/FmA==";

                URI uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrlBuilder)
                        .queryParam("serviceKey", originalServiceKey)
                        .build()
                        // .encode() // URI 인코딩
                        .toUri();
                System.out.println("Builder URI (Modified): " + uriBuilder);
                Map<String,Object> result2 = restTemplate.getForObject(uriBuilder, Map.class);

                System.out.println(result2);

                model.addAttribute("data", result);
                return "fetch";
            }
}
