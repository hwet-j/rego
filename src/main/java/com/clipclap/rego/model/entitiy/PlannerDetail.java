package com.clipclap.rego.model.entitiy;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/*

세부 플래너 (DetailPlan) 테이블

세부플랜ID (detailPlanId):      주요 식별자 (자동 생성)
플랜 ID (planId):             외래 키로 연결된 플랜의 식별자
내용/설명 (content):            세부 플랜에 대한 내용 또는 설명
시작시간 (startTime):           세부 플랜의 시작 시간
종료시간 (endTime):             세부 플랜의 종료 시간
관광지 (touristAttraction):     관광지 정보

*/

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "detailPlan")
public class PlannerDetail {

    @Id
    private Integer detailPlanId;

    @ManyToOne
    @JoinColumn(name = "planId", referencedColumnName = "planId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Planner plan;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private boolean allday;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "touristAttraction", referencedColumnName = "touristAttractionId")
    private TouristAttraction touristAttraction;

}




