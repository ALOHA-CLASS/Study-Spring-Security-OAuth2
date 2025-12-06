package com.example.oauth2sociallogin.repository;

import com.example.oauth2sociallogin.entity.User;
import com.example.oauth2sociallogin.entity.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndProviderId(SocialProvider provider, String providerId);
    boolean existsByEmail(String email);
}

