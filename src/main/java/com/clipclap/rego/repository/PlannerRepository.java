package com.clipclap.rego.repository;

import com.clipclap.rego.model.entitiy.Planner;
import com.clipclap.rego.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerRepository extends JpaRepository<Planner, Integer> {

    Planner findByPlanId(Integer planId);

    List<Planner> findByUserEmail(User userEmail);
}
