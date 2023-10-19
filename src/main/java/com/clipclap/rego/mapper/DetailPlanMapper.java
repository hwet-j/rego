package com.clipclap.rego.mapper;

import com.clipclap.rego.model.dto.DetailPlanDTO;
import com.clipclap.rego.model.entitiy.Planner;
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

        dto.setPrice(entity.getPrice());

        /* 비행기 정보 매핑 */
        dto.setAirlineImg(entity.getAirlineImg());
        dto.setAirlineName(entity.getAirlineName());
        dto.setDepartureTime(entity.getDepartureTime());
        dto.setDepartureAirport(entity.getDepartureAirport());
        dto.setArrivalTime(entity.getArrivalTime());
        dto.setArrivalAirport(entity.getArrivalAirport());



        // entity.getTouristAttraction() 및 entity.getPlan()으로부터 관광지 ID 및 플랜 ID 설정

        if (entity.getTouristAttraction() != null) {
            dto.setTouristAttractionId(entity.getTouristAttraction().getTouristAttractionId());
        }

        if (entity.getPlan() != null) {
            dto.setPlanId(entity.getPlan().getPlanId());
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


        if(dto.getPrice() == null){
            entity.setPrice(0);
        } else {
            entity.setPrice(dto.getPrice());
        }


        /* 비행기 정보 매핑 */
        entity.setAirlineImg(dto.getAirlineImg());
        entity.setAirlineName(dto.getAirlineName());
        entity.setDepartureTime(dto.getDepartureTime());
        entity.setDepartureAirport(dto.getDepartureAirport());
        entity.setArrivalTime(dto.getArrivalTime());
        entity.setArrivalAirport(dto.getArrivalAirport());

        // dto.getTouristAttractionId() 및 dto.getPlanId()로부터 관광지 및 플랜 설정

        if (dto.getTouristAttractionId() != null) {
            TouristAttraction touristAttraction = new TouristAttraction();
            touristAttraction.setTouristAttractionId(dto.getTouristAttractionId());
            entity.setTouristAttraction(touristAttraction);
        }

        if (dto.getPlanId() != null) {
            Planner plan = new Planner();
            plan.setPlanId(dto.getPlanId());
            entity.setPlan(plan);
        }

        return entity;
    }

}
