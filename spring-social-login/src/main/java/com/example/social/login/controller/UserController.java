package com.example.social.login.controller;

import com.example.social.login.model.User;
import com.example.social.login.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("me")
    public User getCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        User user = userService.getUserById(oAuth2User.getName());
        return user;
    }
}
