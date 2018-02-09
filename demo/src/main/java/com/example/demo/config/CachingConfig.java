package com.example.demo.config;

import com.example.demo.service.PersonService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public PersonService personService() {
        return new PersonService();
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("persons"),
                new ConcurrentMapCache("person"),
                new ConcurrentMapCache("skills"),
                new ConcurrentMapCache("skill")));
        return cacheManager;
    }
}