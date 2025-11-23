public class InscricaoEmFila implements IInscricao {
    private int id;
    private Participante participante; // Mantém Participante concreto
    private EventoAberto evento; // Mantém EventoAberto concreto

    public InscricaoEmFila(int id, Participante participante, EventoAberto evento) {
        this.id = id;
        this.participante = participante;
        this.evento = evento;
    }
    
    // *** MÉTODO ADICIONADO (Necessário para o Gestor) ***
    public Participante getParticipante() {
        return this.participante;
    }

    @Override
    public boolean solicitarCancelamento() {
        // Lógica de cancelamento (se houver)
        return true;
    }

    @Override
    public void tornarConfirmado() {
        // Este método é chamado pelo Gestor
        System.out.println("[INSCRIÇÃO] Inscrição ID " + id + " está sendo promovida para Confirmada.");
        // A lógica de transição real ocorre no Gestor (que cria um novo objeto InscricaoConfirmada)
    }

    @Override
    public int getIdInscricao() {
        return id;
    }

    @Override
    public void marcarPresenca() {
        System.err.println("AVISO: Não é possível marcar presença para uma inscrição em fila.");
    }

    @Override
    public IEvento getEvento() {
        return evento;
    }

    @Override
    public boolean temPresenca() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'temPresenca'");
    }
}
