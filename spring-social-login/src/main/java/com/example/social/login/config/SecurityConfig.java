package com.example.social.login.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.social.login.model.User;
import com.example.social.login.security.JwtAuthenticationFilter;
import com.example.social.login.security.RestAuthenticationEntryPoint;
import com.example.social.login.security.TokenProvider;
import com.example.social.login.service.ApplicationOAuth2UserService;
import com.example.social.login.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ApplicationOAuth2UserService applicationOAuth2UserService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(auth -> auth
                .authorizationEndpoint(endpoint -> endpoint
                    .baseUri("/oauth2/authorize")
                    .and()
                    .redirectionEndpoint(rEndpoint -> rEndpoint
                        .baseUri("/oauth2/callback/*")
                        .and()
                    .userInfoEndpoint(infoEndPoint -> infoEndPoint
                        .userService(applicationOAuth2UserService))
                    .successHandler(this::authenticationSuccessHandler))
                    .failureHandler(this::authenticationFailureHandler)))
            .exceptionHandling(handle -> handle
                .authenticationEntryPoint(new RestAuthenticationEntryPoint()))
            .authorizeRequests(request -> request
                .antMatchers("/", "/error", "/oauth2/**").permitAll()
                .anyRequest().authenticated());
    }

    private void authenticationSuccessHandler(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        String userId = ((OAuth2User) authentication.getPrincipal()).getName();

        User user = userService.getUserById(userId);
        String token = tokenProvider.createToken(user);

        String redirectUrl = UriComponentsBuilder
                .fromUriString("http://localhost:3000/oauth2/redirect")
                .queryParam("token", token)
                .toUriString();

        response.sendRedirect(redirectUrl);
    }

    private void authenticationFailureHandler(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authenticationexception) {
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
