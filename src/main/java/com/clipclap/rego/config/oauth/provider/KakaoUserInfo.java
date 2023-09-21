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
}