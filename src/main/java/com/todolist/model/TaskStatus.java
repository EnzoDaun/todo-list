package com.todolist.model;

// Enumeração dos possíveis estados de uma tarefa no ciclo de vida.
public enum TaskStatus {
    PENDING("Pendente"),
    IN_PROGRESS("Em Progresso"),
    COMPLETED("Concluida");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
