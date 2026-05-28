package com.todolist.facade;

import com.todolist.command.*;
import com.todolist.model.Priority;
import com.todolist.model.Task;
import com.todolist.model.TaskStatus;
import com.todolist.observer.TaskObserver;
import com.todolist.repository.TaskRepository;
import com.todolist.strategy.SortStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  PADRÃO: FACADE                                                  ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  ONDE:  Classe TodoFacade                                        ║
 * ║  COMO:  Oferece uma API simples e coesa que internamente         ║
 * ║         orquestra: TaskRepository (Singleton), CommandHistory    ║
 * ║         (Command), List<TaskObserver> (Observer) e               ║
 * ║         SortStrategy (Strategy) de forma transparente.           ║
 * ║  POR QUÊ: O cliente (Main.java) não precisa conhecer a           ║
 * ║         complexidade interna dos subsistemas. Reduz              ║
 * ║         acoplamento e simplifica o uso da aplicação.             ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class TodoFacade {

    private final TaskRepository     repository;
    private final CommandHistory     commandHistory;
    private final List<TaskObserver> observers;
    private SortStrategy             sortStrategy;

    public TodoFacade(SortStrategy defaultStrategy) {
        this.repository     = TaskRepository.getInstance(); // Singleton
        this.commandHistory = new CommandHistory();
        this.observers      = new ArrayList<>();
        this.sortStrategy   = defaultStrategy;
    }

    // Gerenciamento de observadores

    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    //  Estratégia de ordenação

    public void setSortStrategy(SortStrategy strategy) {
        this.sortStrategy = strategy;
        System.out.println("[STRATEGY] Ordenacao alterada para: " + strategy.getDescription());
    }

    //  Operações CRUD (todas via Command)

    /**
     * Cria e adiciona uma nova tarefa ao repositório.
     * @return a tarefa criada (para o cliente obter o id, se necessário)
     */
    public Task addTask(String title, String description, Priority priority) {
        Task task = new Task(title, description, priority);
        commandHistory.execute(new AddTaskCommand(repository, task, observers));
        return task;
    }


    // Edita título, descrição e prioridade de uma tarefa existente.
    public void editTask(String taskId, String newTitle,
                         String newDescription, Priority newPriority) {
        repository.findById(taskId).ifPresentOrElse(
            task -> commandHistory.execute(
                        new EditTaskCommand(task, newTitle, newDescription, newPriority)),
            ()   -> System.out.println("[AVISO] Tarefa nao encontrada: " + taskId)
        );
    }

    // Avança o status de uma tarefa para IN_PROGRESS.
    public void startTask(String taskId) {
        repository.findById(taskId).ifPresentOrElse(
            task -> {
                if (task.getStatus() == TaskStatus.COMPLETED) {
                    System.out.println("[AVISO] Tarefa ja esta concluida.");
                    return;
                }
                task.setStatus(TaskStatus.IN_PROGRESS);
                System.out.println("[CMD] Tarefa iniciada: \"" + task.getTitle() + "\"");
            },
            () -> System.out.println("[AVISO] Tarefa nao encontrada: " + taskId)
        );
    }

    // Marca uma tarefa como COMPLETED.
    public void completeTask(String taskId) {
        repository.findById(taskId).ifPresentOrElse(
            task -> commandHistory.execute(new CompleteTaskCommand(task, observers)),
            ()   -> System.out.println("[AVISO] Tarefa nao encontrada: " + taskId)
        );
    }

    // Remove uma tarefa do repositório.
    public void removeTask(String taskId) {
        repository.findById(taskId).ifPresentOrElse(
            task -> commandHistory.execute(new RemoveTaskCommand(repository, task, observers)),
            ()   -> System.out.println("[AVISO] Tarefa nao encontrada: " + taskId)
        );
    }

    // Desfaz a última operação registrada no histórico de comandos.
    public void undoLastAction() {
        commandHistory.undo();
    }

    // Consultas

    //Retorna todas as tarefas ordenadas pela estratégia ativa.
    public List<Task> listTasks() {
        return sortStrategy.sort(repository.findAll());
    }

    //Exibe todas as tarefas no console com índices para seleção.
    public void printTasks() {
        List<Task> tasks = listTasks();
        System.out.println();
        System.out.println("================================================");
        System.out.printf ("  TAREFAS (%d) — %s%n", tasks.size(), sortStrategy.getDescription());
        System.out.println("================================================");
        if (tasks.isEmpty()) {
            System.out.println("  (nenhuma tarefa cadastrada)");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                System.out.printf("  [%2d] %-32s | %-8s | %s%n",
                        i + 1,
                        t.getTitle(),
                        t.getPriority(),
                        t.getStatus());
            }
        }
        System.out.println("================================================");
    }

    // Conta tarefas pendentes.
    public long countPending() {
        return repository.findAll().stream()
                .filter(t -> t.getStatus() == TaskStatus.PENDING)
                .count();
    }

    // Conta tarefas concluídas
    public long countCompleted() {
        return repository.findAll().stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .count();
    }

    //Retorna se há histórico de comandos para desfazer.
    public boolean hasUndoHistory() {
        return commandHistory.hasHistory();
    }
}
