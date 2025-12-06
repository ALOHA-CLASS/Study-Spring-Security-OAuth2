package com.example.oauth2sociallogin.oauth2;

import com.example.oauth2sociallogin.entity.SocialProvider;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        SocialProvider provider = SocialProvider.fromRegistrationId(registrationId);
        return switch (provider) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            case NAVER -> new NaverOAuth2UserInfo(attributes);
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
        };
    }
}