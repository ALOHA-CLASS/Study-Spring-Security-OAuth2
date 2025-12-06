package com.example.oauth2sociallogin.oauth2;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties == null) {
            return null;
        }
        return (String) properties.get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null) {
            // 이메일이 없는 경우 카카오 ID를 기반으로 가상 이메일 생성
            return "kakao_" + getId() + "@kakao.local";
        }

        String email = (String) kakaoAccount.get("email");
        if (email == null || email.isEmpty()) {
            // 이메일이 없는 경우 카카오 ID를 기반으로 가상 이메일 생성
            return "kakao_" + getId() + "@kakao.local";
        }
        return email;
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties == null) {
            return null;
        }
        return (String) properties.get("profile_image");
    }
}
