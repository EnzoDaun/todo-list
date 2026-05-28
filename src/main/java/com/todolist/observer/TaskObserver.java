package com.todolist.observer;

import com.todolist.model.Task;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  PADRÃO: OBSERVER                                                ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  ONDE:  Interface TaskObserver (Subject: TodoFacade)             ║
 * ║  COMO:  Contrato com 3 eventos do ciclo de vida de tarefas       ║
 * ║  POR QUÊ: Desacopla o núcleo da aplicação das reações a          ║
 * ║         eventos. Novos observadores (ex: notificação push,       ║
 * ║         email) podem ser adicionados sem modificar o código      ║
 * ║         existente — princípio Open/Closed (OCP).                 ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public interface TaskObserver {

    // Disparado quando uma nova tarefa é adicionada ao repositório.
    void onTaskAdded(Task task);

    // Disparado quando uma tarefa tem seu status marcado como COMPLETED.
    void onTaskCompleted(Task task);

    //Disparado quando uma tarefa é removida do repositório.
    void onTaskRemoved(Task task);
}
