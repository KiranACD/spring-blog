package com.example.springblog.security.jwt;

import java.util.Date;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JWTServiceTests {
    
    private JWTService jwtService = new JWTService();
    
    @Test
    void canCreateJWTFromUserId(){
        Long userId = Long.parseLong("1122");
        var jwt = jwtService.createJWT(userId, new Date(0), new Date(1677687592));
        assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMTIyIiwiZXhwIjoxNjc3Njg3LCJpYXQiOjB9.f7ig9fq0qcIWMtUu4foVuAUkK0Hb5gLvL96R-eePWsc",
                    jwt
                    );
    }

    @Test
    void canVerifyJWT() {
        Long userId = Long.parseLong("1122");
        var jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMTIyIiwiZXhwIjoxNjc3NzYxNjg2LCJpYXQiOjE2NzcxNTY4ODZ9.K9g0sK-XMFwI02SZyJy6Km42EhPAfiT--uuun1CGU9w";
        var userIdFromJWT = jwtService.getUserIdFromJWT(jwt);
        assertEquals(userId, userIdFromJWT);
    }
}
