package com.example.springblog.security.authToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springblog.users.UserEntity;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity, UUID> {

    Optional<AuthTokenEntity> findByUser(UserEntity user);
    
}
