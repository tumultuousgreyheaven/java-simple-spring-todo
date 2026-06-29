package com.example.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.todo.model.Task;

@Repository
public class TaskRepository {
    private final JdbcTemplate jdbc;

    public TaskRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Task> rowMapper = (rs, rowNum) -> {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        task.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        return task;
    };

    public List<Task> findAll() {
        return jdbc.query("SELECT * FROM tasks", rowMapper);
    }

    public Optional<Task> findById(Long id) {
        List<Task> tasks = jdbc.query(
                "SELECT * FROM tasks WHERE id = ?", rowMapper, id);
        return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.get(0));
    }

    // other CRUD methods
}
