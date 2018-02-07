package com.example.demo.security;

import com.example.demo.entity.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {

    public JwtUser validate(String token) {
        JwtUser jwtUser=null;

        try {
            String secret = "test";
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            jwtUser = new JwtUser();
            System.out.println(body.getSubject());
            jwtUser.setUserName(body.getSubject());
            jwtUser.setId(Long.parseLong((String) body.get("userId")));
            jwtUser.setPassword((String)body.get("password"));
            jwtUser.setRole((String) body.get("role"));


        }catch (Exception e){
            System.out.println(e);
        }
        return jwtUser;
    }
}
