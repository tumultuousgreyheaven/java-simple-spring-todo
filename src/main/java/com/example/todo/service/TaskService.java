package com.example.todo.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todo.dto.UpdateFullTaskRequest;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;

@Service
public class TaskService {
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> getAllTasks() {
        return repo.findAll();
    }

    public Task getTaskById(Long id) {
        return repo
            .findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(String title, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus("TODO");
        task.setCreatedAt(Instant.now());

        return repo.save(task);
    }

    public Task updateTaskFull(Long id, UpdateFullTaskRequest dto) {
        return repo
            .updateFull(id, dto)
            .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /*
    public ResponseEntity<Task> updateOrCreate(
        Long id, UpdateFullTaskRequest dto
    ) {
        if (repo.findById(id).equals(Optional.empty())) {
            return new ResponseEntity<>(
                createTask(dto.getTitle(), dto.getDescription()),
                HttpStatus.CREATED
            );
        } else {
            return new ResponseEntity<>(
                updateTaskFull(id, dto),
                HttpStatus.OK
            );
        }
    }
    */
}
