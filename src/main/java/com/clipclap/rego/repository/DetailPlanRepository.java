package com.clipclap.rego.repository;

import com.clipclap.rego.model.entitiy.PlannerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailPlanRepository extends JpaRepository<PlannerDetail, Integer> {

    void deleteByPlanPlanIdAndDetailPlanId(Integer planId, Integer detailPlanId);

    @Query(value = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'planner' AND TABLE_NAME = 'detailPlan'", nativeQuery = true)
    Integer findNextAutoIncrementValue();

    @Query("SELECT MAX(p.detailPlanId) FROM PlannerDetail p WHERE p.plan.planId = :planId")
    Integer findMaxDetailPlanIdByPlanId(@Param("planId") Integer planId);

    List<PlannerDetail> findByPlan_PlanId(Integer planId);

    List<PlannerDetail> findByPlanPlanIdOrderByStartTime(Integer planId);

    @Query("SELECT ta.cityName, ta.name, ta.image, dp.startTime, dp.endTime, dp.detailPlanId " +
            "FROM PlannerDetail dp " +
            "JOIN dp.touristAttraction ta WHERE dp.plan.planId = :planId" +
            " ORDER BY dp.startTime")
    List<Object[]> findCityNameAndImageByPlanId(@Param("planId") Integer planId);

}
