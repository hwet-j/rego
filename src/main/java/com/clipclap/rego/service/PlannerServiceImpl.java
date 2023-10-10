package com.clipclap.rego.service;

import com.clipclap.rego.mapper.PlannerMapper;
import com.clipclap.rego.model.dto.PlannerDTO;
import com.clipclap.rego.model.entitiy.Planner;
import com.clipclap.rego.repository.PlannerRepository;
import com.clipclap.rego.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PlannerServiceImpl implements PlannerService {

    private final PlannerRepository plannerRepository;
    private final PlannerMapper plannerMapper;
    private final UserRepository userRepository;

    @Override
    public LocalDate findStartTimeByPlanId(Integer planId) {

        Planner planner = plannerRepository.findByPlanId(planId);

        return planner.getStartDate();
    }

    @Override
    public List<PlannerDTO> findByUserEmail(String userEmail) {
        List<Planner> plannerEntities = plannerRepository.findByUserEmail_Email(userEmail);

        List<PlannerDTO> plannerDTOs = plannerEntities.stream()
                .map(PlannerMapper::entityToDto)        // 엔티티를 DTO로 변환
                .collect(Collectors.toList());          // DTO 목록으로 수집
        return plannerDTOs;
    }

    @Override
    public PlannerDTO findById(Integer planId) {
        Optional<Planner> plannerOptional = plannerRepository.findById(planId);

        if (plannerOptional.isPresent()){
            PlannerDTO dto = plannerMapper.entityToDto(plannerOptional.get());

            return dto;
        }

        return null;
    }

    @Override
    public void save(PlannerDTO dto) {
        Planner planner = plannerMapper.dtoToEntity(dto, userRepository);
        plannerRepository.save(planner);
    }
}
