package com.example.oauth2sociallogin.oauth2;

import com.example.oauth2sociallogin.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    private final String email;
    private final Role role;

    public CustomOAuth2User(OAuth2User oauth2User, String email, Role role) {
        this.oauth2User = oauth2User;
        this.email = email;
        this.role = role;
        System.out.println(oauth2User.getAttributes());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getKey()));
    }

    @Override
    public String getName() {
        String name = "";
        Object o = oauth2User.getAttributes().get("name");
        if ( o != null )
            name = o.toString();
        else {
            Map<String, Object> map1 = (Map<String, Object>) oauth2User.getAttributes();
            Map<String, Object> map2 = (Map<String, Object>) map1.get("properties");
            if (map2 != null)
                name = map2.get("nickname").toString();
            else {
                Map<String, Object> map3 = (Map<String, Object>)map1.get("response");
                name = map3.get("name").toString();
            }
        }
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
