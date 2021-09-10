package com.example.social.login.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("oauth2")
public class OAuthController {
    private final static String KEY_TOKEN_COOKIE = "token";
    
    @GetMapping("callback/google")
    public ResponseEntity<Void> callback(HttpServletResponse response, @RequestParam("token") String token) {
        if (token != null) {
            response.addCookie(new Cookie(KEY_TOKEN_COOKIE, token));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
