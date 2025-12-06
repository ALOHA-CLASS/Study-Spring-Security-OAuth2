package com.example.resource_server.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {

    @GetMapping("/api/public")
    public String publicEndpoint() {
        System.out.println("***public***");
        return "인증과정 필요없이 누구든 접근할 수 있어요.^^";
    }

    @GetMapping("/api/private")
    public String privateEndpoint() {
        System.out.println("***private***");
        return "인증을 거치고 인가 과정을 통해서 받은 액세스 토큰이 있어야 접근할 수 있어요!!";
    }

    @GetMapping("/api/privatejwt")
    public String privateEndpoint(@AuthenticationPrincipal Jwt jwt) {
        System.out.println("***privatejwt***");
        System.out.println("전달받은 JWT 객체 : "+jwt);
        return "반가워요~~" + jwt.getSubject() + "사용자님..., 접근할 수 있는 리소스(데이터 또는 기능)의 범위 : " + jwt.getClaimAsStringList("scope");
    }
}
