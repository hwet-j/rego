package com.clipclap.rego.service;

import com.clipclap.rego.mapper.DetailPlanMapper;
import com.clipclap.rego.model.dto.DetailPlanDTO;
import com.clipclap.rego.model.entitiy.PlannerDetail;
import com.clipclap.rego.repository.DetailPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        PlannerDetail planDetail = detailPlanMapper.dtoToEntity(detail);

        detailPlanRepository.save(planDetail);
    }

    @Override
    public List<DetailPlanDTO> findAllByPlan(Long planId) {
        List<PlannerDetail> entityList = detailPlanRepository.findAll();

        List<DetailPlanDTO> dtoList = entityList.stream()
                .map(DetailPlanMapper::entityToDto)
                .collect(Collectors.toList());


        return dtoList;
    }

    @Override
    public DetailPlanDTO findById(Integer id) {
        Optional<PlannerDetail> optionalPlannerDetail =  detailPlanRepository.findById(id);
        if(optionalPlannerDetail.isPresent()){
            DetailPlanDTO detail = detailPlanMapper.entityToDto(optionalPlannerDetail.get());
            return detail;
        } else {
            return null;
        }


    }
}
