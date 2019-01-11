package com.example.story.gateway;

import static java.util.stream.Collectors.toList;

import com.example.story.data.Story;
import com.example.story.data.StoryForm;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Repository
@ConditionalOnProperty(value = "env.test", havingValue = "true")
public class InMemoryStoryStore implements StoryRepository {

    private Map<Integer, Story> dataStore = new ConcurrentHashMap<>();
    private AtomicInteger idGenerator = new AtomicInteger(100);
    private Story story;

    @Override
    public Story createStory(StoryForm storyForm) {
        final Story story = Story.createStoryBuilder().id(idGenerator.incrementAndGet())
                .projectId(storyForm.getProjectId()).name(storyForm.getName())
                .info(storyForm.getInfo()).build();
        dataStore.put(story.getId(), story);
        return story;
    }

    @Override
    public Optional<Story> fetchStory(Integer id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    @Override
    public List<Story> fetchAllStories() {
        return dataStore.values().stream().collect(toList());
    }

    @Override
    public Optional<Story> updateStory(Integer id, StoryForm storyForm) {
        if (dataStore.containsKey(id)) {
            final Story story = Story.createStoryBuilder().id(idGenerator.incrementAndGet())
                    .projectId(storyForm.getProjectId()).name(storyForm.getName())
                    .info(storyForm.getInfo()).build();
            dataStore.remove(id);
            dataStore.put(story.getId(), story);
            return Optional.of(story);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> deleteStory(Integer id) {
        if (dataStore.containsKey(id)) {
            dataStore.remove(id);
            return Optional.of(Boolean.TRUE);
        }
        return Optional.of(Boolean.FALSE);
    }
}
