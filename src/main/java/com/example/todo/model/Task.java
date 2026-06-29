package com.example.todo.model;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Task {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title size must be at most 100 characters")
    private String title;

    @Size(max = 500, message = "Description size must be at most 500 characters")
    private String description;

    @Pattern(regexp = "TODO|IN_PROGRESS|DONE", message = "Status must be one of: TODO, IN_PROGRESS, DONE")
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
