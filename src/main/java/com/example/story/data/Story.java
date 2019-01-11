package com.example.story.data;

public class Story {

    private Integer id;
    private Integer projectId;
    private String name;
    private String info;

    private Story() {
    }

    public Story (Builder builder) {
        this.id = builder.id;
        this.projectId = builder.projectId;
        this.name = builder.name;
        this.info = builder.info;
    }

    public Integer getId() {
        return id;
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

    public static Builder createStoryBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private Integer projectId;
        private String name;
        private String info;

        public Builder id (Integer id) {
            this.id = id;
            return this;
        }

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

        public Story build() {
            return new Story(this);
        }
    }
}
