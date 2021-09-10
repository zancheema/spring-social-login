package com.example.social.login.config;

import com.example.social.login.security.RestAuthenticationEntryPoint;
import com.example.social.login.service.ApplicationOAuth2UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ApplicationOAuth2UserService applicationOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2Login(auth -> auth
                .authorizationEndpoint(endpoint -> endpoint
                    .baseUri("/oauth2/authorize")
                    .and()
                    .redirectionEndpoint(rEndpoint -> rEndpoint
                        .baseUri("/oauth2/callback/*")
                        .and()
                    .userInfoEndpoint(infoEndPoint -> infoEndPoint
                        .userService(applicationOAuth2UserService)))))
            .exceptionHandling(handle -> handle
                .authenticationEntryPoint(new RestAuthenticationEntryPoint()))
            .authorizeRequests(request -> request
                .antMatchers("/", "/error", "/oauth2/**").permitAll()
                .anyRequest().authenticated());
    }
}
