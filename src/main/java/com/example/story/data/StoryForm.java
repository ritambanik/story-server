package com.example.story.data;

public class StoryForm {

    private Integer projectId;
    private String name;
    private String info;

    public StoryForm() {
        this(createStoryFormBuilder());
    }

    public StoryForm (Builder builder) {
        this.projectId = builder.projectId;
        this.name = builder.name;
        this.info = builder.info;
    }


    public Integer getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public static Builder createStoryFormBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Integer projectId;
        private String name;
        private String info;

        public Builder projectId(Integer projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder info(String info) {
            this.info = info;
            return this;
        }

        public StoryForm build() {
            return new StoryForm(this);
        }
    }
}
