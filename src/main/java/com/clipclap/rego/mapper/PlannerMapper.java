package com.clipclap.rego.mapper;

import com.clipclap.rego.model.dto.PlannerDTO;
import com.clipclap.rego.model.entitiy.Planner;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class PlannerMapper {
    public static PlannerDTO entityToDto(Planner entity) {
        PlannerDTO dto = new PlannerDTO();
        dto.setPlanId(entity.getPlanId());
        dto.setUserEmail(entity.getUser().getEmail()); // User 엔티티의 이메일 주소 가져오기
        dto.setContent(entity.getContent());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setNumberOfPeople(entity.getNumberOfPeople());
        dto.setType(entity.getType());
        dto.setImagePath(entity.getImagePath());
        return dto;
    }

    public static Planner dtoToEntity(PlannerDTO dto, UserRepository userRepository) {
        Planner entity = new Planner();
        entity.setPlanId(dto.getPlanId());

        // User 엔티티 생성 및 이메일 주소 설정
        User user = userRepository.findByEmail(dto.getUserEmail());
        entity.setUser(user);
        entity.setContent(dto.getContent());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setNumberOfPeople(dto.getNumberOfPeople());
        entity.setType(dto.getType());
        entity.setImagePath(dto.getImagePath());
        return entity;
    }
}
