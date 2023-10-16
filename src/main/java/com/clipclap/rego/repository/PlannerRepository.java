package com.clipclap.rego.repository;

import com.clipclap.rego.model.entitiy.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerRepository extends JpaRepository<Planner, Integer> {

    Planner findByPlanId(Integer planId);

    List<Planner> findByUserEmail_Email(String email);

    List<Planner> findTop4ByOrderByPlanIdDesc();
}
