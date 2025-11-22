import java.util.ArrayList;
import java.util.List;

public class Administrador implements IAdministrador {
    private String nome;
    private String email;
    // *** CORREÇÃO DE TIPO (de Evento para IEvento) ***
    private List<IEvento> eventosCriados; 

    public Administrador(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.eventosCriados = new ArrayList<>();
    }

    @Override
    public void criarEvento(IEvento evento) {
        eventosCriados.add(evento);
        System.out.println("[ADMIN] Evento criado: " + evento.nome());
    }

    @Override
    public void cancelarEvento(IEvento evento) {
        if (eventosCriados.remove(evento)) {
            System.out.println("[ADMIN] Evento cancelado: " + evento.nome());
        }
    }

    @Override
    public String nome() {
        return nome;
    }

    @Override
    public String email() {
        return email;
    }

    public List<IEvento> eventosCriados() {
        return List.copyOf(eventosCriados);
    }

    //

}