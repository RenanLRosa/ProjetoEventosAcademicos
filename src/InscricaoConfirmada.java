public class InscricaoConfirmada implements IInscricao {
    private int id;
    private IParticipante participante;
    private IEvento evento;

    public InscricaoConfirmada(int id, IParticipante participante, IEvento evento) {
        this.id = id;
        this.participante = participante;
        this.evento = evento;
    }
    
    // *** MÉTODO ADICIONADO (Necessário para o Gestor) ***
    public IParticipante getParticipante() {
        return this.participante;
    }

    @Override
    public boolean solicitarCancelamento() {
        // Lógica de cancelamento (se houver)
        return true;
    }

    @Override
    public void tornarConfirmado() {
        System.err.println("AVISO: Tentativa de confirmar uma inscrição que já está confirmada.");
    }

    @Override
    public int getIdInscricao() {
        return id;
    }

    @Override
    public boolean marcarPresenca() { //Testa se o evento tá aberto
        // Lógica de presença (ainda não implementada)
        return true;
    }

    @Override
    public IEvento getEvento() {
        return evento;
    }
}