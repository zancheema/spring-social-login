package com.example.social.login.security;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.social.login.model.User;
import com.example.social.login.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")) {
            String token = authHeader.split(" ")[1];
            String userId = tokenProvider.getIdFromToken(token);

            User user = userService.getUserById(userId);

            OAuth2User oAuth2User = new OAuth2User(){

                @Override
                public Map<String, Object> getAttributes() {
                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("sub", user.getId());
                    attributes.put("name", user.getName());
                    attributes.put("email", user.getEmail());
                    attributes.put("picture", user.getImgUrl());
                    return attributes;
                }

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
                }

                @Override
                public String getName() {
                    return user.getId();
                }
            };


            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(oAuth2User, null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
