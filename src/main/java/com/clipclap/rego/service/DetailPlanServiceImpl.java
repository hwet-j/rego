package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.DetailPlanDTO;
import com.clipclap.rego.repository.DetailPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DetailPlanServiceImpl implements DetailPlanService {

    private final DetailPlanRepository detailPlanRepository;

    @Override
    public void makeDetailPlan(DetailPlanDTO detail) {


        //detailPlanRepository.save();
    }
}
