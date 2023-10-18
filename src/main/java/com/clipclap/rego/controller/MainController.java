package com.clipclap.rego.controller;

import com.clipclap.rego.model.dto.TouristAttractionDTO;
import com.clipclap.rego.model.dto.TouristAttractionFullDTO;
import com.clipclap.rego.model.entitiy.City;
import com.clipclap.rego.repository.CityRepository;
import com.clipclap.rego.repository.CountryRepository;
import com.clipclap.rego.service.TouristAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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


    /* 나라별 도시목록을 가져오는 메서드 (AJAX) */
    @GetMapping("/cityList")
    @ResponseBody
    public List<String> cityList(@RequestParam String keyword) {

        List<String> cityNames = cityRepository.findDistinctCityNamesByCountryName(keyword);

        return cityNames;
    }



    /* 도시별로 검색하는 메서드 (AJAX를 통해서 구현되어있음) */
    @GetMapping("/search")
    @ResponseBody
    public List<TouristAttractionDTO> search(@RequestParam String keyword) {

        // keyword에는 도시 이름 정보가 담겨있으며 해당 도시 이름으로 City객체를 생성해서 정보를 전달함
        City cityObject = new City();
        cityObject.setCityName(keyword);

        List<TouristAttractionDTO> touristAttractions = touristAttractionService.countryAttractionList(cityObject);

        // @ResponseBody 어노테이션을 사용하면 객체를 전달할 경우에 JSON형식으로 변환하여 return한다.
        return touristAttractions;
    }


}
