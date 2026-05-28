package com.todolist.command;

import com.todolist.model.Task;
import com.todolist.observer.TaskObserver;
import com.todolist.repository.TaskRepository;

import java.util.List;

/**
 * remove uma tarefa do repositório.
 * Undo: reinsere a tarefa removida, mantendo todos os seus dados originais intactos (objeto Task é guardado por referência).
 */
public class RemoveTaskCommand implements Command {

    private final TaskRepository repository;
    private final Task task;
    private final List<TaskObserver> observers;

    public RemoveTaskCommand(TaskRepository repository,
                             Task task,
                             List<TaskObserver> observers) {
        this.repository = repository;
        this.task       = task;
        this.observers  = observers;
    }

    @Override
    public void execute() {
        repository.remove(task);
        observers.forEach(o -> o.onTaskRemoved(task));
    }

    @Override
    public void undo() {
        repository.add(task);
        System.out.println("[UNDO] Remocao desfeita — tarefa restaurada: \"" + task.getTitle() + "\"");
    }

    @Override
    public String getDescription() {
        return "Remover tarefa: \"" + task.getTitle() + "\"";
    }
}
