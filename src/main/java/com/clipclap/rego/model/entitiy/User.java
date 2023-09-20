package com.clipclap.rego.model.entitiy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDate;


@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User {
	@Id 							// primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// 자동증가
	private Integer userId;
	private String password;
	private String nickname;
	private String email;
	private String gender;
	private LocalDate birthDate;


	private String role; 			// ROLE_USER, ROLE_ADMIN 등 역할
	private String username;		// UserDetails 라는 클래스를 상속받고 있어서 사용되어야함 -> API 정보가 들어감
	// OAuth를 위해 구성한 추가 필드 2개
	private String provider;
	private String providerId;
	@CreationTimestamp				// 최초 생성시 현재시간을 기준으로 자동입력
	private Timestamp createDate;


	public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(plainPassword, this.password);
	}
}
