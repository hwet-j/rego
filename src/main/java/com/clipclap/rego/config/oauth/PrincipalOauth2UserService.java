package com.clipclap.rego.config.oauth;

import java.util.Map;
import java.util.Optional;

import com.clipclap.rego.config.auth.PrincipalDetails;
import com.clipclap.rego.config.oauth.provider.GoogleUserInfo;
import com.clipclap.rego.config.oauth.provider.KakaoUserInfo;
import com.clipclap.rego.config.oauth.provider.NaverUserInfo;
import com.clipclap.rego.config.oauth.provider.OAuth2UserInfo;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	// userRequest 는 code를 받아서 accessToken을 응답 받은 객체
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회

		// code를 통해 구성한 정보
		// System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());
		// token을 통해 응답받은 회원정보
		// System.out.println("oAuth2User : " + oAuth2User);

		return processOAuth2User(userRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

		// Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
		OAuth2UserInfo oAuth2UserInfo = null;
		if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			log.info("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			log.info("카카오 로그인 요청");
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
			log.info("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
		} else {
			log.warn("구글,카카오,네이버 이외의 로그인 요청");
		}

		//System.out.println("oAuth2UserInfo.getProvider() : " + oAuth2UserInfo.getProvider());
		//System.out.println("oAuth2UserInfo.getProviderId() : " + oAuth2UserInfo.getProviderId());
		Optional<User> userOptional =
				userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

		// 이메일 정보로 회원 수
		long countUser = userRepository.countByEmail(oAuth2UserInfo.getEmail());

		User user;
		if (countUser == 1) {	// API가 달라도 동일한 email값이 들어오면 회원 가입 진행 X
			user = userRepository.findByEmail(oAuth2UserInfo.getEmail());
			log.info("회원가입 정보 존재");
		} else if (userOptional.isPresent()) {	// API로 부터 받아온 정보로 검색은 되었지만, email값이 없다면
			user = userOptional.get();
			// API값으로 검색된 정보에서 email 업데이트
			user.setEmail(oAuth2UserInfo.getEmail());
			userRepository.save(user);
			log.info("API 이메일 정보 변경");
		} else {  // 최초 정보 입력 (API정보도 이메일도 없을 경우) / 비밀번호 제외 설정
			user = User.builder()
					.username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
					.email(oAuth2UserInfo.getEmail())
					.role("ROLE_USER")
					.provider(oAuth2UserInfo.getProvider())
					.providerId(oAuth2UserInfo.getProviderId())
					.build();
			userRepository.save(user);
			log.info("회원가입 정보 없음");
		}

		return new PrincipalDetails(user, oAuth2User.getAttributes());
	}
}
