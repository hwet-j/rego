package com.clipclap.rego.repository;

import com.clipclap.rego.model.entitiy.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerRepository extends JpaRepository<Planner, Integer> {

    Planner findByPlanId(Integer planId);

}
