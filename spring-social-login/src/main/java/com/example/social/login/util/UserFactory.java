package com.example.social.login.util;

import com.example.social.login.model.User;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserFactory {
    private final Object provider;

    public UserFactory(Object provider) {
        this.provider = provider;
    }

    public User create() {
        User user = new User();
        if (provider instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) provider;
            user.setId(oAuth2User.getAttribute("sub"));
            user.setEmail(oAuth2User.getAttribute("email"));
            user.setName(oAuth2User.getAttribute("name"));
            user.setImgUrl(oAuth2User.getAttribute("picture"));
        } else {
            throw new IllegalStateException("No factory implementation for provided object yet.");
        }
        return user;
    }
}
