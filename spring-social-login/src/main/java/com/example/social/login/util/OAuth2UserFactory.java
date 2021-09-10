package com.example.social.login.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.social.login.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserFactory {
    private final Object provider;

    public OAuth2UserFactory(Object provider) {
        this.provider = provider;
    }

    public OAuth2User create() {
        OAuth2User user;
        if (provider instanceof User) {
            user = new CustomOAuth2User((User) provider);
        } else {
            throw new IllegalStateException("Factory not implemented for provider yet.");
        }
        return user;
    }

    private static class CustomOAuth2User implements OAuth2User {
        private HashMap<String, Object> attributes;
        private List<SimpleGrantedAuthority> authorities;
        private String name;

        public CustomOAuth2User(User user) {
            this.attributes = new HashMap<>();
            this.attributes.put("sub", user.getId());
            this.attributes.put("email", user.getEmail());
            this.attributes.put("name", user.getName());
            this.attributes.put("picture", user.getImgUrl());

            this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

            this.name = user.getId();
        }

        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
