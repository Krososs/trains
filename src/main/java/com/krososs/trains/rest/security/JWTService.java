package com.krososs.trains.rest.security;

import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import com.krososs.trains.rest.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JWTService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    public String getAccessToken(User user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //24h
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
    }
}