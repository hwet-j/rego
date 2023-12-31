package com.clipclap.rego.model.entitiy;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;


/*

좋아요 (Like) 테이블

좋아요ID (likeId): 주요 식별자 (자동 생성)
회원 ID (userEmail): 외래 키로 연결된 사용자의 이메일 주소
관광지 ID (attractionId): 외래 키로 연결된 플랜의 식별자
추가시간 (addTime): 좋아요 추가 시각 (자동 생성)

 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="likeAttraction")
public class LikeAttraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeId;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "email")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "attractionId", referencedColumnName = "touristAttractionId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TouristAttraction attraction;

    @Column(nullable = false)
    private LocalDateTime addTime;



}
