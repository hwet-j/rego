package com.clipclap.rego.model.entitiy;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/*

결제 (Payment) 테이블

결제ID (paymentId):            주요 식별자 (자동 생성)
예약ID (reservationId):        외래 키로 연결된 예약의 식별자
결제일시 (paymentTime):         결제 시각 (자동 생성)
결제금액 (paymentAmount):       결제 금액
결제수단 (paymentMethod):       결제 수단 (예: 신용카드, 현금 등)
결제상태 (paymentStatus):       결제 상태

*/
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "reservationId")
    private Reservation reservation;

    private LocalDateTime paymentTime;
    private BigDecimal paymentAmount;
    private String paymentMethod;
    private String paymentStatus;

}
