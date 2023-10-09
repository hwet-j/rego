package com.clipclap.rego.model.entitiy;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;


/* 항공 예약

항공 예약 (Reservation) 테이블

항공예약ID (reservationId):             주요 식별자 (자동 생성)
예약회원 (userEmail):                  외래 키로 연결된 사용자의 이메일 주소
예약시간 (reservationTime):             예약 시각 (자동 생성)
출발지 (departure):                    출발지
도착지 (destination):                  도착지
좌석번호 (seatNumber):                  예약된 좌석 수
출국 시간 (departureTime):              출국 시간
입국 시간 (arrivalTime):                입국 시간
인원 (numberOfPassengers):            예약한 인원 수
금액 (amount):                        예약에 대한 총 금액
항공사 (airline):                      항공사 정보
운행정보 (operationInfo):              왕복, 편도, 다구간 등의 운행 정보
복귀항공예약번호 (returnReservationId):  왕복일 경우에만 존재
상태 (status):                        예약 상태

*/
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "userEmail", referencedColumnName = "email")
    private User userEmail;

    @CreationTimestamp
    private LocalDateTime reservationTime;

    @Column(nullable = false)
    private String departure;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private int seatNumber;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime  arrivalTime;

    @Column(nullable = false)
    private int numberOfPassengers;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String airline;

    @Column(nullable = false)
    private String operationInfo;

    private Integer returnReservationId;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.REMOVE)
    private List<Payment> Payments;

}