package com.example.story;

import com.example.story.data.Project;
import com.example.story.data.ProjectStatus;
import com.example.story.data.Story;
import com.example.story.data.StoryForm;
import com.example.story.gateway.StoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController("/stories")
public class StoryController {

    private final Logger log = LoggerFactory.getLogger(StoryController.class);

    @Autowired
    private ProjectClient projectClient;

    @Autowired
    private StoryRepository repo;

    @PostMapping
    public ResponseEntity<Story> createStory(@RequestBody StoryForm storyForm) {
        ProjectStatus projectStatus = projectClient.checkProject(storyForm.getProjectId());
        if (ProjectStatus.ACTIVE.equals(projectStatus)) {
            Story story = repo.createStory(storyForm);
            return new ResponseEntity<>(story, HttpStatus.CREATED);
        } else if (ProjectStatus.INACTIVE.equals(projectStatus)) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/stories/{id}")
    public ResponseEntity<Story> getStory(@PathVariable("id") Integer id) {
        Optional<Story> story = repo.fetchStory(id);
        if (story.isPresent()) {
            return new ResponseEntity<>(story.get(), HttpStatus.FOUND);
        }
        log.debug("No story present with id {}", id);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<Story>> getStories() {
        return new ResponseEntity<>(repo.fetchAllStories(), HttpStatus.ACCEPTED);
    }

    @PutMapping("/stories/{id}")
    public ResponseEntity<Story> updateStory(@PathVariable("id") Integer id, @RequestBody StoryForm newStory) {
        Optional<Story> updatedStory = repo.updateStory(id, newStory);
        if (updatedStory.isPresent()) {
            return new ResponseEntity<>(updatedStory.get(), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/stories/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable("id") Integer id) {
        Optional<Boolean> result = repo.deleteStory(id);
        return result.get() ? new ResponseEntity<>(HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
