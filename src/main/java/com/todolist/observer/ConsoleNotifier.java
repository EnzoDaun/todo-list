package com.todolist.observer;

import com.todolist.model.Task;

// exibe notificações amigáveis no consolesempre que um evento relevante ocorre em uma tarefa.
public class ConsoleNotifier implements TaskObserver {

    @Override
    public void onTaskAdded(Task task) {
        System.out.printf("[NOTIFICACAO] Nova tarefa adicionada: \"%s\" (Prioridade: %s)%n",
                task.getTitle(), task.getPriority());
    }

    @Override
    public void onTaskCompleted(Task task) {
        System.out.printf("[NOTIFICACAO] Tarefa concluida: \"%s\"%n", task.getTitle());
    }

    @Override
    public void onTaskRemoved(Task task) {
        System.out.printf("[NOTIFICACAO] Tarefa removida: \"%s\"%n", task.getTitle());
    }
}
