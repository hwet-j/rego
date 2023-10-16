package com.clipclap.rego.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes;
	
    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
	
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) ((Map<?, ?>) attributes.get("kakao_account")).get("email");

    }

	@Override
	public String getProvider() {
		return "kakao";
	}


    @Override
    public String getUserProfile() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                return (String) profile.get("profile_image_url");
            }
        }
        return null;
    }
}
