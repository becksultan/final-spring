package com.web.router;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;


@Component
public class JwtUtil implements Serializable {
    private Key secret = MacProvider.generateKey();
    private static SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    public String decodeToken(String token) {
        return Jwts.parser().
                setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
