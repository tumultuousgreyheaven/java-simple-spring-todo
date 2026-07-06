package com.example.todo.model;

import java.time.Instant;

import com.example.todo.dto.UpdateFullTaskRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Task {
    @Min(1L)
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

    public Task() {}

    public Task(Long id, UpdateFullTaskRequest dto) {
        this.id = id;
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
        this.createdAt = null;
    }

    public Task(
        Long id,
        String title, String description, String status,
        Instant createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }
    
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
