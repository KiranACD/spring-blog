package com.example.springblog.security.jwt;

import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class JWTAuthenticationFilter extends AuthenticationFilter {

    public JWTAuthenticationFilter(JWTService jwtService) {
        super(new JWTAuthenticationManager(jwtService), new JWTAuthenticationConverter());

        setSuccessHandler((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }

    static class JWTAuthenticationManager implements AuthenticationManager {

        // private JWTService jwtService = new JWTService();
        private JWTService jwtService;

        public JWTAuthenticationManager(JWTService jwtService) {
            this.jwtService = jwtService;
        }

        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            if (authentication instanceof JWTAuthentication) {
                JWTAuthentication jwtAuthentication = (JWTAuthentication) authentication;
                String token = jwtAuthentication.getCredentials();
                
                if (token != null) {
                    var userId = jwtService.getUserIdFromJWT(token);
                    if (userId != null) {
                        jwtAuthentication.setUserId(userId);
                        return jwtAuthentication;
                    } 
                }
            }
            return null;
        }

    }

    static class JWTAuthenticationConverter implements AuthenticationConverter {
        public Authentication convert(HttpServletRequest request) {
            if (request.getHeader("Authorization") != null) {
                String token = request.getHeader("Authorization").split(" ")[1];
                return new JWTAuthentication(token) ;
            }
            return null;
        }
    }
    
}
