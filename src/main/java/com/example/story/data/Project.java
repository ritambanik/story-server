package com.example.story.data;

import java.io.Serializable;

public class Project implements Serializable {

    private int id;
    private String name;
    private boolean active;

    public Project() {
        this(createProjectBuilder());
    }

    public Project(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.active = builder.active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }


    public static class Builder {
        private int id;
        private String name;
        private boolean active;

        public Builder id (int id) {
            this.id = id;
            return this;
        }

        public Builder name (String name) {
            this.name = name;
            return this;
        }

        public Builder active (boolean active) {
            this.active = active;
            return this;
        }

        public Project build() {
            return new Project(this);
        }

    }
    public static Builder createProjectBuilder() {
        return new Builder();
    }
}
