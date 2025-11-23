import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {
    // Referência ao Singleton Gestor (apenas para garantir inicialização se necessário)
    private gestorDeIncricoes gestor = gestorDeIncricoes.getInstancia();
    private Scanner scanner;

    // Repositórios locais (Simulação de Banco de Dados)
    private Administrador admin;
    private List<Sala> salas = new ArrayList<>();
    private List<IParticipante> participantes = new ArrayList<>();
    private List<IEvento> eventos = new ArrayList<>(); 

    public App() {
        this.scanner = new Scanner(System.in);
    }

    public void exec(){
        System.out.println("--- BEM-VINDO AO SISTEMA DE GESTÃO DE EVENTOS ---");
        this.setupInicial();
        
        int opcao;
        do {
            mostrarMenu();
            opcao = lerInt("\nEscolha uma opção: ");
            
            switch (opcao) {
                case 1: criarSala(); break;
                case 2: criarEvento(); break;
                case 3: cadastrarParticipante(); break;
                case 4: listarEventosDisponiveis(); break;
                case 5: listarParticipantes(); break;
                case 6: inscreverParticipanteEmEvento(); break;
                case 7: cancelarInscricao(); break;
                case 8: marcarPresenca(); break;     // NOVO
                case 9: emitirCertificado(); break;  // NOVO
                case 10: listarMeusCertificados();
                case 0: System.out.println("Encerrando sistema. Até logo!"); break;
                default: System.err.println("Opção inválida.");
            }
        } while (opcao != 0);
        
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Criar Sala");
        System.out.println("2. Criar Evento");
        System.out.println("3. Cadastrar Participante");
        System.out.println("4. Listar Eventos Disponíveis");
        System.out.println("5. Listar Participantes");
        System.out.println("6. Inscrever Participante em Evento");
        System.out.println("7. Cancelar Inscrição");
        System.out.println("8. Marcar Presença (Simulação)");
        System.out.println("9. Emitir Certificado");
        System.out.println("10. Listar Meus Certificados");
        System.out.println("0. Sair");
    }

    // --- Métodos de Ação ---

    private void criarSala() {
        String nome = lerString("Nome da sala:");
        int capacidade = lerInt("Capacidade:");
        Sala novaSala = new Sala(nome, capacidade);
        this.salas.add(novaSala);
        System.out.println("Sala criada com sucesso.");
    }

    private void criarEvento() {
        String nome = lerString("Nome do evento:");
        String desc = lerString("Descrição:");
        
        listarSalas();
        int idSala = lerInt("ID da Sala:");
        Sala sala = buscarSala(idSala - 1);
        if (sala == null) return;

        // Simplificação: Cria evento para daqui a 1 hora
        LocalDateTime inicio = LocalDateTime.now().plusHours(1).withMinute(0).withSecond(0); 
        LocalDateTime fim = inicio.plusHours(2);
        
        InformacaoEvento info = new InformacaoEvento(nome, desc, sala, inicio, fim);
        EventoAberto novoEvento = new EventoAberto(info, sala);
        
        // Verifica retorno booleano da sala (sem prints na classe Sala)
        if (sala.agendarEvento(novoEvento)) {
            this.eventos.add(novoEvento);
            this.admin.criarEvento(novoEvento);
            System.out.println("Evento agendado com sucesso para " + inicio.format(DateTimeFormatter.ofPattern("HH:mm")));
        } else {
            System.err.println("Erro: A sala não está disponível neste horário.");
        }
    }

    private void cadastrarParticipante() {
        String nome = lerString("Nome:");
        String email = lerString("Email:");
        this.participantes.add(new Participante(nome, email));
        System.out.println("Participante cadastrado.");
    }

    private void inscreverParticipanteEmEvento() {
        listarParticipantes();
        int idP = lerInt("ID Participante:");
        Participante p = (Participante) buscarParticipante(idP - 1);
        if (p == null) return;
        
        listarEventosDisponiveis();
        int idE = lerInt("ID Evento:");
        EventoAberto e = buscarEventoAberto(idE - 1);
        if (e == null) return;

        // Captura a mensagem de retorno e exibe
        String resultado = p.inscrever(e);
        System.out.println(">> " + resultado);
    }

    private void cancelarInscricao() {
        listarParticipantes();
        int idP = lerInt("ID Participante:");
        Participante p = (Participante) buscarParticipante(idP - 1);
        if (p == null) return;

        listarEventosDisponiveis();
        int idE = lerInt("ID Evento para cancelar:");
        EventoAberto e = buscarEventoAberto(idE - 1);
        if (e == null) return;
        
        String resultado = p.cancelarInscricao(e);
        System.out.println(">> " + resultado);
    }

    private void marcarPresenca() {
        System.out.println("\n--- 8. Marcar Presença ---");
        listarParticipantes();
        int idP = lerInt("ID do Participante:");
        Participante p = (Participante) buscarParticipante(idP - 1);
        if (p == null) return;

        listarEventosDisponiveis();
        int idE = lerInt("ID do Evento:");
        EventoAberto e = buscarEventoAberto(idE - 1);
        if (e == null) return;

        String resultado = p.marcarPresenca(e);
        System.out.println(">> " + resultado);
    }

    private void emitirCertificado() {
        System.out.println("\n--- 9. Emitir Certificado ---");
        listarParticipantes();
        int idP = lerInt("ID do Participante:");
        Participante p = (Participante) buscarParticipante(idP - 1);
        if (p == null) return;

        listarEventosDisponiveis();
        int idE = lerInt("ID do Evento:");
        EventoAberto e = buscarEventoAberto(idE - 1);
        if (e == null) return;

        String resultado = p.requisitarCertificado(e);
        System.out.println("------------------------------------------------");
        System.out.println(resultado);
        System.out.println("------------------------------------------------");
    }

    private void listarMeusCertificados() {
        System.out.println("\n--- 10. Listar Meus Certificados ---");
        
        listarParticipantes();
        int idP = lerInt("ID do Participante:");
        IParticipante p = buscarParticipante(idP - 1); // Note que agora usa a interface
        if (p == null) return;

        List<Certificado> certificados = p.getCertificados();

        if (certificados.isEmpty()) {
            System.out.println(">> Este participante ainda não possui certificados emitidos.");
        } else {
            System.out.println(">> Certificados de " + p.nome() + ":");
            for (Certificado c : certificados) {
                // Usa o toString() que criamos na etapa anterior
                System.out.println("   * " + c.toString());
            }
        }
    }

    // --- Helpers de Listagem e Busca ---

    private void listarEventosDisponiveis() {
        int count = 1;
        for (IEvento evento : eventos) {
            if (evento instanceof EventoAberto) {
                EventoAberto ev = (EventoAberto) evento;
                int vagas = ev.sala().getCapacidade() - ev.numeroDeInscricoes();
                String statusVagas = vagas > 0 ? vagas + " vagas" : "LOTADO (Fila)";
                
                System.out.printf("[%d] %s | Sala: %s | %s%n", 
                    count++, ev.nome(), ev.sala().getNome(), statusVagas);
            }
        }
    }

    private void listarParticipantes() {
        int count = 1;
        for (IParticipante p : participantes) {
            System.out.printf("[%d] %s (%s)%n", count++, p.nome(), p.email());
        }
    }
    
    private void listarSalas() {
        int count = 1;
        for (Sala s : salas) System.out.printf("[%d] %s%n", count++, s.toString());
    }

    private Sala buscarSala(int index) {
        if (index >= 0 && index < salas.size()) return salas.get(index);
        System.err.println("Sala não encontrada.");
        return null;
    }

    private IParticipante buscarParticipante(int index) {
        if (index >= 0 && index < participantes.size()) return participantes.get(index);
        System.err.println("Participante não encontrado.");
        return null;
    }

    private EventoAberto buscarEventoAberto(int index) {
        List<EventoAberto> abertos = new ArrayList<>();
        for(IEvento ev : eventos) if(ev instanceof EventoAberto) abertos.add((EventoAberto)ev);
        if (index >= 0 && index < abertos.size()) return abertos.get(index);
        System.err.println("Evento não encontrado.");
        return null;
    }

    // --- Helpers de Input e Setup ---

    private String lerString(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }

    private int lerInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " ");
                int v = scanner.nextInt();
                scanner.nextLine(); return v;
            } catch (InputMismatchException e) {
                scanner.nextLine(); System.err.println("Digite um número válido.");
            }
        }
    }
    
    private void setupInicial() {
        System.out.println("\n--- CONFIGURANDO DADOS DE TESTE... ---");
        Sala salaPequena = new Sala("Auditório C-01", 2); 
        this.salas.add(salaPequena);

        admin = new Administrador("Ana Silva", "ana@ifsc.edu");
        
        Participante p1 = new Participante("Carlos", "carlos@a.com");
        Participante p2 = new Participante("Bruna", "bruna@a.com");
        Participante p3 = new Participante("Daniel", "daniel@a.com");
        this.participantes.add(p1);
        this.participantes.add(p2);
        this.participantes.add(p3);

        LocalDateTime inicio = LocalDateTime.now().plusHours(1);
        InformacaoEvento info = new InformacaoEvento("Java Workshop", "Intro", salaPequena, inicio, inicio.plusHours(2));
        EventoAberto evento = new EventoAberto(info, salaPequena);

        admin.criarEvento(evento);
        if (salaPequena.agendarEvento(evento)) {
            this.eventos.add(evento);
        }
        System.out.println("Sistema pronto.");
    }
}