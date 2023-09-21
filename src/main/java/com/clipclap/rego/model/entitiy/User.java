package com.clipclap.rego.model.entitiy;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;


/*

User 테이블

userId 				사용자의 고유 식별자 (자동 생성)
password 			비밀번호 (암호화된 형태로 저장)
nickname  			별명
email 				이메일 주소 (API를 통해서 받음)
gender 				성별
birthDate 			생년월일
role 				사용자의 역할 (예: ROLE_USER, ROLE_ADMIN..)
withdrawalRequest  	탈퇴요청 (기본값: False)
username  			Spring Security에서 사용되는 사용자명
provider  			OAuth 공급자 (예: Google, Naver..)
providerId  		OAuth 공급자에서 제공하는 사용자 고유 ID
createDate  		사용자 계정의 생성 일자 및 시간 (API로 연결된 시간, 자동 생성)

*/


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

	@Column(unique = true, nullable = false)
	private String email;

	private String gender;
	private LocalDate birthDate;
	private Boolean withdrawalRequest;

	private String role; 			// ROLE_USER, ROLE_ADMIN 등 역할
	private String username;		// UserDetails 라는 클래스를 상속받고 있어서 사용되어야함 -> API 정보가 들어감
	// OAuth를 위해 구성한 추가 필드 2개
	private String provider;
	private String providerId;
	@CreationTimestamp				// 최초 생성시 현재시간을 기준으로 자동입력
	private LocalDateTime createDate;


	public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(plainPassword, this.password);
	}
}
