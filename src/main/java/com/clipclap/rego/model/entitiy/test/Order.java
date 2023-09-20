package com.clipclap.rego.model.entitiy.test;


import com.clipclap.rego.model.entitiy.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private LocalDateTime orderDateTime;
    private BigDecimal totalAmount;

    private String paymentStatus;

}
