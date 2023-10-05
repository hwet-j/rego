package com.clipclap.rego.mapper;

import com.clipclap.rego.model.dto.DetailPlanDTO;
import com.clipclap.rego.model.entitiy.PlannerDetail;
import com.clipclap.rego.model.entitiy.TouristAttraction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class DetailPlanMapper {


    public static DetailPlanDTO entityToDto(PlannerDetail entity) {
        if (entity == null) {
            return null;
        }

        DetailPlanDTO dto = new DetailPlanDTO();
        dto.setDetailPlanId(entity.getDetailPlanId());
        dto.setContent(entity.getContent());
        dto.setStartTime(entity.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
        dto.setEndTime(entity.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
        dto.setAllDay(entity.isAllday());
        if (entity.getTouristAttraction() != null){
            dto.setTouristAttractionId(entity.getTouristAttraction().getTouristAttractionId());
        }

        return dto;
    }

    public static PlannerDetail dtoToEntity(DetailPlanDTO dto) {
        if (dto == null) {
            return null;
        }

        PlannerDetail entity = new PlannerDetail();
        entity.setDetailPlanId(dto.getDetailPlanId());
        entity.setContent(dto.getContent());
        entity.setStartTime(LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
        entity.setEndTime(LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
        entity.setAllday(dto.isAllDay());

        if (dto.getTouristAttractionId() != null) {
            TouristAttraction touristAttraction = new TouristAttraction();
            touristAttraction.setTouristAttractionId(dto.getTouristAttractionId());
            entity.setTouristAttraction(touristAttraction);
        }

        return entity;
    }

}
