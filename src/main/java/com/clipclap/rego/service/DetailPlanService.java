package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.DetailPlanDTO;

import java.util.List;

public interface DetailPlanService {

    void makeDetailPlan(DetailPlanDTO detail);

    List<DetailPlanDTO> findAllByPlan(Integer planId);

    DetailPlanDTO findById(Integer id);

    Integer findMaxDetailPlanIdByPlanId(Integer planId);

    List<DetailPlanDTO> findByPlanPlanIdOrderByStartTime(Integer planId);
}
