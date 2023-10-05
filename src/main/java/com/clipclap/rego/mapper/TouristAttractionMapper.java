package com.clipclap.rego.mapper;

import com.clipclap.rego.model.dto.TouristAttractionDTO;
import com.clipclap.rego.model.entitiy.TouristAttraction;
import org.springframework.stereotype.Component;

@Component
public class TouristAttractionMapper {

    public static TouristAttractionDTO entityToDto(TouristAttraction entity) {
        if (entity == null) {
            return null;
        }

        TouristAttractionDTO dto = new TouristAttractionDTO();
        dto.setTouristAttractionId(entity.getTouristAttractionId());
        dto.setAddress(entity.getAddress());
        dto.setName(entity.getName());
        dto.setImage(entity.getImage());
        dto.setIntroduction(entity.getIntroduction());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setContentType(entity.getContentType());

        return dto;
    }

    public static TouristAttraction dtoToEntity(TouristAttractionDTO dto) {
        if (dto == null) {
            return null;
        }

        TouristAttraction entity = new TouristAttraction();
        entity.setTouristAttractionId(dto.getTouristAttractionId());
        entity.setAddress(dto.getAddress());
        entity.setName(dto.getName());
        entity.setImage(dto.getImage());
        entity.setIntroduction(dto.getIntroduction());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setContentType(dto.getContentType());

        return entity;
    }
}
