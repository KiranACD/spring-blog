package com.example.springblog.security.authToken;

import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.stereotype.Service;

import com.example.springblog.users.UserEntity;

import java.util.UUID;
import java.util.Optional;

@Service
public class AuthTokenService {
    
    private final AuthTokenRepository authTokenRepository;

    public AuthTokenService(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    private UUID getTokenFromUser(UserEntity user) {
        //Long userId = user.getId();
        Optional<AuthTokenEntity> authTokenOptional = authTokenRepository.findByUser(user);
        if (authTokenOptional.isPresent()) {
            return authTokenOptional.get().getId();
        }
        return null;
    }

    public UUID createToken(UserEntity user) {
        
        Optional<AuthTokenEntity> authTokenOptional = authTokenRepository.findByUser(user);
        if (authTokenOptional.isPresent()) {
            return authTokenOptional.get().getId();
        }
        AuthTokenEntity authTokenEntity = new AuthTokenEntity();
        authTokenEntity.setUser(user);
        var savedAuthToken = authTokenRepository.save(authTokenEntity);
        return savedAuthToken.getId(); 
    }

    public Long getUserIdFromAuthToken(UUID token) {
        var savedAuthToken = authTokenRepository.findById(token)
                                .orElseThrow(() -> new BadCredentialsException("Invalid Authentication Token"));
        
        return savedAuthToken.getUser().getId();
        

    }
}
