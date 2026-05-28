package com.todolist.observer;

import com.todolist.model.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * registra todos os eventos de tarefas em um log interno para auditoria e consulta posterior.
 * Demonstra como múltiplos observadores podem coexistir, cada um com uma responsabilidade distinta (SRP).
 */
public class TaskEventLogger implements TaskObserver {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final List<String> entries = new ArrayList<>();

    @Override
    public void onTaskAdded(Task task) {
        record("ADICIONADA", task);
    }

    @Override
    public void onTaskCompleted(Task task) {
        record("CONCLUIDA", task);
    }

    @Override
    public void onTaskRemoved(Task task) {
        record("REMOVIDA", task);
    }

    private void record(String event, Task task) {
        String entry = String.format("[%s] %-12s | id=%s | \"%s\"",
                LocalDateTime.now().format(FORMATTER),
                event,
                task.getId().substring(0, 8),
                task.getTitle());
        entries.add(entry);
    }

    /** Imprime o log completo de eventos no console. */
    public void printLog() {
        System.out.println();
        System.out.println("=================================================");
        System.out.println("             LOG DE AUDITORIA DE EVENTOS         ");
        System.out.println("=================================================");
        if (entries.isEmpty()) {
            System.out.println("  (nenhum evento registrado)");
        } else {
            entries.forEach(e -> System.out.println("  " + e));
        }
        System.out.println("=================================================");
    }
}
