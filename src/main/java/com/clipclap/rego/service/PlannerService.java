package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.PlannerDTO;
import com.clipclap.rego.model.entitiy.User;

import java.time.LocalDate;
import java.util.List;

public interface PlannerService {

    LocalDate findStartTimeByPlanId(Integer planId);

    List<PlannerDTO> findByUserEmail(User userEmail);
}
