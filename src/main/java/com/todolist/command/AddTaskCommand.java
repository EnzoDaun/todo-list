package com.todolist.command;

import com.todolist.model.Task;
import com.todolist.observer.TaskObserver;
import com.todolist.repository.TaskRepository;

import java.util.List;

/**
 * Adiciona uma tarefa ao repositório e notifica os observadores registrados.
 * Undo: remove a tarefa adicionada, revertendo o estado.
 */
public class AddTaskCommand implements Command {

    private final TaskRepository repository;
    private final Task task;
    private final List<TaskObserver> observers;

    public AddTaskCommand(TaskRepository repository,
                          Task task,
                          List<TaskObserver> observers) {
        this.repository = repository;
        this.task = task;
        this.observers = observers;
    }

    @Override
    public void execute() {
        repository.add(task);
        observers.forEach(o -> o.onTaskAdded(task));
    }

    @Override
    public void undo() {
        repository.remove(task);
        System.out.println("[UNDO] Adicao desfeita — tarefa removida: \"" + task.getTitle() + "\"");
    }

    @Override
    public String getDescription() {
        return "Adicionar tarefa: \"" + task.getTitle() + "\"";
    }
}
