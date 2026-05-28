package com.todolist.command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Gerencia o histórico de comandos executados.
 * Usa uma pilha para garantir que o undo sempre desfaz o comando mais recente primeiro (LIFO).
 */
public class CommandHistory {

    private final Deque<Command> history = new ArrayDeque<>();

    /**
     * Executa o comando e o empilha no histórico.
     *
     * @param command comando a ser executado
     */
    public void execute(Command command) {
        command.execute();
        history.push(command);
        System.out.println("[CMD] " + command.getDescription());
    }

    /**
     * Desfaz o comando mais recente.
     * Se o histórico estiver vazio, exibe aviso.
     */
    public void undo() {
        if (history.isEmpty()) {
            System.out.println("[UNDO] Nenhuma acao para desfazer.");
            return;
        }
        Command last = history.pop();
        last.undo();
    }

    // Indica se há comandos no histórico para desfazer.
    public boolean hasHistory() {
        return !history.isEmpty();
    }

    // Quantidade de comandos no histórico.
    public int size() {
        return history.size();
    }
}
