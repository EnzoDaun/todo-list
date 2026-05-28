package com.todolist.command;

import com.todolist.model.Priority;
import com.todolist.model.Task;

/**
 * edita os campos de uma tarefa existente.
 * Captura o estado completo ANTES da edição para permitir undo, restaurando título, descrição e prioridade.
 */
public class EditTaskCommand implements Command {

    private final Task task;

    // Estado anterior (para undo)
    private final String    oldTitle;
    private final String    oldDescription;
    private final Priority  oldPriority;

    // Novo estado (para execute)
    private final String    newTitle;
    private final String    newDescription;
    private final Priority  newPriority;

    public EditTaskCommand(Task task,
                           String newTitle,
                           String newDescription,
                           Priority newPriority) {
        this.task           = task;
        // snapshot do estado atual ANTES da edição
        this.oldTitle       = task.getTitle();
        this.oldDescription = task.getDescription();
        this.oldPriority    = task.getPriority();
        // valores desejados
        this.newTitle       = newTitle;
        this.newDescription = newDescription;
        this.newPriority    = newPriority;
    }

    @Override
    public void execute() {
        task.setTitle(newTitle);
        task.setDescription(newDescription);
        task.setPriority(newPriority);
    }

    @Override
    public void undo() {
        task.setTitle(oldTitle);
        task.setDescription(oldDescription);
        task.setPriority(oldPriority);
        System.out.println("[UNDO] Edicao desfeita — tarefa restaurada para: \"" + oldTitle + "\"");
    }

    @Override
    public String getDescription() {
        return "Editar tarefa: \"" + oldTitle + "\" -> \"" + newTitle + "\"";
    }
}
