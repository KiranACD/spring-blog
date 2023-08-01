package com.example.springblog.security.authToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthTokenAuthentication implements Authentication {

    private String token;
    private Long userId;

    public AuthTokenAuthentication(String token) {
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

    

