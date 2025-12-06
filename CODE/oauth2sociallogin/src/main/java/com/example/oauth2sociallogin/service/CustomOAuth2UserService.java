package com.example.oauth2sociallogin.service;

import com.example.oauth2sociallogin.entity.Role;
import com.example.oauth2sociallogin.entity.SocialProvider;
import com.example.oauth2sociallogin.entity.User;
import com.example.oauth2sociallogin.oauth2.CustomOAuth2User;
import com.example.oauth2sociallogin.oauth2.OAuth2UserInfo;
import com.example.oauth2sociallogin.oauth2.OAuth2UserInfoFactory;
import com.example.oauth2sociallogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oauth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException("OAuth2 사용자 처리 중 오류가 발생했습니다: " + ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oauth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oauth2User.getAttributes());

        String email = oauth2UserInfo.getEmail();
        if (!StringUtils.hasText(email)) {
            throw new OAuth2AuthenticationException("OAuth2 provider로부터 이메일을 찾을 수 없습니다.");
        }

        SocialProvider provider = SocialProvider.fromRegistrationId(registrationId);

        // 카카오의 경우 providerId와 이메일로 사용자 찾기
        Optional<User> userOptional;
        if (provider == SocialProvider.KAKAO) {
            // 먼저 providerId로 찾기
            userOptional = userRepository.findByProviderAndProviderId(provider, oauth2UserInfo.getId());
            // 없으면 이메일로 찾기 (기존 사용자가 있을 수 있음)
            if (userOptional.isEmpty()) {
                userOptional = userRepository.findByEmail(email);
                // 이메일로 찾은 사용자가 있고 다른 provider라면 새 사용자로 처리
                if (userOptional.isPresent() && !userOptional.get().getProvider().equals(provider)) {
                    userOptional = Optional.empty();
                }
            }
        } else {
            userOptional = userRepository.findByProviderAndProviderId(provider, oauth2UserInfo.getId());
        }

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user = updateExistingUser(user, oauth2UserInfo);
        } else {
            user = registerNewUser(oauth2UserInfo, provider);
        }

        return new CustomOAuth2User(oauth2User, user.getEmail(), user.getRole());
    }

    private User registerNewUser(OAuth2UserInfo oauth2UserInfo, SocialProvider provider) {
        String email = oauth2UserInfo.getEmail();

        // 카카오의 경우 가상 이메일을 사용하므로 중복 체크 후 유니크하게 만들기
        if (provider == SocialProvider.KAKAO && email.endsWith("@kakao.local")) {
            int counter = 1;
            String originalEmail = email;
            while (userRepository.existsByEmail(email)) {
                email = originalEmail.replace("@kakao.local", "_" + counter + "@kakao.local");
                counter++;
            }
        }

        User user = new User(
                email,
                oauth2UserInfo.getName(),
                oauth2UserInfo.getImageUrl(),
                Role.USER,
                provider,
                oauth2UserInfo.getId()
        );

        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oauth2UserInfo) {
        existingUser.update(oauth2UserInfo.getName(), oauth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}