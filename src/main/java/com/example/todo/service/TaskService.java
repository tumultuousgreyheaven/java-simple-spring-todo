package com.example.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
        return
            repo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
