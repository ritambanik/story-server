package com.example.story.gateway;

import com.example.story.data.Story;
import com.example.story.data.StoryForm;

import java.util.List;
import java.util.Optional;

public interface StoryRepository {

    Story createStory(StoryForm story);

    Optional<Story> fetchStory(Integer id);

    List<Story> fetchAllStories();

    Optional<Story> updateStory(Integer id, StoryForm story);

    Optional<Boolean> deleteStory(Integer id);
}
