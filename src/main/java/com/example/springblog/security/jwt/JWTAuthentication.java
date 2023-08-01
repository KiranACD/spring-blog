package com.example.springblog.security.jwt;

import com.example.springblog.users.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTAuthentication implements Authentication {

    private String token;
    private Long userId;

    public JWTAuthentication(String token) {
        this.token = token;
    }

    public String getCredentials() {
        return token;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Principal being authenticated can be all sorts of entities. It is just not a user.
    public Long getPrincipal() {
        return userId;
    }

    public boolean isAuthenticated() {
        return (userId != null);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    public String getName() {
        return null;
    }

    public Object getDetails() {
        return null;
    }



    
}
