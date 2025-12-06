package com.example.oauth2naver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numId;
    private String id;
    private String name;
    private String email;
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private Role role;

    // 생성자
    public User() {}

    public User(String id, String name, String email, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = Role.USER;
    }

    // Getter, Setter
    public Long getNumId() { return numId; }
    public void setNumId(Long numId) { this.numId = numId; }

    public String getId() { return id; }
    public void setId(String naverId) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public enum Role {
        USER, ADMIN
    }

    @Override
    public String toString() {
        return "User{" +
                "numId=" + numId +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", role=" + role +
                '}';
    }
}

