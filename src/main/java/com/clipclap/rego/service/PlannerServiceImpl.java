package com.clipclap.rego.service;

import com.clipclap.rego.mapper.PlannerMapper;
import com.clipclap.rego.model.dto.PlannerDTO;
import com.clipclap.rego.model.entitiy.Planner;
import com.clipclap.rego.model.entitiy.PlannerDetail;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.DetailPlanRepository;
import com.clipclap.rego.repository.PlannerRepository;
import com.clipclap.rego.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final DetailPlanRepository detailPlanRepository;
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
            PlannerDTO dto = PlannerMapper.entityToDto(plannerOptional.get());

            return dto;
        }

        return null;
    }

    @Override
    public List<PlannerDTO> findByAllId() {

        List<Planner> planners = plannerRepository.findAll();

        List<PlannerDTO> plannerDTOs = planners.stream()
                .map(PlannerMapper::entityToDto)
                .collect(Collectors.toList());

        return plannerDTOs;
    }



    @Override
    public List<PlannerDTO> findTop4RecentPlanners() {
        List<Planner> planners = plannerRepository.findTop4ByOrderByPlanIdDesc(); // 가장 최근에 만들어진 4개의 플래너를 조회합니다.

        List<PlannerDTO> plannerDTOs = planners.stream()
                .map(PlannerMapper::entityToDto)
                .collect(Collectors.toList());

        return plannerDTOs;
    }


    @Override
    public Integer save(PlannerDTO dto) {
        if(dto.getType().equals("힐링")){
            dto.setImagePath("https://github.com/hwet-j/hwet-j.github.io/assets/81364742/56961613-b6b5-431a-99a8-89210728551e");
        } else if(dto.getType().equals("문화")) {
            dto.setImagePath("https://github.com/hwet-j/hwet-j.github.io/assets/81364742/d7cb0a0b-4457-4c5c-990c-842d59d16527");
        } else if(dto.getType().equals("쇼핑")) {
            dto.setImagePath("https://github.com/hwet-j/hwet-j.github.io/assets/81364742/73f16ac0-64c0-428e-93ba-60418fb2d65f");
        } else if(dto.getType().equals("식도락")) {
            dto.setImagePath("https://github.com/hwet-j/hwet-j.github.io/assets/81364742/8650721a-ab0d-4f71-ad28-e9fb73477a1d");
        } else if(dto.getType().equals("자유")) {
            dto.setImagePath("https://github.com/hwet-j/hwet-j.github.io/assets/81364742/7c38de8f-adc9-46a0-8d81-288b610cde87");
        }

        Planner planner = PlannerMapper.dtoToEntity(dto, userRepository);

        Planner savedPlanner = plannerRepository.save(planner);

        return savedPlanner.getPlanId();
    }

    @Override
    public Planner copyPlanner(Integer sourcePlanId, String userEmail) {
        Planner sourcePlanner = plannerRepository.findById(sourcePlanId).orElse(null);
        if (sourcePlanner == null) {
            throw new EntityNotFoundException("Planner not found");
        }

        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }
        User user=optionalUser.get();

        Planner newPlanner = new Planner();
        newPlanner.setUser(user);
        newPlanner.setContent(sourcePlanner.getContent());
        newPlanner.setStartDate(sourcePlanner.getStartDate());
        newPlanner.setEndDate(sourcePlanner.getEndDate());
        newPlanner.setNumberOfPeople(sourcePlanner.getNumberOfPeople());
        newPlanner.setType(sourcePlanner.getType());
        newPlanner.setImagePath(sourcePlanner.getImagePath());

        plannerRepository.save(newPlanner);

        List<PlannerDetail> sourceDetailPlans = detailPlanRepository.findByPlan(sourcePlanner);

        for (PlannerDetail sourceDetailPlan : sourceDetailPlans) {
            PlannerDetail newDetailPlan = new PlannerDetail();
            newDetailPlan.setPlan(newPlanner);
            newDetailPlan.setContent(sourceDetailPlan.getContent());
            newDetailPlan.setStartTime(sourceDetailPlan.getStartTime());
            newDetailPlan.setAllday(sourceDetailPlan.isAllday());
            newDetailPlan.setEndTime(sourceDetailPlan.getEndTime());
            newDetailPlan.setTouristAttraction(sourceDetailPlan.getTouristAttraction());

            detailPlanRepository.save(newDetailPlan);
        }

        return newPlanner;
    }

    }
