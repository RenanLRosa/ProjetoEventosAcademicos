import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {
    // Referência ao Singleton Gestor
    private gestorDeIncricoes gestor = gestorDeIncricoes.getInstancia();
    private Scanner scanner;

    // Repositórios locais para simular o "banco de dados"
    private Administrador admin;
    private List<Sala> salas = new ArrayList<>();
    private List<IParticipante> participantes = new ArrayList<>();
    private List<IEvento> eventos = new ArrayList<>(); // Lista mestre de eventos

    public App() {
        this.scanner = new Scanner(System.in);
    }

    public void exec(){
        System.out.println("--- BEM-VINDO AO SISTEMA DE GESTÃO DE EVENTOS ---");
        System.out.println("(Usando a data/hora atual do sistema para operações)"); 
        
        // 1. Configura o cenário inicial com dados de teste
        this.setupInicial();
        
        // 2. Loop principal do menu
        int opcao;
        do {
            mostrarMenu();
            opcao = lerInt("\nEscolha uma opção: ");
            
            switch (opcao) {
                case 1:
                    criarSala();
                    break;
                case 2:
                    criarEvento(); // <-- Método Modificado
                    break;
                case 3:
                    cadastrarParticipante();
                    break;
                case 4:
                    listarEventosDisponiveis();
                    break;
                case 5:
                    listarParticipantes();
                    break;
                case 6:
                    inscreverParticipanteEmEvento();
                    break;
                case 7:
                    cancelarInscricao();
                    break;
                case 0:
                    System.out.println("Encerrando sistema. Até logo!");
                    break;
                default:
                    System.err.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
        
        scanner.close();
    }

    /**
     * ETAPA 1: Configura o cenário de teste inicial.
     * MODIFICADO: Usa LocalDateTime.now() como base para o agendamento.
     */
    private void setupInicial() {
        System.out.println("\n--- CONFIGURAÇÃO INICIAL (DADOS DE TESTE) ---");
        
        // Sala com Capacidade 2 (para testar a fila)
        Sala salaPequena = new Sala("Auditório C-01", 2); 
        salaPequena.adicionarRecurso(new Recurso("Projetor HD", "Audiovisual"));
        this.salas.add(salaPequena);
        System.out.println("Sala criada: " + salaPequena);

        // Atores
        admin = new Administrador("Ana Silva (Admin)", "ana@ifsc.edu");
        
        Participante p1 = new Participante("Carlos (P1)", "carlos@a.com");
        Participante p2 = new Participante("Bruna (P2)", "bruna@a.com");
        Participante p3 = new Participante("Daniel (P3 - Fila)", "daniel@a.com");
        this.participantes.add(p1);
        this.participantes.add(p2);
        this.participantes.add(p3);
        System.out.println("Atores criados: Admin, P1, P2, P3.");

        // Evento de Teste (Agendado para começar em 1 hora a partir de agora)
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioE1 = agora.plusHours(1).withMinute(0).withSecond(0).withNano(0); // Arredonda para a hora
        LocalDateTime fimE1 = inicioE1.plusHours(2); // Duração de 2 horas
        
        InformacaoEvento infoE1 = new InformacaoEvento("Workshop de Java", "Padrão State", salaPequena, inicioE1, fimE1);
        EventoAberto eventoJava = new EventoAberto(infoE1, salaPequena);

        // Agendamento
        admin.criarEvento(eventoJava);
        if (salaPequena.agendarEvento(eventoJava)) {
            this.eventos.add(eventoJava);
        }
    }

    /**
     * Exibe o menu principal de opções.
     */
    private void mostrarMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Criar Sala");
        System.out.println("2. Criar Evento (Agendamento Automático)");
        System.out.println("3. Cadastrar Participante");
        System.out.println("4. Listar Eventos Disponíveis");
        System.out.println("5. Listar Participantes");
        System.out.println("6. Inscrever Participante em Evento");
        System.out.println("7. Cancelar Inscrição");
        System.out.println("0. Sair");
    }

    // --- Métodos de Ação (Casos de Uso) ---

    private void criarSala() {
        System.out.println("\n--- 1. Criar Sala ---");
        String nome = lerString("Nome da sala:");
        int capacidade = lerInt("Capacidade:");
        
        Sala novaSala = new Sala(nome, capacidade);
        this.salas.add(novaSala);
        System.out.println("[SUCESSO] Sala '" + nome + "' (Cap: " + capacidade + ") criada!");
    }

    // *** MÉTODO MODIFICADO (Agendamento Automático) ***
    private void criarEvento() {
        System.out.println("\n--- 2. Criar Evento (Agendamento Automático) ---");
        String nome = lerString("Nome do evento:");
        String desc = lerString("Descrição:");
        
        listarSalas();
        int idSala = lerInt("Escolha o ID da Sala (Ex: 1):");
        Sala sala = buscarSala(idSala - 1); // Converte ID (1-based) para índice (0-based)
        if (sala == null) return;

        // Simplificação: Eventos duram 2 horas e começam na próxima hora vaga.
        // (Em um sistema real, isso exigiria uma lógica complexa de busca de horário)
        // Para esta simulação, vamos agendá-lo para a próxima hora cheia a partir de agora.
        
        try {
            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime inicio = agora.plusHours(1).withMinute(0).withSecond(0).withNano(0); // Arredonda para a próxima hora
            LocalDateTime fim = inicio.plusHours(2); // Duração fixa de 2 horas
            
            System.out.println("[AVISO] Agendando evento '" + nome + "' para " + inicio.format(DateTimeFormatter.ofPattern("dd/MM 'às' HH:mm")));

            InformacaoEvento info = new InformacaoEvento(nome, desc, sala, inicio, fim);
            EventoAberto novoEvento = new EventoAberto(info, sala);
            
            // Tenta agendar na sala (verifica conflito na agenda da sala)
            if (sala.agendarEvento(novoEvento)) {
                this.eventos.add(novoEvento);
                this.admin.criarEvento(novoEvento);
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar data/hora. " + e.getMessage());
        }
    }

    private void cadastrarParticipante() {
        System.out.println("\n--- 3. Cadastrar Participante ---");
        String nome = lerString("Nome do participante:");
        String email = lerString("Email:");
        
        Participante novoP = new Participante(nome, email);
        this.participantes.add(novoP);
        System.out.println("[SUCESSO] Participante '" + nome + "' cadastrado!");
    }

    private void listarEventosDisponiveis() {
        System.out.println("\n--- 4. Listar Eventos Disponíveis ---");
        int count = 1;
        for (IEvento evento : eventos) {
            // Apenas lista eventos que ainda estão no estado Aberto
            if (evento instanceof EventoAberto) {
                System.out.printf("[%d] %s (Sala: %s | Data: %s)%n", 
                    count++, 
                    evento.nome(), 
                    evento.sala().getNome(),
                    evento.dataInicial().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")));
            }
        }
        if (count == 1) {
            System.out.println("Nenhum evento aberto disponível.");
        }
    }

    private void listarParticipantes() {
        System.out.println("\n--- 5. Listar Participantes ---");
        int count = 1;
        for (IParticipante p : participantes) {
            System.out.printf("[%d] %s (Email: %s)%n", count++, p.nome(), p.email());
        }
    }

    private void inscreverParticipanteEmEvento() {
        System.out.println("\n--- 6. Inscrever Participante em Evento ---");
        
        listarParticipantes();
        int idParticipante = lerInt("Escolha o ID do Participante (Ex: 1):");
        Participante p = (Participante) buscarParticipante(idParticipante - 1);
        if (p == null) return;
        
        listarEventosDisponiveis();
        int idEvento = lerInt("Escolha o ID do Evento (Ex: 1):");
        EventoAberto e = buscarEventoAberto(idEvento - 1);
        if (e == null) return;

        // O Participante chama o Gestor, que verifica RN01 (Conflito) e RN02 (Lotação)
        p.inscrever(e);
    }

    private void cancelarInscricao() {
        System.out.println("\n--- 7. Cancelar Inscrição ---");

        listarParticipantes();
        int idParticipante = lerInt("Escolha o ID do Participante (Ex: 1):");
        Participante p = (Participante) buscarParticipante(idParticipante - 1);
        if (p == null) return;

        listarEventosDisponiveis(); // Lista os eventos que podem ser cancelados
        int idEvento = lerInt("Escolha o ID do Evento para CANCELAR (Ex: 1):");
        EventoAberto e = buscarEventoAberto(idEvento - 1);
        if (e == null) return;
        
        // O Participante chama o Gestor, que cancela e promove a fila
        p.cancelarInscricao(e);
    }

    // --- Métodos Auxiliares (Helpers de UI/Busca) ---

    private void listarSalas() {
        System.out.println("Salas disponíveis:");
        int count = 1;
        for (Sala s : salas) {
            System.out.printf("[%d] %s%n", count++, s.toString());
        }
    }

    // Busca por índice (baseado no ID 1-based)
    private Sala buscarSala(int index) {
        if (index >= 0 && index < salas.size()) {
            return salas.get(index);
        }
        System.err.println("ID da Sala inválido.");
        return null;
    }

    // Busca por índice
    private IParticipante buscarParticipante(int index) {
        if (index >= 0 && index < participantes.size()) {
            return participantes.get(index);
        }
        System.err.println("ID do Participante inválido.");
        return null;
    }

    // Busca por índice (apenas eventos abertos)
    private EventoAberto buscarEventoAberto(int index) {
        // Filtra a lista principal para pegar apenas Eventos Abertos
        List<EventoAberto> eventosAbertos = new ArrayList<>();
        for (IEvento ev : this.eventos) {
            if (ev instanceof EventoAberto) {
                eventosAbertos.add((EventoAberto) ev);
            }
        }
        
        if (index >= 0 && index < eventosAbertos.size()) {
            return eventosAbertos.get(index);
        }
        System.err.println("ID do Evento inválido ou evento não está aberto.");
        return null;
    }

    // --- Métodos Auxiliares (Helpers de Input) ---

    private String lerString(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }

    private int lerInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " ");
                int valor = scanner.nextInt();
                scanner.nextLine(); // Consome o "Enter" (nova linha)
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Limpa o buffer do scanner
                System.err.println("Entrada inválida. Por favor, digite um número inteiro.");
            }
        }
    }
}