package com.example.story;

import com.example.story.data.Project;
import com.example.story.data.ProjectStatus;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.Optional;


@Service
public class ProjectClient {


    private RestOperations restOperations;
    private String projectServerEndpoint;
    private RedisTemplate redisTemplate;


    public ProjectClient(RestOperations restOperations, String projectServerEndpoint, RedisTemplate redisTemplate) {
        this.restOperations = restOperations;
        this.projectServerEndpoint = projectServerEndpoint;
        this.redisTemplate = redisTemplate;
    }

    @HystrixCommand(fallbackMethod = "checkProjectFromCache")
    public ProjectStatus checkProject(int projectId) {
        Optional<Project> project = Optional.ofNullable(restOperations.getForObject(projectServerEndpoint + "/projects/" + projectId, Project.class));
        if (project.isPresent()) {
            redisTemplate.opsForValue().set(projectId, project.get());
            if (project.get().isActive()) return ProjectStatus.ACTIVE;
            return ProjectStatus.INACTIVE;
        }
        return ProjectStatus.NOT_EXISTS;
    }

    public ProjectStatus checkProjectFromCache(int projectId) {
        return ((Project)redisTemplate.opsForValue().get(projectId)).isActive() ? ProjectStatus.ACTIVE : ProjectStatus.INACTIVE ;
    }
}
