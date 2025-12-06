package com.example.oauth2sociallogin.entity;

public enum SocialProvider {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    private final String registrationId;

    SocialProvider(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public static SocialProvider fromRegistrationId(String registrationId) {
        for (SocialProvider provider : values()) {
            if (provider.registrationId.equals(registrationId)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Unknown registration id: " + registrationId);
    }
}
