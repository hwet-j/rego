package com.clipclap.rego.model.dto;

import lombok.*;


@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private String nickname;
	private String email;
	private String role; 			// ROLE_USER, ROLE_ADMIN 등 역할
	private String memberImage;
	private String password;
}
