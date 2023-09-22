package com.clipclap.rego.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Controller
public class FetchController {
    @Autowired
    RestTemplate restTemplate;

            @GetMapping("/fetch")
            public String fetchData(Model model) throws URISyntaxException, UnsupportedEncodingException {
                String apiUrl = "http://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList?serviceKey=fseInZ0ZQr4ZrGEN%2BCiOTMGmIGlJ4R2AF7tX4HuvG5L06fVdnF%2FexCvt7DTDb23WFiI0A27opoDiuztovK1%2FkA%3D%3D";
                URI uri = new URI(apiUrl);
                Map<String,Object> result = restTemplate.getForObject(uri, Map.class);

                // 직접 URI에 담는 방식
                String apiUrlDirect = "http://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList?serviceKey=fseInZ0ZQr4ZrGEN%2BCiOTMGmIGlJ4R2AF7tX4HuvG5L06fVdnF%2FexCvt7DTDb23WFiI0A27opoDiuztovK1%2FkA%3D%3D";
                URI uriDirect = new URI(apiUrlDirect);
                System.out.println("Direct URI: " + uriDirect);
                model.addAttribute("data", result);

                //빌더방식
                String baseUrl = "http://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList";
                String encodedServiceKey = "fseInZ0ZQr4ZrGEN%2BCiOTMGmIGlJ4R2AF7tX4HuvG5L06fVdnF%2FexCvt7DTDb23WFiI0A27opoDiuztovK1%2FkA%3D%3D";

                UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(baseUrl)
                // 여기에 다른 파라미터들을 추가하세요. 예: .queryParam("paramName", "paramValue")
                        .build();

                String uriWithoutServiceKey = uriComponents.toUriString();

                String finalUriString = uriWithoutServiceKey +
                        "?serviceKey=" + encodedServiceKey;

                // URI 타입으로 변환
                URI finalUri = URI.create(finalUriString);

                System.out.println("Final URI: " + finalUri);

                // RestTemplate 호출
                Map<String, Object> result3 = restTemplate.getForObject(finalUri, Map.class);
                System.out.println(result3);

                return "fetch";
            }
}
