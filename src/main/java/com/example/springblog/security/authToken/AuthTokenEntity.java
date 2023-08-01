package com.example.springblog.security.authToken;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import com.example.springblog.users.UserEntity;

@Entity(name="auth_tokens")
@Getter
@Setter
public class AuthTokenEntity {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
}
