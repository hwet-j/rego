package com.clipclap.rego.model.entitiy;

import jakarta.persistence.*;
import lombok.*;


/*

관광지 (TouristAttraction) 테이블

관광지ID (touristAttractionId):     주요 식별자 (자동 생성)
주소 (address):                    관광지의 주소
관광지명 (name):                    관광지의 이름
이미지 (image):                     관광지 이미지
소개 (introduction):               관광지 소개
위도 (latitude):                   관광지의 위도
경도 (longitude):                  관광지의 경도
콘텐츠타입 (contentType):            관광지 콘텐츠 유형

 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "touristAttraction")
public class TouristAttraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer touristAttractionId;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String name;

    private String image;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "cityName", referencedColumnName = "cityName")
    private City cityName ;

}