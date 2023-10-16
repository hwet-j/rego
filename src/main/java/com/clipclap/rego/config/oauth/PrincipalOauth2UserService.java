package com.clipclap.rego.config.oauth;

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

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		log.info("oAuth2User : " + oAuth2User);
		log.info("userRequest clientRegistration : " + userRequest.getClientRegistration());

		return processOAuth2User(userRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

		OAuth2UserInfo oAuth2UserInfo = null;
		if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			log.info("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			log.info("카카오 로그인 요청");
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			log.info("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
		} else {
			log.warn("구글,카카오,네이버 이외의 로그인 요청");
		}

		Optional<User> userOptional = userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
		long countUser = userRepository.countByEmail(oAuth2UserInfo.getEmail());

		Optional<User> user;
		User userInfo;
		if (countUser == 1) {
			user = userRepository.findByEmail(oAuth2UserInfo.getEmail());
			userInfo = user.get();
			if(userInfo.getUserprofile() == null){
				userInfo.setUserprofile(oAuth2UserInfo.getUserProfile());
				userRepository.save(userInfo);
			}
			log.info("회원가입 정보 존재");
		} else if (userOptional.isPresent()) {
			userInfo = userOptional.get();
			userInfo.setEmail(oAuth2UserInfo.getEmail());
			userInfo.setUserprofile(oAuth2UserInfo.getUserProfile());
			userRepository.save(userInfo);
			log.info("API 이메일 정보 변경");
		} else {
			userInfo = User.builder()
					.username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
					.email(oAuth2UserInfo.getEmail())
					.role("ROLE_USER")
					.provider(oAuth2UserInfo.getProvider())
					.providerId(oAuth2UserInfo.getProviderId())
					.userprofile(oAuth2UserInfo.getUserProfile()) // 여기에 프로필 사진 정보 추가
					.build();
			userRepository.save(userInfo);
			log.info("회원가입 정보 없음");
		}
		String profileImageUrl = oAuth2UserInfo.getUserProfile();
		if (profileImageUrl == null || profileImageUrl.isEmpty()) {
			profileImageUrl = "https://github.com/uki95/image/assets/138668659/8a1bc0cc-2299-490b-897c-043c2d9e57bb";  // 대체 이미지 URL
		}

		if (countUser == 1) {
			user = userRepository.findByEmail(oAuth2UserInfo.getEmail());
			userInfo = user.get();
			log.info("회원가입 정보 존재");
		} else if (userOptional.isPresent()) {
			userInfo = userOptional.get();
			userInfo.setEmail(oAuth2UserInfo.getEmail());
			userRepository.save(userInfo);
			log.info("API 이메일 정보 변경");
		} else {
			userInfo = User.builder()
					.username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
					.email(oAuth2UserInfo.getEmail())
					.role("ROLE_USER")
					.provider(oAuth2UserInfo.getProvider())
					.providerId(oAuth2UserInfo.getProviderId())
					.userprofile(profileImageUrl) // 대체 이미지 로직 적용
					.build();
			userRepository.save(userInfo);
			log.info("회원가입 정보 없음");
		}

		return new PrincipalDetails(userInfo, oAuth2User.getAttributes());
	}
}
