package com.todolist.repository;

import com.todolist.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  PADRÃO: SINGLETON                                               ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  ONDE:  Classe TaskRepository                                    ║
 * ║  COMO:  Construtor privado + getInstance() com double-checked    ║
 * ║         locking thread-safe (volatile + synchronized)            ║
 * ║  POR QUÊ: Um ToDo List deve ter exatamente um repositório de     ║
 * ║         tarefas. O Singleton garante que todos os componentes    ║
 * ║         (Facade, Commands) acessem o mesmo armazenamento,        ║
 * ║         evitando inconsistências de estado.                      ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * Responsabilidade única (SRP): armazenar e recuperar tarefas.
 * Não contém lógica de negócio.
 */
public class TaskRepository {

    // volatile garante visibilidade entre threads
    private static volatile TaskRepository instance;

    private final List<Task> tasks;

    // Construtor privado impede instanciação externa
    private TaskRepository() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Retorna a instância única do repositório.
     * Usa double-checked locking para eficiência em ambientes multi-thread.
     */
    public static TaskRepository getInstance() {
        if (instance == null) {
            synchronized (TaskRepository.class) {
                if (instance == null) {
                    instance = new TaskRepository();
                }
            }
        }
        return instance;
    }

    // Persiste uma nova tarefa.
    public void add(Task task) {
        tasks.add(task);
    }

    /** Remove uma tarefa existente. */
    public void remove(Task task) {
        tasks.remove(task);
    }

    // Busca tarefa pelo identificador único.
    public Optional<Task> findById(String id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    // Retorna visão imutável de todas as tarefas.
    public List<Task> findAll() {
        return Collections.unmodifiableList(tasks);
    }

    // Quantidade de tarefas armazenadas.
    public int size() {
        return tasks.size();
    }
}
