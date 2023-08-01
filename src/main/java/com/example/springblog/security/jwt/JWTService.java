package com.example.springblog.security.jwt;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JWTService {

    private Algorithm algorithm = Algorithm.HMAC256("SECRET SIGNING KEY (should be in env or config");



    public String createJWT(Long userId) {
        return createJWT(userId, 
                        new Date(),
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)
                        );
    }

    // Tests can be run on this function. As it is a protected method, users cannot access
    // this function.
    protected String createJWT(Long userId, Date iat, Date exp) {
        String token = JWT.create()
                        .withSubject(userId.toString())
                        .withIssuedAt(iat)
                        .withExpiresAt(exp)
                        .sign(algorithm);
        return token;
    }

    public Long getUserIdFromJWT(String jwt) {
        try {
            var verifier = JWT.require(algorithm).build();
            var decodedJWT = verifier.verify(jwt);
            var subject = decodedJWT.getSubject();
            return Long.parseLong(subject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
