import java.util.HashMap;
import java.util.Map;

public class gestorDeIncricoes {
    private static gestorDeIncricoes instancia;
    private Map<Integer, IInscricao> repositorioInscricoes;
    private static int nextId = 1;

    VerificadorConflitoHorario verificadorConflito = new VerificadorConflitoHorario();
    VerificadorDisponibilidade verificadorDisponibilidade = new VerificadorDisponibilidade();

    private gestorDeIncricoes(){
        this.repositorioInscricoes = new HashMap<>();
    }

    public static gestorDeIncricoes getInstancia(){
        if (instancia == null){
            instancia = new gestorDeIncricoes();
        }
        return instancia;
    }

    private int verificadorGeral(Participante aluno, EventoAberto evento){
        if(verificadorConflito.verificar(aluno, evento)){
            return 1;
        }else if(verificadorDisponibilidade.verificar(evento)){
            return 2;
        }else{
            return 0;
        }
    }

    public IInscricao registarInscricao(Participante aluno, EventoAberto evento){
        int resultado = verificadorGeral(aluno, evento);
        
        switch (resultado) {
            case 0: // SUCESSO
                InscricaoConfirmada novaInscricao = new InscricaoConfirmada(nextId++, aluno, evento);
                this.repositorioInscricoes.put(novaInscricao.getIdInscricao(), novaInscricao);
                evento.adicionarInscricao(novaInscricao.getIdInscricao());
                return novaInscricao;

            case 1: // CONFLITO
                return null; 

            case 2: // FILA
                InscricaoEmFila novaInscricaoFila = new InscricaoEmFila(nextId++, aluno, evento);
                this.repositorioInscricoes.put(novaInscricaoFila.getIdInscricao(), novaInscricaoFila);
                evento.adicionarNaFila(novaInscricaoFila);
                return novaInscricaoFila;

            default:
                throw new AssertionError("Resultado inesperado.");
        }
    }

    public IInscricao buscarInscricaoPorId(int idInscricao) {
        return this.repositorioInscricoes.get(idInscricao);
    }

    public String cancelarInscricao(IInscricao inscricao, Participante participante){
        StringBuilder log = new StringBuilder();

        if (inscricao instanceof InscricaoCancelada) {
            return "Erro: Esta inscrição já está cancelada.";
        }

        EventoAberto evento = (EventoAberto) inscricao.getEvento();

        // 1. Torna a inscrição atual "Cancelada"
        InscricaoCancelada novaInscricaoCancelada = new InscricaoCancelada(inscricao);
        this.repositorioInscricoes.put(novaInscricaoCancelada.getIdInscricao(), novaInscricaoCancelada);
        log.append("Inscrição cancelada com sucesso.");

        if(inscricao instanceof InscricaoConfirmada){
            evento.cancelarInscricao(inscricao.getIdInscricao()); 

            // 2. Lógica de Promoção da Fila
            if (evento.fila.temFila()) {
                int idParaPromover = evento.movimentarFila(); 
                IInscricao inscricaoDaFila = buscarInscricaoPorId(idParaPromover);

                if (inscricaoDaFila instanceof InscricaoEmFila) {
                    InscricaoEmFila inscricaoParaConfirmar = (InscricaoEmFila) inscricaoDaFila;
                    
                    InscricaoConfirmada promovida = new InscricaoConfirmada(
                        inscricaoParaConfirmar.getIdInscricao(),
                        inscricaoParaConfirmar.getParticipante(),
                        inscricaoParaConfirmar.getEvento()
                    );
                    
                    this.repositorioInscricoes.put(promovida.getIdInscricao(), promovida);
                    evento.adicionarInscricao(promovida.getIdInscricao());
                    
                    log.append("\n[SISTEMA] Vaga preenchida! " + promovida.getParticipante().nome() + " saiu da fila e foi confirmado(a).");
                }
            }
        } else if (inscricao instanceof InscricaoEmFila) {
            evento.removerDaFila(inscricao.getIdInscricao());
            log.append(" (Removido da fila de espera)");
        }
        
        return log.toString();
    }

    public void marcarPresenca(IInscricao inscricao){
        inscricao.marcarPresenca();
    }

    public Certificado emitirCertificado(IInscricao inscricao){
        if (inscricao.temPresenca()) {
            return new Certificado(inscricao);
        } else{
            return null; // Retorna null indicando que não atendeu aos critérios
        }
    }
}