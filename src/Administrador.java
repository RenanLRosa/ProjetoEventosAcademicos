import java.util.ArrayList;
import java.util.List;

public class Administrador implements IAdministrador {
    private String nome;
    private String email;
    private List<IEvento> eventosCriados; 

    public Administrador(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.eventosCriados = new ArrayList<>();
    }

    @Override
    public void criarEvento(IEvento evento) {
        eventosCriados.add(evento);
    }

    @Override
    public boolean cancelarEvento(IEvento evento) {
        return eventosCriados.remove(evento);
    }

    @Override public String nome() { return nome; }
    @Override public String email() { return email; }
    public List<IEvento> eventosCriados() { return List.copyOf(eventosCriados); }
}