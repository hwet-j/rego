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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

		System.out.println(detailPlanDTO);
		// 일반 수정
		if (detailPlanDTO.getStartTime() == null){
			DetailPlanDTO originDetail = detailPlanService.findById(detailPlanDTO.getDetailPlanId());
			detailPlanDTO.setStartTime(originDetail.getStartTime());
			detailPlanDTO.setEndTime(originDetail.getEndTime());
			// detailPlanDTO.setAllDay(originDetail.isAllDay());
		}
		
		// 이벤트 드래그 수정 / 이벤트 길이 조절
		if (detailPlanDTO.getContent() == null){
			DetailPlanDTO originDetail = detailPlanService.findById(detailPlanDTO.getDetailPlanId());
			detailPlanDTO.setContent(originDetail.getContent());
			detailPlanDTO.setTouristAttractionId(originDetail.getTouristAttractionId());
			// detailPlanDTO.setAllDay(originDetail.isAllDay());
		}



		/* allDay 객체를 이동할 경우 endTime 설정이 안되므로 강제 설정 */
		if(detailPlanDTO.getEndTime() == null){
			Instant startTime = Instant.parse(detailPlanDTO.getStartTime());

			// Instant를 UTC 기준의 ZonedDateTime으로 변환
			ZonedDateTime zonedDateTime = startTime.atZone(ZoneId.of("UTC"));

			// 1시간(3600초)을 더해서 endTime 설정
			ZonedDateTime endTime = zonedDateTime.plusHours(1);

			// ZonedDateTime을 문자열로 변환하여 원하는 형식으로 포맷팅
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String endTimeStr = formatter.format(endTime);

			detailPlanDTO.setEndTime(endTimeStr);
		}

		System.out.println(detailPlanDTO);
		detailPlanService.makeDetailPlan(detailPlanDTO);

		return null;
	}

	@Transactional
	@PostMapping(value ="/deletePlan")
	public List<TouristAttractionDTO> insertDetail(Integer detailPlanId, Integer planId) {

		detailPlanRepository.deleteByPlanPlanIdAndDetailPlanId(planId, detailPlanId);
		return null;
	}

}
