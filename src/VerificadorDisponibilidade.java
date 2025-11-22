public class VerificadorDisponibilidade implements IVerificador {

    @Override
    public boolean verificar(Participante aluno, EventoAberto evento) {
        throw new UnsupportedOperationException("Método não aplicável a este verificador.");
    }

    // Verifica se o evento está LOTADO (RN02)
    @Override
    public boolean verificar(EventoAberto evento) {
        // Retorna TRUE se estiver lotado
        return evento.sala().getCapacidade() <= evento.numeroDeInscricoes();
    }
}