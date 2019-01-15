package com.example.story;

import com.example.story.data.Project;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ResourceServerConfig {

    @LoadBalanced
    @Bean
    public RestOperations restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("localhost", 16379);
        JedisConnectionFactory factory = new JedisConnectionFactory(configuration);
        return factory;
    }

    @Bean
    public RedisTemplate<Integer, Project> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Integer, Project> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
