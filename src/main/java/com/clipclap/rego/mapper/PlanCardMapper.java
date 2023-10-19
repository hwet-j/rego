package com.clipclap.rego.mapper;

import com.clipclap.rego.model.dto.PlanCard;
import com.clipclap.rego.model.entitiy.Planner;
import org.springframework.stereotype.Component;

@Component
public class PlanCardMapper {


    public PlanCard entityToPlanCard(Planner entity) {
        PlanCard card = new PlanCard();
        card.setPlanId(entity.getPlanId());
        card.setUserEmail(entity.getUser().getEmail()); // User 엔티티의 이메일 주소 가져오기
        card.setContent(entity.getContent());
        card.setStartDate(entity.getStartDate());
        card.setEndDate(entity.getEndDate());
        card.setNumberOfPeople(entity.getNumberOfPeople());
        card.setType(entity.getType());
        card.setImagePath(entity.getImagePath());

        card.setFlightPrice("0");
        card.setWithoutFlightPrice("0");

        return card;
    }

}
