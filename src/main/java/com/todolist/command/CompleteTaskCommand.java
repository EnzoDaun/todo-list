package com.todolist.command;

import com.todolist.model.Task;
import com.todolist.model.TaskStatus;
import com.todolist.observer.TaskObserver;

import java.util.List;

/**
 * marca uma tarefa como COMPLETED.
 * Guarda o status anterior para possibilitar o undo, independente do estado em que a tarefa estava antes.
 */
public class CompleteTaskCommand implements Command {

    private final Task task;
    private final TaskStatus previousStatus;
    private final List<TaskObserver> observers;

    public CompleteTaskCommand(Task task, List<TaskObserver> observers) {
        this.task           = task;
        this.previousStatus = task.getStatus(); // captura estado ANTES
        this.observers      = observers;
    }

    @Override
    public void execute() {
        task.setStatus(TaskStatus.COMPLETED);
        observers.forEach(o -> o.onTaskCompleted(task));
    }

    @Override
    public void undo() {
        task.setStatus(previousStatus);
        System.out.println("[UNDO] Conclusao desfeita — \"" + task.getTitle()
                + "\" voltou para: " + previousStatus);
    }

    @Override
    public String getDescription() {
        return "Concluir tarefa: \"" + task.getTitle() + "\"";
    }
}
