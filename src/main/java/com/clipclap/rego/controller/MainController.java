package com.clipclap.rego.controller;

import com.clipclap.rego.model.dto.TouristAttractionDTO;
import com.clipclap.rego.model.dto.TouristAttractionFullDTO;
import com.clipclap.rego.model.entitiy.City;
import com.clipclap.rego.model.entitiy.TouristAttraction;
import com.clipclap.rego.repository.CityRepository;
import com.clipclap.rego.repository.CountryRepository;
import com.clipclap.rego.service.TouristAttractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/touristAttraction")
public class MainController {

    private final TouristAttractionService touristAttractionService;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    /* 관광지 전체 목록을 보여주는 메서드 */
    @GetMapping("/list")
    public String list(Model model) {

        List<String> countryNames = countryRepository.findAllDistinctCountryNames();

        List<TouristAttractionFullDTO> touristAttractionDTOList = touristAttractionService.getTouristAttractionsWithCityAndCountry();

        // 초기 전달 정보는 나라이름 뿐이며 나머지 정보는 AJAX를 통해서 값을 불러와 구현중
        model.addAttribute("countryNames", countryNames);
        model.addAttribute("touristAttractionList", touristAttractionDTOList);

        return "main_detail";
    }


}
