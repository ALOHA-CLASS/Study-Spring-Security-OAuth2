package com.example.oauth2kakao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String kakaoId;

    private String nickname;
    private String email;
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 생성자
    public User() {}

    public User(String kakaoId, String nickname, String email, String profileImageUrl) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = Role.USER;
    }

    // Getter, Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKakaoId() { return kakaoId; }
    public void setKakaoId(String kakaoId) { this.kakaoId = kakaoId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public enum Role {
        USER, ADMIN
    }
}

