import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AgendaSala {
    private List<IEvento> eventosAgendados;

    public AgendaSala() {
        this.eventosAgendados = new ArrayList<>();
    }

    public boolean estaDisponivel(LocalDateTime inicio, LocalDateTime fim) {
        for (IEvento evento : this.eventosAgendados) {
            if(evento instanceof EventoCancelado){
                continue;
            }
            boolean conflito = inicio.isBefore(evento.dataFinal()) &&
                    fim.isAfter(evento.dataInicial());
            if (conflito) {
                return false;
            }
        }
        return true;
    }

    public boolean tentarAgendar(IEvento novoEvento) {
        boolean disponivel = estaDisponivel(
                novoEvento.dataInicial(),
                novoEvento.dataFinal()
        );

        if (disponivel) {
            this.eventosAgendados.add(novoEvento);
            return true;
        } else {
            return false;
        }
    }

    public List<IEvento> getEventosAgendados() {
        return List.copyOf(this.eventosAgendados);
    }
}