package com.example.demo.controller;

import com.example.demo.entity.JwtUser;
import com.example.demo.entity.Skill;
import com.example.demo.security.JwtGenerator;
import com.example.demo.service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {

    private JwtGenerator jwtGenerator;

    @Autowired
    private JwtUserService jwtUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public TokenController(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping
    public void generate(@RequestBody final JwtUser jwtUser){

        JwtUser jwtUser1 = jwtUser;
        jwtUser1.setPassword(passwordEncoder.encode(jwtUser.getPassword()));

        jwtUserService.createUser(jwtUser1);

    }

    @PostMapping("/login")
    public ResponseEntity<String> getSkillById(@RequestBody JwtUser jwtUser) {
        String token = jwtUserService.authorise(jwtUser);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token: ", token);
        System.out.println(token);
        return ResponseEntity.ok().headers(headers).body(token);
    }
}
