package com.example.todo.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo.dto.UpdateFullTaskRequest;
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

    @Transactional
    public Task save(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO tasks (title, description, status, created_at) VALUES (?,?,?,?)",
                new String[]{"id"}
            );
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());
            ps.setTimestamp(4, Timestamp.from(task.getCreatedAt()));
            return ps;
        }, keyHolder);
        task.setId(keyHolder.getKey().longValue());
        return task;
    }

    @Transactional
    public Optional<Task> updateFull(Long id, UpdateFullTaskRequest dto) {
        jdbc.update(
            "UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?",
            dto.getTitle(), dto.getDescription(), dto.getStatus(), id
        );
        return findById(id);
    }

    @Transactional
    public Optional<Task> updateText(Long id, String text, String fieldName) {
        String query = "UPDATE tasks SET " + fieldName + " = ? WHERE id = ?";
        jdbc.update(query, text, id);
        return findById(id);
    }

    @Transactional
    public void deleteTask(Long id) {
        jdbc.update("DELETE FROM tasks WHERE id = ?", id);
    }
}
