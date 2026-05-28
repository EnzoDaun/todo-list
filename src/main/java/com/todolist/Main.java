package com.todolist;

import com.todolist.facade.TodoFacade;
import com.todolist.model.Priority;
import com.todolist.model.Task;
import com.todolist.observer.ConsoleNotifier;
import com.todolist.observer.TaskEventLogger;
import com.todolist.strategy.SortByCreationDate;
import com.todolist.strategy.SortByPriority;
import com.todolist.strategy.SortByStatus;

import java.util.List;
import java.util.Scanner;

/**
 * CLI interativa do ToDo List.
 *
 * Todos os padrões de projeto são exercitados pelo usuário em tempo real:
 *   • Singleton  — TaskRepository compartilhado por toda a sessão
 *   • Observer   — ConsoleNotifier e TaskEventLogger reagem a cada operação
 *   • Command    — add/edit/complete/remove registrados no histórico (undo)
 *   • Strategy   — ordenação trocável pelo menu sem reiniciar
 *   • Facade     — Main só conhece TodoFacade; subsistemas são transparentes
 *
 * Grupo: Enzo Shimada Daun (840552) · Miguel Ribas Berlese (839938) · Mateus Ávila (839015)
 * Disciplina: Padrões de Projetos de Software
 */
public class Main {

    private static final Scanner       scanner = new Scanner(System.in);
    private static       TodoFacade    todo;
    private static       TaskEventLogger logger;

    // Ponto de entrada

    public static void main(String[] args) {
        printHeader();

        // Configura a fachada com os dois observadores
        logger = new TaskEventLogger();
        todo   = new TodoFacade(new SortByPriority());
        todo.addObserver(new ConsoleNotifier());
        todo.addObserver(logger);

        // Loop principal do menu
        boolean running = true;
        while (running) {
            printMenu();
            String opcao = lerLinha("Escolha: ").trim();
            System.out.println();

            switch (opcao) {
                case "1" -> todo.printTasks();
                case "2" -> handleAdicionar();
                case "3" -> handleIniciar();
                case "4" -> handleConcluir();
                case "5" -> handleEditar();
                case "6" -> handleRemover();
                case "7" -> handleUndo();
                case "8" -> handleOrdenacao();
                case "9" -> logger.printLog();
                case "0" -> { running = false; System.out.println("Ate logo!"); }
                default  -> System.out.println("[AVISO] Opcao invalida. Digite um numero de 0 a 9.");
            }
            if (running) pausar();
        }

        scanner.close();
    }

    // Handlers de cada opção do menu

    // Opção 2 — Adicionar nova tarefa
    private static void handleAdicionar() {
        System.out.println("--- ADICIONAR TAREFA ---");
        String titulo    = lerCampo("Titulo: ");
        String descricao = lerCampo("Descricao (Enter para pular): ");
        Priority prioridade = escolherPrioridade();

        todo.addTask(titulo, descricao.isEmpty() ? "-" : descricao, prioridade);
    }

    // Opção 3 — Iniciar tarefa (PENDING → IN_PROGRESS)
    private static void handleIniciar() {
        System.out.println("--- INICIAR TAREFA ---");
        Task t = selecionarTarefa();
        if (t != null) {
            todo.startTask(t.getId());
        }
    }

    // Opção 4 — Concluir tarefa (qualquer → COMPLETED)
    private static void handleConcluir() {
        System.out.println("--- CONCLUIR TAREFA ---");
        Task t = selecionarTarefa();
        if (t != null) {
            todo.completeTask(t.getId());
        }
    }

    //  Opção 5 — Editar campos de uma tarefa existente
    private static void handleEditar() {
        System.out.println("--- EDITAR TAREFA ---");
        Task t = selecionarTarefa();
        if (t == null) return;

        System.out.printf("Titulo atual: \"%s\"%n", t.getTitle());
        String novoTitulo = lerCampo("Novo titulo (Enter para manter): ");
        if (novoTitulo.isEmpty()) novoTitulo = t.getTitle();

        System.out.printf("Descricao atual: \"%s\"%n", t.getDescription());
        String novaDesc = lerCampo("Nova descricao (Enter para manter): ");
        if (novaDesc.isEmpty()) novaDesc = t.getDescription();

        System.out.printf("Prioridade atual: %s%n", t.getPriority());
        System.out.print("Nova prioridade? (s = sim / Enter = manter): ");
        String trocar = lerLinha("").trim();
        Priority novaPrioridade = trocar.equalsIgnoreCase("s")
                ? escolherPrioridade()
                : t.getPriority();

        todo.editTask(t.getId(), novoTitulo, novaDesc, novaPrioridade);
        System.out.println("[OK] Tarefa editada com sucesso.");
    }

