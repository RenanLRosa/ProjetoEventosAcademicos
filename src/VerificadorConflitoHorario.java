// *** VERSÃO CORRIGIDA (Lógica de Overlap) ***
public class VerificadorConflitoHorario implements IVerificador {

    @Override
    public boolean verificar(Participante aluno, EventoAberto eventoNovo){
        for(IInscricao inscricaoLista : aluno.getInscricoes()){
            IEvento eventoExistente = inscricaoLista.getEvento();
            
            // Ignora inscrições canceladas na verificação de conflito
            if (inscricaoLista instanceof InscricaoCancelada) {
                continue;
            }

            // Lógica de Overlap (RN01) - Verdadeiro se houver conflito
            // (InícioA < FimB) E (FimA > InícioB)
            boolean conflito = eventoNovo.dataInicial().isBefore(eventoExistente.dataFinal()) &&
                             eventoNovo.dataFinal().isAfter(eventoExistente.dataInicial());
            
            if(conflito){
                return true; // Encontrou conflito
            }
        }
        return false; // Sem conflitos
    }

    @Override
    public boolean verificar(EventoAberto evento) {
        throw new UnsupportedOperationException("Método não aplicável a este verificador.");
    }
}