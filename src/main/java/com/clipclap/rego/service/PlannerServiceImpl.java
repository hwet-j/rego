package com.clipclap.rego.service;

import com.clipclap.rego.mapper.PlannerMapper;
import com.clipclap.rego.model.dto.PlanCard;
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
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PlannerServiceImpl implements PlannerService {

    private final PlannerRepository plannerRepository;
    private final DetailPlanRepository detailPlanRepository;
    private final PlannerMapper plannerMapper;
    private final UserRepository userRepository;
    private final DetailPlanService detailPlanService;



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
            dto.setImagePath("https://github.com/hwet-j/rego/assets/125880480/9cd46f23-f82b-43e0-8657-ad0bf8620dda");
        } else if(dto.getType().equals("문화")) {
            dto.setImagePath("https://github.com/hwet-j/rego/assets/125880480/5ef1fea4-c460-4373-a5e4-afbc2d8fb7c4");
        } else if(dto.getType().equals("쇼핑")) {
            dto.setImagePath("https://github.com/hwet-j/rego/assets/125880480/419d0be8-f5f1-4dd8-8294-14ef15cc23cf");
        } else if(dto.getType().equals("식도락")) {
            dto.setImagePath("https://github.com/hwet-j/rego/assets/125880480/9f3df0be-3480-4567-b63a-c4a66421e8ee");
        } else if(dto.getType().equals("자유")) {
            dto.setImagePath("https://github.com/hwet-j/rego/assets/125880480/d1b61d9c-ac5a-4240-8773-d13352623445");
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

    @Override
    public int updateContentAndTypeAndNumberOfPeople(Integer planId, String content, String type, int numberOfPeople) {
        Planner planner = plannerRepository.findByPlanId(planId);
        int updatePlannerCnt =0;
        if (planner != null) {
            planner.setContent(content);
            planner.setType(type);
            planner.setNumberOfPeople(numberOfPeople);
            if(planner.getType().equals("힐링")){
                planner.setImagePath("https://github.com/hwet-j/rego/assets/125880480/9cd46f23-f82b-43e0-8657-ad0bf8620dda");
            } else if(planner.getType().equals("문화")) {
                planner.setImagePath("https://github.com/hwet-j/rego/assets/125880480/5ef1fea4-c460-4373-a5e4-afbc2d8fb7c4");
            } else if(planner.getType().equals("쇼핑")) {
                planner.setImagePath("https://github.com/hwet-j/rego/assets/125880480/419d0be8-f5f1-4dd8-8294-14ef15cc23cf");
            } else if(planner.getType().equals("식도락")) {
                planner.setImagePath("https://github.com/hwet-j/rego/assets/125880480/9f3df0be-3480-4567-b63a-c4a66421e8ee");
            } else if(planner.getType().equals("자유")) {
                planner.setImagePath("https://github.com/hwet-j/rego/assets/125880480/d1b61d9c-ac5a-4240-8773-d13352623445");
            }
            updatePlannerCnt++;
            // save 메서드를 호출하여 변경 사항을 데이터베이스에 바로 반영합니다.
            plannerRepository.save(planner);
        } else {
            throw new EntityNotFoundException("Planner with ID " + planId + " not found");
        }
        return updatePlannerCnt;
    }

    @Override
    public void changeIsComplete(Integer planId) {
        Planner planner = plannerRepository.findByPlanId(planId);
        planner.setIsWritten(1);
        plannerRepository.save(planner);
    }

    @Override
    public List<PlanCard> findAllPlanCard() {
        List<Planner> plannerList =  plannerRepository.findAll();

        List<PlanCard> planCardList = new ArrayList<>();

        for (Planner plan : plannerList) {
            int flight = detailPlanService.calculateTotalPriceForPlanWithFlight(plan.getPlanId());
            int withoutFlight = detailPlanService.calculateTotalPriceForPlanWithoutFlight(plan.getPlanId());
            List<User> voters = plannerRepository.findVotersByPlanId(plan.getPlanId());




            PlanCard planCard = new PlanCard();
            planCard.setPlanId(plan.getPlanId());
            planCard.setType(plan.getType());
            planCard.setContent(plan.getContent());
            planCard.setStartDate(plan.getStartDate());
            planCard.setEndDate(plan.getEndDate());
            planCard.setUserEmail(plan.getUser().getEmail());
            planCard.setNumberOfPeople(plan.getNumberOfPeople());
            planCard.setImagePath(plan.getImagePath());
            planCard.setFlightPrice(String.valueOf(flight));
            planCard.setWithoutFlightPrice(String.valueOf(withoutFlight));

            Set<String> voteEmails = new HashSet<>();
            if (voters != null) {
                for (User user : voters) {
                    voteEmails.add(user.getEmail());
                }
            }
            planCard.setVoter(voteEmails);

            planCardList.add(planCard);
        }

        return planCardList;
    }

    @Override
    public void vote(Integer planId,String email) {
        Optional<Planner> optionalPlanner = plannerRepository.findById(planId);
        Optional<User> optionalUser = userRepository.findByEmail(email);


        List<User> voters = plannerRepository.findVotersByPlanId(planId);

        Set<String> voteEmails = new HashSet<>();
        if (voters != null) {
            for (User user : voters) {
                voteEmails.add(user.getEmail());
            }
        }

        Planner planner = null;
        User user = null;
        if (optionalPlanner.isPresent()){
            planner = optionalPlanner.get();
        }
        if (optionalUser.isPresent()){
            user = optionalUser.get();
        }

        if(!voteEmails.contains(email)){
            System.out.println("11111111111");
            planner.getVoter().add(user);
            plannerRepository.save(planner);
        } else {
            System.out.println("22222222222");
            planner.getVoter().remove(user);
            plannerRepository.save(planner);
        }


    }

    @Override
    public List<Integer> userPlanVotes(String email) {
        List<Integer> planIds = plannerRepository.findAllPlanIds();

        List<Integer> list = new ArrayList<>();

        for(Integer id : planIds){
            List<User> voters = plannerRepository.findVotersByPlanId(id);

            Set<String> voteEmails = new HashSet<>();
            if (voters != null) {
                for (User user : voters) {
                    if(user.getEmail().equals(email)){
                        list.add(id);
                    }
                }
            }
        }

        return list;
    }

    @Override
    public List<PlanCard> findTop5PlanCard() {
        List<PlanCard> allPlanCard = findAllPlanCard();

        List<PlanCard> topFivePlanCards = allPlanCard.stream()
                .sorted((pc1, pc2) -> Integer.compare(pc2.getVoter().size(), pc1.getVoter().size())) // 크기 역순으로 정렬
                .limit(5) // 상위 5개 요소만 선택
                .collect(Collectors.toList()); // List로 변환



        return topFivePlanCards;
    }

    @Override
    public List<PlanCard> userPlanVotePlans(List<Integer> plans) {
        List<PlannerDTO> plannerDTOS = new ArrayList<>();

        for(Integer id : plans) {
            PlannerDTO dto = findById(id);
            plannerDTOS.add(dto);
        }

        List<PlanCard> planCardList = new ArrayList<>();

        for (PlannerDTO plan : plannerDTOS) {
            int flight = detailPlanService.calculateTotalPriceForPlanWithFlight(plan.getPlanId());
            int withoutFlight = detailPlanService.calculateTotalPriceForPlanWithoutFlight(plan.getPlanId());
            List<User> voters = plannerRepository.findVotersByPlanId(plan.getPlanId());


            PlanCard planCard = new PlanCard();
            planCard.setPlanId(plan.getPlanId());
            planCard.setType(plan.getType());
            planCard.setContent(plan.getContent());
            planCard.setStartDate(plan.getStartDate());
            planCard.setEndDate(plan.getEndDate());
            planCard.setUserEmail(plan.getUserEmail());
            planCard.setNumberOfPeople(plan.getNumberOfPeople());
            planCard.setImagePath(plan.getImagePath());
            planCard.setFlightPrice(String.valueOf(flight));
            planCard.setWithoutFlightPrice(String.valueOf(withoutFlight));

            Set<String> voteEmails = new HashSet<>();
            if (voters != null) {
                for (User user : voters) {
                    voteEmails.add(user.getEmail());
                }
            }
            planCard.setVoter(voteEmails);

            planCardList.add(planCard);
        }

        return planCardList;
    }

}