    // Opção 6 — Remover tarefa
    private static void handleRemover() {
        System.out.println("--- REMOVER TAREFA ---");
        Task t = selecionarTarefa();
        if (t == null) return;

        System.out.printf("Confirmar remocao de \"%s\"? (s/N): ", t.getTitle());
        String conf = lerLinha("").trim();
        if (conf.equalsIgnoreCase("s")) {
            todo.removeTask(t.getId());
        } else {
            System.out.println("Operacao cancelada.");
        }
    }

    // Opção 7 — Desfazer última ação
    private static void handleUndo() {
        if (!todo.hasUndoHistory()) {
            System.out.println("[AVISO] Nenhuma acao para desfazer.");
            return;
        }
        todo.undoLastAction();
    }

    // Opção 8 — Alterar estratégia de ordenação
    private static void handleOrdenacao() {
        System.out.println("--- ALTERAR ORDENACAO ---");
        System.out.println("  1. Por prioridade (Critica -> Baixa)");
        System.out.println("  2. Por status (Pendente -> Concluida)");
        System.out.println("  3. Por data de criacao (mais antiga -> recente)");
        String op = lerLinha("Escolha: ").trim();

        switch (op) {
            case "1" -> todo.setSortStrategy(new SortByPriority());
            case "2" -> todo.setSortStrategy(new SortByStatus());
            case "3" -> todo.setSortStrategy(new SortByCreationDate());
            default  -> System.out.println("[AVISO] Opcao invalida.");
        }
    }

    // Helpers de UI

    /**
     * Exibe a lista atual com índices e pede ao usuário para escolher uma tarefa pelo número.
     * Retorna null se a lista estiver vazia ou entrada for inválida.
     */
    private static Task selecionarTarefa() {
        List<Task> tarefas = todo.listTasks();
        if (tarefas.isEmpty()) {
            System.out.println("(nenhuma tarefa cadastrada)");
            return null;
        }

        todo.printTasks();

        while (true) {
            String entrada = lerLinha("Numero da tarefa (0 = cancelar): ").trim();
            if (entrada.equals("0")) {
                System.out.println("Operacao cancelada.");
                return null;
            }
            try {
                int idx = Integer.parseInt(entrada) - 1;
                if (idx >= 0 && idx < tarefas.size()) {
                    return tarefas.get(idx);
                }
                System.out.printf("[AVISO] Digite um numero entre 1 e %d.%n", tarefas.size());
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Entrada invalida — digite apenas o numero.");
            }
        }
    }

    //  Apresenta as prioridades e retorna a escolhida.
    private static Priority escolherPrioridade() {
        System.out.println("  Prioridades disponiveis:");
        System.out.println("    1. Baixa    2. Media    3. Alta    4. Critica");
        while (true) {
            String p = lerLinha("  Prioridade (1-4): ").trim();
            switch (p) {
                case "1": return Priority.LOW;
                case "2": return Priority.MEDIUM;
                case "3": return Priority.HIGH;
                case "4": return Priority.CRITICAL;
                default : System.out.println("  [AVISO] Digite 1, 2, 3 ou 4.");
            }
        }
    }

    // Lê uma linha não nula do console, exibindo um prompt.
    private static String lerLinha(String prompt) {
        System.out.print(prompt);
        String linha = scanner.nextLine();
        return linha == null ? "" : linha;
    }

    //  Lê um campo obrigatório (repete até receber algo não vazio).
    private static String lerCampo(String prompt) {
        return lerLinha(prompt);
    }

    // Pausa até o usuário pressionar Enter.
    private static void pausar() {
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        scanner.nextLine();
        System.out.println();
    }

    // Impressão de telas

    private static void printHeader() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║          ToDo List — Design Patterns             ║");
        System.out.println("║  Singleton | Observer | Command | Strategy       ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  Enzo Shimada Daun        (840552)               ║");
        System.out.println("║  Miguel Ribas Berlese     (839938)               ║");
        System.out.println("║  Mateus Avila             (839015)               ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printMenu() {
        long pendentes  = todo.countPending();
        long concluidas = todo.countCompleted();
        System.out.printf("  [ %d pendente(s) · %d concluida(s) ]%n%n",
                pendentes, concluidas);
        System.out.println("  ┌─────────────────────────────────┐");
        System.out.println("  │           MENU PRINCIPAL        │");
        System.out.println("  ├─────────────────────────────────┤");
        System.out.println("  │  1. Listar tarefas              │");
        System.out.println("  │  2. Adicionar tarefa            │");
        System.out.println("  │  3. Iniciar tarefa              │");
        System.out.println("  │  4. Concluir tarefa             │");
        System.out.println("  │  5. Editar tarefa               │");
        System.out.println("  │  6. Remover tarefa              │");
        System.out.println("  │  7. Desfazer ultima acao (undo) │");
        System.out.println("  │  8. Alterar ordenacao           │");
        System.out.println("  │  9. Ver log de auditoria        │");
        System.out.println("  │  0. Sair                        │");
        System.out.println("  └─────────────────────────────────┘");
    }
}
