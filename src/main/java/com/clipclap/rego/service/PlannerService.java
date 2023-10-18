package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.PlannerDTO;

import java.time.LocalDate;
import java.util.List;

public interface PlannerService {

    LocalDate findStartTimeByPlanId(Integer planId);

    List<PlannerDTO> findByUserEmail(String userEmail);

    PlannerDTO findById(Integer planId);

    List<PlannerDTO> findByAllId();

    List<PlannerDTO> findTop4RecentPlanners();

    Integer save(PlannerDTO dto);

    int updateContentAndTypeAndNumberOfPeople(Integer planId, String content, String type, int numberOfPeople);
}
