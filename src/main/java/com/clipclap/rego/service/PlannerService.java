package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.PlanCard;
import com.clipclap.rego.model.dto.PlannerDTO;
import com.clipclap.rego.model.entitiy.Planner;

import java.time.LocalDate;
import java.util.List;

public interface PlannerService {

    // 특정 플랜 ID의 시작일
    LocalDate findStartTimeByPlanId(Integer planId);

    // 특정 유저의 플랜 리스트
    List<PlannerDTO> findByUserEmail(String userEmail);

    // 특정 플랜 ID의 플랜 정보
    PlannerDTO findById(Integer planId);

    // 모든 플랜 리스트
    List<PlannerDTO> findByAllId();

    List<PlannerDTO> findTop4RecentPlanners();

    // 플랜 저장
    Integer save(PlannerDTO dto);

    Planner copyPlanner(Integer sourcePlanId, String userEmail);

    int updateContentAndTypeAndNumberOfPeople(Integer planId, String content, String type, int numberOfPeople);

    void changeIsComplete(Integer planId);

    // 플랜 리스트에 출력할 모든 플랜 정보(필요 정보가 있어 PlanCard객체에 받아 사용)
    List<PlanCard> findAllPlanCard();

    // 플랜 찜
    void vote(Integer planId,String email);

    // 특정 유저가 찜한 플랜 ID 목록
    List<Integer> userPlanVotes(String email);

    // 찜 개수가 많은 5개의 플랜 정보
    List<PlanCard> findTop5PlanCard();

    List<PlanCard> userPlanVotePlans(List<Integer> plans);


}
