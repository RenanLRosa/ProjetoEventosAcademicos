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

    public String getNome() { return nome; }
    public int getCapacidade() { return capacidade; }

    public void adicionarRecurso(Recurso recurso) {
        this.recursosDaSala.add(recurso);
    }

    public List<Recurso> getRecursos() {
        return List.copyOf(this.recursosDaSala);
    }

    public boolean estaDisponivel(LocalDateTime inicio, LocalDateTime fim) {
        return this.agenda.estaDisponivel(inicio, fim);
    }

    public boolean agendarEvento(IEvento novoEvento) {
        return this.agenda.tentarAgendar(novoEvento);
    }

    @Override
    public String toString() {
        return nome + " (Capacidade: " + capacidade + ")";
    }
}