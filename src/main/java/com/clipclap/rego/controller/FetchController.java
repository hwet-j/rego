package com.clipclap.rego.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class FetchController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/fetch")
    public String fetchData(
            @RequestParam(value = "departureAirport", required = false) String departureAirport,
            @RequestParam(value = "arrivalAirport", required = false) String arrivalAirport,
            Model model) throws URISyntaxException, UnsupportedEncodingException {


        List<String> departureAirportAndSchedule = new ArrayList<>();
        List<String> arrivalAirportAndSchedule = new ArrayList<>();

        // First API Call: getPassengerArrivalsOdp
        String departuresApiUrl = "http://apis.data.go.kr/B551177/StatusOfPassengerFlightsDSOdp/getPassengerDeparturesDSOdp?serviceKey=1aSaBDjtGv7lreIFCaI7K016XZYvLOvDcgTpPEGAOcp1TjOJY%2B9%2BxV9QkH5cA75XF1xpbVoZUC1WBE7XaNbAsw%3D%3D&type=xml";  // your departuresApiUrl here

        if (departureAirport != null && !departureAirport.trim().isEmpty()) {
            List<String> departureSchedules = departureFetchAirportAndScheduleFromApi(departuresApiUrl);
            departureAirportAndSchedule.addAll(
                    departureSchedules.stream()
                            .filter(s -> s.startsWith(departureAirport + " - "))
                            .collect(Collectors.toList())
            );
        }


        // Second API Call: getPassengerDeparturesOdp
        String arrivalsApiUrl = "http://apis.data.go.kr/B551177/StatusOfPassengerFlightsDSOdp/getPassengerArrivalsDSOdp?serviceKey=1aSaBDjtGv7lreIFCaI7K016XZYvLOvDcgTpPEGAOcp1TjOJY%2B9%2BxV9QkH5cA75XF1xpbVoZUC1WBE7XaNbAsw%3D%3D&type=xml";  // your arrivalsApiUrl here

        if (arrivalAirport != null && !arrivalAirport.trim().isEmpty()) {
            List<String> arrivalSchedules = arrivalFetchAirportAndScheduleFromApi(arrivalsApiUrl);
            arrivalAirportAndSchedule.addAll(
                    arrivalSchedules.stream()
                            .filter(s -> s.startsWith(arrivalAirport + " - "))
                            .collect(Collectors.toList())
            );
        }

        model.addAttribute("departureAirportAndSchedule", departureAirportAndSchedule);
        model.addAttribute("arrivalAirportAndSchedule", arrivalAirportAndSchedule);
        return "fetch";
    }




    private List<String> departureFetchAirportAndScheduleFromApi(String ApiUrl) throws URISyntaxException {
        List<String> departureAirportAndScheduleList = new ArrayList<>();

        URI uri = new URI(ApiUrl);
        Map<String, Object> result = restTemplate.getForObject(uri, Map.class);

        Map<String, Object> body = (Map<String, Object>) result.get("body");
        Map<String, Object> items = (Map<String, Object>) body.get("items");
        List<Map<String, String>> itemList;

        if (items.get("item") instanceof List) {
            itemList = (List<Map<String, String>>) items.get("item");
        } else {
            Map<String, String> singleItem = (Map<String, String>) items.get("item");
            itemList = new ArrayList<>();
            itemList.add(singleItem);
        }

        for (Map<String, String> item : itemList) {
            String departureAirport = item.get("airport");
            String departureScheduleDateTime = item.get("scheduleDateTime");
            departureAirportAndScheduleList.add(departureAirport + " - " + departureScheduleDateTime);
        }

        return departureAirportAndScheduleList;
    }

    private List<String> arrivalFetchAirportAndScheduleFromApi(String ApiUrl) throws URISyntaxException {
        List<String> arrivalAirportAndScheduleList = new ArrayList<>();

        URI uri = new URI(ApiUrl);
        Map<String, Object> result = restTemplate.getForObject(uri, Map.class);

        Map<String, Object> body = (Map<String, Object>) result.get("body");
        Map<String, Object> items = (Map<String, Object>) body.get("items");
        List<Map<String, String>> itemList;

        if (items.get("item") instanceof List) {
            itemList = (List<Map<String, String>>) items.get("item");
        } else {
            Map<String, String> singleItem = (Map<String, String>) items.get("item");
            itemList = new ArrayList<>();
            itemList.add(singleItem);
        }

        for (Map<String, String> item : itemList) {
            String arrivalAirport = item.get("airport");
            String arrivalScheduleDateTime = item.get("scheduleDateTime");
            arrivalAirportAndScheduleList.add(arrivalAirport + " - " + arrivalScheduleDateTime);
        }

        return arrivalAirportAndScheduleList;
    }
}