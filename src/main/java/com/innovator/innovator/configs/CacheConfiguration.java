//package com.innovator.innovator.configs;
//
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.support.SimpleCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//@EnableCaching
//public class CacheConfiguration extends CachingConfigurerSupport {
//    private static final int TIME_TO_LEAVE = 1;                //in days
//    private static final int CACHE_SIZE = 100;
//
//    public static final String CACHE_IMAGE = "image";
//
//    @Bean
//    @Override
//    public CacheManager cacheManager() {
//        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
//
//
//
//        return super.cacheManager();
//    }
//}
