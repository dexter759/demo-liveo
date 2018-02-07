package com.example.demo.security;

import com.example.demo.entity.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    public String generate(JwtUser jwtUser) {

        Claims claims = Jwts.claims()
                .setSubject(jwtUser.getUserName());
        claims.put("userId", String.valueOf(jwtUser.getId()));
        claims.put("password",jwtUser.getPassword());
        claims.put("role", jwtUser.getRole());

             return Jwts.builder().setClaims(claims)
                        .signWith(SignatureAlgorithm.HS512,"test")
                        .compact();
    }
}
