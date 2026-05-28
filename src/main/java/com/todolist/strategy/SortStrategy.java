package com.todolist.strategy;

import com.todolist.model.Task;

import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  PADRÃO: STRATEGY                                                ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  ONDE:  Interface SortStrategy                                   ║
 * ║  COMO:  Encapsula diferentes algoritmos de ordenação em classes  ║
 * ║         separadas, intercambiáveis em tempo de execução via      ║
 * ║         TodoFacade.setSortStrategy().                            ║
 * ║  POR QUÊ: Permite trocar a forma de exibir tarefas sem           ║
 * ║         modificar o código existente (OCP). O usuário pode       ║
 * ║         ordenar por prioridade, data ou status conforme          ║
 * ║         sua necessidade, sem if/else espalhados no código.       ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public interface SortStrategy {

    /**
     * Ordena e retorna uma nova lista de tarefas.
     * A lista original não é modificada.
     *
     * @param tasks lista de tarefas a ordenar
     * @return nova lista ordenada segundo esta estratégia
     */
    List<Task> sort(List<Task> tasks);

    // Nome legível da estratégia para exibição no console.
    String getDescription();
}
