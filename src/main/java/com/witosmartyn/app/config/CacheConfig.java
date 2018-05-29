package com.witosmartyn.app.config;

import lombok.extern.log4j.Log4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Cache could be disabled in unit test .
 */
    @Configuration
    @EnableCaching
    @Profile("production")
    @Log4j
    public class CacheConfig {

        @Bean(name = "cacheManager")
        public CacheManager cacheManager() {

            final CacheManager cm = new ConcurrentMapCacheManager(
                    "categories"
                    , "allRoles"
                    , "cities");
            log.info("### CASHE CONFIG ###");
            log.info("CacheNames ="+cm.getCacheNames());

            return cm;
        }
    }
