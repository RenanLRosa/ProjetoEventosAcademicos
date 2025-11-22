import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sala {
    private String nome;
    private int capacidade;
    private List<Recurso> recursosDaSala;
    private AgendaSala agenda;

    public Sala(String nome, int capacidade) {
        this.nome = nome;
        this.capacidade = capacidade;
        this.recursosDaSala = new ArrayList<>();
        this.agenda = new AgendaSala();
    }

    public String getNome() {
        return nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void adicionarRecurso(Recurso recurso) {
        this.recursosDaSala.add(recurso);
    }

    public List<Recurso> getRecursos() {
        return List.copyOf(this.recursosDaSala);
    }

    public boolean estaDisponivel(LocalDateTime inicio, LocalDateTime fim) {
        return this.agenda.estaDisponivel(inicio, fim);
    }

    // *** ADICIONADO FEEDBACK ***
    public boolean agendarEvento(IEvento novoEvento) {
        boolean sucesso = this.agenda.tentarAgendar(novoEvento);
        if (sucesso) {
            System.out.println("[SALA-SUCESSO] Evento '" + novoEvento.nome() + "' agendado na " + this.nome);
        } else {
            System.err.println("[SALA-FALHA] Horário indisponível para '" + novoEvento.nome() + "' na " + this.nome);
        }
        return sucesso;
    }

    @Override
    public String toString() {
        return nome + " (Capacidade: " + capacidade + ")";
    }
}