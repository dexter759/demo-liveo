package com.example.demo.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class JwtUserDetails implements UserDetails {
    public JwtUserDetails(String userName, long id, String token, List<GrantedAuthority> grantedAuthorities) {
    }
}
