package com.example.social.login.service;

import com.example.social.login.model.User;
import com.example.social.login.repository.UserRepository;
import com.example.social.login.util.UserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * This service uses the default oauth2 authentication functionality
 * with the addition of saving the user to the datasource
 */
@Service
public class ApplicationOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        User user = new UserFactory(oAuth2User).create();
        userRepository.save(user);
        return oAuth2User;
    }
}
