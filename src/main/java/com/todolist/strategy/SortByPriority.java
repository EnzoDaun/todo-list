package com.todolist.strategy;

import com.todolist.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//ordena tarefas por nível de prioridade, da mais crítica (CRITICAL=4) para a menos urgente (LOW=1).
public class SortByPriority implements SortStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        List<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator.comparingInt(
                (Task t) -> t.getPriority().getLevel()).reversed());
        return sorted;
    }

    @Override
    public String getDescription() {
        return "Prioridade (Critica -> Alta -> Media -> Baixa)";
    }
}
