package com.clipclap.rego.controller;

import com.clipclap.rego.model.dto.DetailPlanDTO;
import com.clipclap.rego.model.dto.TouristAttractionDTO;
import com.clipclap.rego.model.entitiy.City;
import com.clipclap.rego.repository.DetailPlanRepository;
import com.clipclap.rego.repository.TouristAttractionRepository;
import com.clipclap.rego.service.DetailPlanService;
import com.clipclap.rego.service.TouristAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class AttractionController {

	private final TouristAttractionRepository touristAttractionRepository;
	private final DetailPlanRepository detailPlanRepository;
	private final TouristAttractionService touristAttractionService;
	private final DetailPlanService detailPlanService;
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


	@Transactional
	@PostMapping(value ="/insertPlan",  produces = "application/json")
	public List<TouristAttractionDTO> insertDetail(@RequestBody DetailPlanDTO detailPlanDTO) {

		System.out.println(detailPlanDTO.isAllDay());
		// 일반 수정
		if (detailPlanDTO.getStartTime() == null){
			DetailPlanDTO originDetail = detailPlanService.findById(detailPlanDTO.getDetailPlanId());
			detailPlanDTO.setStartTime(originDetail.getStartTime());
			detailPlanDTO.setEndTime(originDetail.getEndTime());
			detailPlanDTO.setAllDay(originDetail.isAllDay());
		}
		
		// 이벤트 드래그 수정 / 이벤트 길이 조절
		if (detailPlanDTO.getContent() == null){
			DetailPlanDTO originDetail = detailPlanService.findById(detailPlanDTO.getDetailPlanId());
			detailPlanDTO.setContent(originDetail.getContent());
			detailPlanDTO.setTouristAttractionId(originDetail.getTouristAttractionId());
			detailPlanDTO.setAllDay(originDetail.isAllDay());
		}


		System.out.println(detailPlanDTO.isAllDay());
		detailPlanService.makeDetailPlan(detailPlanDTO);

		return null;
	}

	@Transactional
	@PostMapping(value ="/deletePlan")
	public List<TouristAttractionDTO> insertDetail(Integer detailPlanId) {

		detailPlanRepository.deleteByDetailPlanId(detailPlanId);
		return null;
	}

}
