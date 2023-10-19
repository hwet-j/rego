package com.clipclap.rego.service;

import com.clipclap.rego.mapper.DetailPlanMapper;
import com.clipclap.rego.model.dto.DetailPlanDTO;
import com.clipclap.rego.model.dto.PreviewDTO;
import com.clipclap.rego.model.entitiy.City;
import com.clipclap.rego.model.entitiy.PlannerDetail;
import com.clipclap.rego.repository.DetailPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DetailPlanServiceImpl implements DetailPlanService {

    private final DetailPlanRepository detailPlanRepository;
    private final DetailPlanMapper detailPlanMapper;


    @Override
    public void makeDetailPlan(DetailPlanDTO detail) {
        PlannerDetail planDetail = DetailPlanMapper.dtoToEntity(detail);

        detailPlanRepository.save(planDetail);
    }

    @Override
    public List<DetailPlanDTO> findAllByPlan(Integer planId) {
        List<PlannerDetail> entityList = detailPlanRepository.findByPlan_PlanId(planId);

        List<DetailPlanDTO> dtoList = entityList.stream()
                .map(DetailPlanMapper::entityToDto)
                .collect(Collectors.toList());

        return dtoList;
    }
    @Override
    public List<DetailPlanDTO> findByPlanPlanIdOrderByStartTime(Integer planId) {
        List<PlannerDetail> entityList = detailPlanRepository.findByPlanPlanIdOrderByStartTime(planId);

        List<DetailPlanDTO> dtoList = entityList.stream()
                .map(DetailPlanMapper::entityToDto)
                .collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public List<PreviewDTO> findPreview(Integer planId) {
        List<Object[]> results=detailPlanRepository.findCityNameAndImageByPlanId(planId);
        List<PreviewDTO> dtoList= new ArrayList<>();
        for (Object[] result : results) {
            PreviewDTO previewDTO = new PreviewDTO();
            previewDTO.setCityName(((City)result[0]).getCityName());
            previewDTO.setTourAttractionName(result[1].toString());
            previewDTO.setImage(result[2].toString());
            previewDTO.setStartTime((LocalDateTime)result[3]);
            previewDTO.setEndTime((LocalDateTime)result[4]);
            previewDTO.setDetailPlanId(result[5].toString());

            dtoList.add(previewDTO);
        }
            return dtoList;
            }

    @Override
    public DetailPlanDTO findById(Integer id) {
        Optional<PlannerDetail> optionalPlannerDetail =  detailPlanRepository.findById(id);
        if(optionalPlannerDetail.isPresent()){
            DetailPlanDTO detail = DetailPlanMapper.entityToDto(optionalPlannerDetail.get());
            return detail;
        } else {
            return null;
        }
    }

    @Override
    public Integer findMaxDetailPlanIdByPlanId(Integer planId) {
        Integer id = detailPlanRepository.findMaxDetailPlanIdByPlanId(planId);
        if(id == null){
            return 0;
        } else {
            return id;
        }
    }

    @Override
    public int updateStartTimeAndEndTime(Integer planId, long daysBetween) {
        int updateCnt =0;
        List<PlannerDetail> plannerDetailList= detailPlanRepository.findByPlan_PlanId(planId);
        for(PlannerDetail plannerDetail : plannerDetailList){
            plannerDetail.setStartTime(plannerDetail.getStartTime().plusDays(daysBetween));
            plannerDetail.setEndTime((plannerDetail.getEndTime().plusDays(daysBetween)));
            detailPlanRepository.save(plannerDetail);
            updateCnt++;
        }
        return updateCnt;
    }


    @Override
    public Integer calculateTotalPriceForPlanWithFlight(Integer planId) {
        Integer flightPrice = detailPlanRepository.calculateTotalPriceForPlanWithFlight(planId);
        if (flightPrice == null){
            return 0;
        } else {
            return flightPrice;
        }

    }


    @Override
    public Integer calculateTotalPriceForPlanWithoutFlight(Integer planId) {
        Integer withoutFlightPrice = detailPlanRepository.calculateTotalPriceForPlanWithoutFlight(planId);

        if (withoutFlightPrice == null){
            return 0;
        } else {
            return withoutFlightPrice;
        }
    }


}
