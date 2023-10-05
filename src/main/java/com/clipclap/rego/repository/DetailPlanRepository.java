package com.clipclap.rego.repository;

import com.clipclap.rego.model.entitiy.PlannerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DetailPlanRepository extends JpaRepository<PlannerDetail, Integer> {
    @Query("SELECT MAX(d.detailPlanId) FROM PlannerDetail d")
    Long findMaxDetailPlanId();

    void deleteByDetailPlanId(Integer detailPlanId);

    @Query(value = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'planner' AND TABLE_NAME = 'detailPlan'", nativeQuery = true)
    Long findNextAutoIncrementValue();

}
