package com.example.oauth2google.service;

import com.example.oauth2google.entity.User;
import com.example.oauth2google.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if ("google".equals(registrationId)) {
            return processGoogleUser(oauth2User);
        }

        return oauth2User;
    }

    private OAuth2User processGoogleUser(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        String id = attributes != null ? (String) attributes.get("sub") : "";
        String name = attributes != null ? (String) attributes.get("name") : "";
        String email = attributes != null ? (String) attributes.get("email") : "";
        String profileImageUrl = attributes != null ? (String) attributes.get("picture") : "";

        // 사용자 저장 또는 업데이트
        User user = userRepository.findById(id)
                .orElse(new User(id, name, email, profileImageUrl));

        user.setName(name);
        user.setEmail(email);
        user.setProfileImageUrl(profileImageUrl);

        userRepository.save(user);

        return new CustomOAuth2User(oauth2User.getAttributes(), user);
    }
}
