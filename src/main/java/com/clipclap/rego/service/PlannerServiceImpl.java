package com.clipclap.rego.service;

import com.clipclap.rego.mapper.PlannerMapper;
import com.clipclap.rego.model.dto.PlannerDTO;
import com.clipclap.rego.model.entitiy.Planner;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PlannerServiceImpl implements PlannerService {

    private final PlannerRepository plannerRepository;
    private final PlannerMapper plannerMapper;

    @Override
    public LocalDate findStartTimeByPlanId(Integer planId) {

        Planner planner = plannerRepository.findByPlanId(planId);

        return planner.getStartDate();
    }

    @Override
    public List<PlannerDTO> findByUserEmail(User userEmail) {
        List<Planner> plannerEntities = plannerRepository.findByUserEmail(userEmail);

        List<PlannerDTO> plannerDTOs = plannerEntities.stream()
                .map(PlannerMapper::entityToDto) // 엔티티를 DTO로 변환
                .collect(Collectors.toList()); // DTO 목록으로 수집
        return plannerDTOs;
    }
}
