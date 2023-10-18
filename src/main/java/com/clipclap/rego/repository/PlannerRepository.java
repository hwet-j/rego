package com.clipclap.rego.repository;

import com.clipclap.rego.model.entitiy.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface PlannerRepository extends JpaRepository<Planner, Integer> {

    Planner findByPlanId(Integer planId);

    List<Planner> findByUserEmail_Email(String email);

    List<Planner> findTop4ByOrderByPlanIdDesc();

    @Query("SELECT MAX(p.planId) FROM Planner p")
    Integer findLatestPlanId();

    @Modifying
    @Query("INSERT INTO Planner (user, content, startDate, endDate, numberOfPeople, type, imagePath) VALUES (:#{#planner.user}, :#{#planner.content}, :#{#planner.startDate}, :#{#planner.endDate}, :#{#planner.numberOfPeople}, :#{#planner.type}, :#{#planner.imagePath})")
    @Transactional
    void savePlannerAndFlush(@Param("planner") Planner planner);

    @Modifying
    @Transactional
    @Query("UPDATE Planner p SET p.startDate = :startDate, p.endDate = :endDate WHERE p.planId = :planId")
    void updateStartDateAndEndDate(@Param("planId") Integer planId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}

