package com.todolist.strategy;

import com.todolist.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//ordena tarefas por data de criação,,da mais antiga para a mais recente (ordem cronológica).
public class SortByCreationDate implements SortStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        List<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator.comparing(Task::getCreatedAt));
        return sorted;
    }

    @Override
    public String getDescription() {
        return "Data de criacao (mais antiga -> mais recente)";
    }
}
