package com.example.demo.service;


import com.example.demo.entity.JwtUser;

import com.example.demo.repository.JwtUserRepository;
import com.example.demo.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserService {

    @Autowired
   private JwtUserRepository jwtUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtGenerator jwtGenerator;

    public void createUser(JwtUser jwtUser) {
        jwtUserRepository.save(jwtUser);
    }

    public String authorise(JwtUser jwtUser){
      JwtUser jwtUser1=  jwtUserRepository.findOne(jwtUser.getUserName());
      if(jwtUser1==null){
          System.out.println("Greska");
      }
      System.out.println("Testiranje Cacha");

      if(passwordEncoder.matches(jwtUser.getPassword(),jwtUser1.getPassword())){
          return jwtGenerator.generate(jwtUser1);
      }

        return null;
    }



}
