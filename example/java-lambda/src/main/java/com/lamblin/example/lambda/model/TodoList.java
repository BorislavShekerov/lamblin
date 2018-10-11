package com.lamblin.example.lambda.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public final class TodoList {

    private final UUID uid;
    private final String name;
    private final List<TodoListEntry> entries;

    @JsonCreator
    public TodoList(
            @JsonProperty("uid") UUID uid,
            @JsonProperty("name") String name,
            @JsonProperty("entries") List<TodoListEntry> entries) {

        this.uid = uid;
        this.name = name;
        this.entries = entries;
    }

    public UUID getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public List<TodoListEntry> getEntries() {
        return entries;
    }

    public static final class TodoListEntry {

        private final String name;
        private final String description;

        @JsonCreator
        public TodoListEntry(@JsonProperty("name") String name, @JsonProperty("description") String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}
