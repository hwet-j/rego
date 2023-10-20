package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.DetailPlanDTO;
import com.clipclap.rego.model.dto.PreviewDTO;

import java.util.List;

public interface DetailPlanService {
    
    // 디테일 플랜 생성
    void makeDetailPlan(DetailPlanDTO detail);

    // 특정 플랜 ID의 모든 디테일 플랜 정보
    List<DetailPlanDTO> findAllByPlan(Integer planId);

    // 특정 디테일 플랜 ID로 디테일 플랮 정보
    DetailPlanDTO findById(Integer id);

    // 해당 플랜 ID의 최대 디테일 플랜 ID값 가져오기 
    Integer findMaxDetailPlanIdByPlanId(Integer planId);

    // 특정 플랜 ID의 모든 디테일 플랜 정보를 가져와 시간순으로 정렬
    List<DetailPlanDTO> findByPlanPlanIdOrderByStartTime(Integer planId);

    List<PreviewDTO> findPreview(Integer planId);

    int updateStartTimeAndEndTime(Integer planId, long daysBetween);

    // 특정 플랜의 비행기 비용
    Integer calculateTotalPriceForPlanWithFlight(Integer planId);

    // 특정 플랜의 비행기를 제외한 비용
    Integer calculateTotalPriceForPlanWithoutFlight(Integer planId);
}
