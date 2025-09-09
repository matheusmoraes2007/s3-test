package com.tech.s3test.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tech.s3test.configuration.security.jwt.JwtProperties;
import com.tech.s3test.dto.res.JwtResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    public JwtResDto generateToken(String email) {
        Instant expiresAt = this.getExpirationDate();
        String token = JWT.create()
                .withIssuer(jwtProperties.getIssuer())
                .withSubject(email)
                .withExpiresAt(expiresAt)
                .sign(this.getAlgorithm());
        return new JwtResDto(
                jwtProperties.getPrefix(),
                token,
                expiresAt.toEpochMilli()
        );
    }

    public String validateToken(String token){
        return JWT.require(this.getAlgorithm())
                .withIssuer(jwtProperties.getIssuer())
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant getExpirationDate() {
        return Instant.now().plus(jwtProperties.getExpiration(), ChronoUnit.HOURS);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtProperties.getSecret());
    }
}
