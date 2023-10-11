package com.clipclap.rego.controller;

import com.clipclap.rego.model.dto.DetailPlanDTO;
import com.clipclap.rego.model.dto.PlannerDTO;
import com.clipclap.rego.model.dto.TouristAttractionDTO;
import com.clipclap.rego.repository.DetailPlanRepository;
import com.clipclap.rego.repository.TouristAttractionRepository;
import com.clipclap.rego.service.DetailPlanService;
import com.clipclap.rego.service.PlannerService;
import com.clipclap.rego.service.TouristAttractionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanController {

	private final TouristAttractionService touristAttractionService;
	private final TouristAttractionRepository touristAttractionRepository;
	private final ObjectMapper objectMapper;
	private final DetailPlanRepository detailPlanRepository;
	private final DetailPlanService detailPlanService;
	private final PlannerService plannerService;

	@GetMapping("/list")
	@PreAuthorize("isAuthenticated()")
	public String myPlanList(Model model, Principal principal ,PlannerDTO plannerDTO ) {

		if (principal != null){
			List<PlannerDTO> dtos = plannerService.findByUserEmail(principal.getName());
			System.out.println(dtos);
			model.addAttribute("test", dtos);
		}

		return "plan/planList";
	}

	@PostMapping("/add")
	@PreAuthorize("isAuthenticated()")
	public String myPlanAdd(Model model, Principal principal,
							@ModelAttribute @Valid PlannerDTO plannerDTO,
							BindingResult bindingResult) {


		if (bindingResult.hasErrors()) {
			System.out.println("문제 발생");
			List<ObjectError> errors = bindingResult.getAllErrors();
			for (ObjectError error : errors) {
				System.out.println(error.toString());
				System.out.println("검사 오류: " + error.getDefaultMessage());
			}
			return "plan/planList";
		}

		if (principal != null){
			System.out.println(principal.getName());
			plannerDTO.setUserEmail(principal.getName());
		}

		System.out.println(plannerDTO);
		plannerService.save(plannerDTO);
		return "redirect:/plan/list";
	}

	@GetMapping("/detail")
	public String map(@RequestParam(required = false) Integer planId, Model model) throws JsonProcessingException {
		PlannerDTO plannerDTO = plannerService.findById(planId);
		if (plannerDTO == null){
			return "redirect:/";
		}

		List<TouristAttractionDTO> touristAttractionListAll = touristAttractionService.touristListAll();

		String listAll = objectMapper.writeValueAsString(touristAttractionListAll);

		List<DetailPlanDTO> detailList = detailPlanService.findAllByPlan(planId);

		String detailPlan = objectMapper.writeValueAsString(detailList);

		// 상세플랜 목록
		model.addAttribute("detailPlan" , detailPlan);
		// 전체 관광지 리스트
		model.addAttribute("attractionList" , listAll);
		// 도시 리스트 (검색)
		model.addAttribute("cityList" , touristAttractionRepository.findDistinctCityNames());
		// 현재 사용중인 PK 번호 최대
		model.addAttribute("detailIdMax" , detailPlanService.findMaxDetailPlanIdByPlanId(planId));
		// 이후에 정보를 받아오면 필요없을듯
		model.addAttribute("planID" , planId);
		// 플래너의 시작날짜 (이것도 굳이 필요없을 수도)
		model.addAttribute("startDate" , plannerService.findStartTimeByPlanId(planId));

		return "plan/planDetail";
	}


}
