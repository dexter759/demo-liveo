package com.example.demo;

import com.example.demo.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import com.damir.restapi.service.ApplicationUserService;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public static SkillService skillService(){
        return new SkillService();
    }


//    @Bean public static ApplicationUserService applicationUserService(){return new ApplicationUserService();}

//    @Bean public static UserDaoImpl userDao(){
//        return new UserDaoImpl();
//    }

}
