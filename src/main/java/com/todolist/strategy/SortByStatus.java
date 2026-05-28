package com.todolist.strategy;

import com.todolist.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//ordena tarefas pelo status no ciclo de vida: PENDING -> IN_PROGRESS -> COMPLETED (ordem de urgência operacional).

public class SortByStatus implements SortStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        List<Task> sorted = new ArrayList<>(tasks);
        // Enum.ordinal() reflete a ordem de declaração: PENDING=0, IN_PROGRESS=1, COMPLETED=2
        sorted.sort(Comparator.comparingInt(t -> t.getStatus().ordinal()));
        return sorted;
    }

    @Override
    public String getDescription() {
        return "Status (Pendente -> Em Progresso -> Concluida)";
    }
}
