package com.example.oauth2kakao.service;

import com.example.oauth2kakao.entity.User;
import com.example.oauth2kakao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String accessToken = userRequest.getAccessToken().getTokenValue();
        System.out.println("access 토큰 : " + accessToken);

        ClientRegistration cr = userRequest.getClientRegistration();
        System.out.println("ClientRegistration 객체 정보 : " + cr);
        String registrationId = cr.getRegistrationId();

        if ("kakao".equals(registrationId)) {
            return processKakaoUser(oauth2User);
        }

        return oauth2User;
    }

    private OAuth2User processKakaoUser(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        String kakaoId = String.valueOf(attributes.get("id"));

        // Kakao 사용자 정보 추출
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String nickname = properties != null ? (String) properties.get("nickname") : "";
        String profileImageUrl = properties != null ? (String) properties.get("profile_image") : "";
        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : "";

        // 사용자 저장 또는 업데이트
        User user = userRepository.findByKakaoId(kakaoId)
                .orElse(new User(kakaoId, nickname, email, profileImageUrl));

        user.setNickname(nickname);
        user.setEmail(email);
        user.setProfileImageUrl(profileImageUrl);

        userRepository.save(user);

        return new CustomOAuth2User(oauth2User.getAttributes(), user);
    }
}
