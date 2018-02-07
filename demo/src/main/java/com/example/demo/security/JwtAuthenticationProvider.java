package com.example.demo.security;

import com.example.demo.entity.JwtAuthenticationToken;
import com.example.demo.entity.JwtUser;
import com.example.demo.entity.JwtUserDetails;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{

    @Autowired
    private JwtValidator validator;

    @Override
    public void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    public UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

      JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
      String token = jwtToken.getToken();
      JwtUser jwtUser= validator.validate(token);

    if(jwtUser == null){
        throw new RuntimeException("JWT Token is incorrect");
    }
        System.out.println(token);
        System.out.println(jwtUser.getUserName());
        System.out.println(jwtUser.getRole());
        System.out.println(jwtUser.getId());
        System.out.println(jwtUser.getPassword());



        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(jwtUser.getRole());

        System.out.println(grantedAuthorities.get(0));
        return new JwtUserDetails(jwtUser.getUserName(),token,jwtUser.getId(),jwtUser.getPassword(),grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
