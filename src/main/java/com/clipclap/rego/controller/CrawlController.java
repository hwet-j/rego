package com.clipclap.rego.controller;

import com.clipclap.rego.service.CrawlService;
import com.clipclap.rego.model.dto.FlightInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CrawlController {
    @Autowired
    CrawlService crawlService;

    @GetMapping("/flightSearch")
    public String flightSearch(){
        return "crawl/flightSearch";
    }

    @GetMapping("/flightResult")
    public String noArgRoundCrawl(Model model
                                , @RequestParam String departureAirport
                                , @RequestParam String arrivalAirport
                                , @RequestParam String departureDate
                                , @RequestParam String arrivalDate){
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

        String formattedDepartureDate = LocalDate.parse(departureDate, inputFormat).format(outputFormat);
        String formattedArrivalDate = LocalDate.parse(arrivalDate, inputFormat).format(outputFormat);

        List<FlightInfo> flights = crawlService.getFlightInfo(departureAirport, arrivalAirport, formattedDepartureDate, formattedArrivalDate);
        model.addAttribute("flights", flights);
        return "crawl/roundCrawl";
    }



}
