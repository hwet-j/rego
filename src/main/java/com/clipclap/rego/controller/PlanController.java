package com.clipclap.rego.controller;

import com.clipclap.rego.repository.DetailPlanRepository;
import com.clipclap.rego.repository.TouristAttractionRepository;
import com.clipclap.rego.repository.UserRepository;
import com.clipclap.rego.service.AuthService;
import com.clipclap.rego.service.DetailPlanService;
import com.clipclap.rego.service.PlannerService;
import com.clipclap.rego.service.TouristAttractionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


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
	public String myPlanList(Model model, Principal principal) {
		System.out.println(principal.getName());
//		findByUserEmail
		return "plan/planList";
	}



}
