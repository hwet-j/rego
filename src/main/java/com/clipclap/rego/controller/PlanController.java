package com.clipclap.rego.controller;

import com.clipclap.rego.model.dto.PlannerDTO;
import com.clipclap.rego.repository.DetailPlanRepository;
import com.clipclap.rego.repository.TouristAttractionRepository;
import com.clipclap.rego.repository.UserRepository;
import com.clipclap.rego.service.AuthService;
import com.clipclap.rego.service.DetailPlanService;
import com.clipclap.rego.service.PlannerService;
import com.clipclap.rego.service.TouristAttractionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanController {

	private final UserRepository userRepository;
	private final AuthService authService;
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

		return "redirect:/plan/list";
	}


}
