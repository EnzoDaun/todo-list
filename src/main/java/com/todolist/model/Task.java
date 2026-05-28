package com.todolist.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Entidade central da aplicacao. Representa uma tarefa com
 * título, descrição, prioridade, status e timestamps.
 */
public class Task {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private final LocalDateTime createdAt;

    public Task(String title, String description, Priority priority) {
        this.id          = UUID.randomUUID().toString();
        this.title       = title;
        this.description = description;
        this.priority    = priority;
        this.status      = TaskStatus.PENDING;
        this.createdAt   = LocalDateTime.now();
    }

    // Getters

    public String getId()          { return id; }
    public String getTitle()       { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus()  { return status; }
    public Priority getPriority()  { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters

    public void setTitle(String title)             { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(Priority priority)     { this.priority = priority; }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    // Utilitários

    @Override
    public String toString() {
        return String.format("Task{id='%s', title='%s', priority=%s, status=%s, createdAt=%s}",
                id.substring(0, 8), title, priority, status,
                createdAt.format(FORMATTER));
    }
}
