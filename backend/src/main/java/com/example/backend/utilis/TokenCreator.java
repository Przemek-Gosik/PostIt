package com.example.backend.utilis;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.backend.constants.TokenConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

@Component
public class TokenCreator {

    @Value("${jwt.secret}")
    private String secret;

    public String createUserToken(String login){
        return JWT.create()
                .withSubject(login)
                .sign(Algorithm.HMAC512(secret));
    }

    public String getLoginFromToken(String token){
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token.replace(TokenConstants.PREFIX,""))
                .getSubject();
    }

}
