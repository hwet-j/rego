package com.clipclap.rego.controller;

import com.clipclap.rego.model.dto.DetailPlanDTO;
import com.clipclap.rego.model.dto.TouristAttractionDTO;
import com.clipclap.rego.model.entitiy.City;
import com.clipclap.rego.repository.TouristAttractionRepository;
import com.clipclap.rego.service.DetailPlanService;
import com.clipclap.rego.service.TouristAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class AttractionController {

	private final TouristAttractionService touristAttractionService;
	private final DetailPlanService detailPlanService;
	private final TouristAttractionRepository touristAttractionRepository;
	/* 관광지를 도시별로 검색 */
	@GetMapping("/selectAttraction")
	public List<TouristAttractionDTO> getAttractionsByKeyword(@RequestParam String keyword, @RequestParam String contentType) {

		if (keyword.equals("")){
			List<TouristAttractionDTO> touristAttractions =  touristAttractionService.touristListAll();
			return touristAttractions;
		}
		City city = new City();
		city.setCityName(keyword);

		List<TouristAttractionDTO> touristAttractions = touristAttractionService.cityContentTypeList(city,contentType);

		return touristAttractions;
	}



	@PostMapping(value ="/insertPlan",  produces = "application/json")
	public List<TouristAttractionDTO> insertDetail(@RequestBody DetailPlanDTO detailPlanDTO) {

		System.out.println(detailPlanDTO);

		detailPlanService.makeDetailPlan(detailPlanDTO);

		return null;
	}

}
