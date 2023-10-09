package com.clipclap.rego.model.dto;

import lombok.*;

import java.time.LocalDateTime;


@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private Integer userId;
    private String email;
    private String password;
    private String nickname;
    private String gender;
    private String birthDate;
    private LocalDateTime createDate;
    private String role; 			// ROLE_USER, ROLE_ADMIN 등 역할
    private Boolean withdrawalRequest;

}
