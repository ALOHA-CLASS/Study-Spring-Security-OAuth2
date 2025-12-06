package com.example.oauth2google.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
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

    @Override
    public String toString() {
        return "User{" +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", role=" + role +
                '}';
    }

    public enum Role {
        USER, ADMIN
    }
}


