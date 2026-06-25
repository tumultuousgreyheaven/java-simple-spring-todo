package com.example.todo.model;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;

public class Task {
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private String status = "TODO";
    private Instant createdAt;

    // constructors, getters, setters

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatus() {
        return this.status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
