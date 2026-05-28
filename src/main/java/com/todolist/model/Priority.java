package com.todolist.model;

/**
 * Enumeração de prioridades de uma tarefa.
 * Cada nível possui um valor numérico para comparação e um nome legível.
 */
public enum Priority {
    LOW(1, "Baixa"),
    MEDIUM(2, "Media"),
    HIGH(3, "Alta"),
    CRITICAL(4, "Critica");

    private final int level;
    private final String displayName;

    Priority(int level, String displayName) {
        this.level = level;
        this.displayName = displayName;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
