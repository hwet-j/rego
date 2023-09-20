package com.clipclap.rego.model.entitiy.test;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    private LocalDateTime paymentDateTime;
    private BigDecimal paymentAmount;
    private String paymentMethod;
    private String paymentStatus;

    // Getter와 Setter 메서드 추가
}
