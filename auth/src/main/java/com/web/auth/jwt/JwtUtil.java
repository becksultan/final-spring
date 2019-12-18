package com.web.auth.jwt;

import com.web.auth.entity.User;
import com.web.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.util.Base64;

@Component
public class JwtUtil implements Serializable {
    private Key secret = MacProvider.generateKey();
    private static SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    private UserRepository userRepository;

   public JwtUtil(UserRepository userRepository) {
       this.userRepository = userRepository;
   }

   public String createToken(User user) {
       return Jwts.builder().setSubject(user.getUsername()).signWith(algorithm, secret).compact();
   }

   public boolean validateToken(String token) {
       System.out.println(token);
       String username = decodeToken(token);
       return userRepository.findById(username).isPresent();
   }

   public String decodeToken(String token) {
       return Jwts.parser().
               setSigningKey(secret)
               .parseClaimsJws(token)
               .getBody()
               .getSubject();
   }
}
