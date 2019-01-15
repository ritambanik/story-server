package com.example.story;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestOperations;

@SpringBootApplication
@EnableCircuitBreaker
public class StoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoryApplication.class, args);
    }

    @Bean
    public ProjectClient projectClient(@Value("${project-server.url}") String projectServerUrl, RestOperations restTemplate, RedisTemplate redisTemplate) {
        return new ProjectClient(restTemplate, projectServerUrl, redisTemplate);
    }

}

