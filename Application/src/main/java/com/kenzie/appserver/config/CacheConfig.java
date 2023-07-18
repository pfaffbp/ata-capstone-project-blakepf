package com.kenzie.appserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    // Create a Cache here if needed
    @Bean
    public CacheUserStore userCache() {return new CacheUserStore(120, TimeUnit.SECONDS); }
    @Bean
    public CacheAnimeStore myCache() {
        return new CacheAnimeStore(120, TimeUnit.SECONDS);
    }
}
