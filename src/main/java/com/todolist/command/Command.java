package com.todolist.command;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  PADRÃO: COMMAND                                                 ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  ONDE:  Interface Command (contrato base)                        ║
 * ║  COMO:  Encapsula cada operação (add, complete, remove) em um    ║
 * ║         objeto com execute() e undo(), gerenciado por            ║
 * ║         CommandHistory para desfazer ações.                      ║
 * ║  POR QUÊ: Desacopla quem invoca uma operação de quem a executa.  ║
 * ║           Permite undo/redo sem conhecer os detalhes internos    ║
 * ║           de cada operação — princípio OCP e DIP.                ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * ISP: interface mínima com apenas os métodos essenciais.
 */
public interface Command {

    /// Executa a operação encapsulada.
    void execute();

    //Desfaz a operação, restaurando o estado anterior.
    void undo();

    // Descrição legível do comando (para logs e depuração).
    String getDescription();
}
