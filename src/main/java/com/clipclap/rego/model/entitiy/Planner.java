package com.clipclap.rego.model.entitiy;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/*

플래너 (Planner) 테이블

플래너ID (plannerId):          주요 식별자 (자동 생성)
작성자 (userEmail):           외래 키로 연결된 사용자의 이메일 주소
내용/설명 (content):            플래너에 대한 내용 또는 설명
여행시작일자 (startDate):       여행 시작 일자
여행종료일자 (endDate):         여행 종료 일자
종류 (type):                  관광지, 이동수단, 식당 등의 플래너 종류

*/



@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "planner")
public class Planner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    @ManyToOne
    @JoinColumn(name = "userEmail", referencedColumnName = "email")
    private User userEmail;

    private String content;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int numberOfPeople;

    @Column(nullable = false)
    private String type;

}