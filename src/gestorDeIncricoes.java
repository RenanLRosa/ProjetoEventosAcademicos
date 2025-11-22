// *** VERSÃO CORRIGIDA (Lógica do Switch e Cancelamento) ***
import java.util.HashMap;
import java.util.Map;

public class gestorDeIncricoes {
    private static gestorDeIncricoes instancia;
    private Map<Integer, IInscricao> repositorioInscricoes;
    private static int nextId = 1;

    // Verificadores
    VerificadorConflitoHorario verificadorConflito = new VerificadorConflitoHorario();
    VerificadorDisponibilidade verificadorDisponibilidade = new VerificadorDisponibilidade();

    // Construtor privado
    private gestorDeIncricoes(){
        this.repositorioInscricoes = new HashMap<>();
    }

    // Singleton getInstance()
    public static gestorDeIncricoes getInstancia(){
        if (instancia == null){
            instancia = new gestorDeIncricoes();
        }
        return instancia;
    }

    // 0 = OK, 1 = Conflito, 2 = Lotado
    private int verificadorGeral(Participante aluno, EventoAberto evento){
        if(verificadorConflito.verificar(aluno, evento)){
            return 1;
        }else if(verificadorDisponibilidade.verificar(evento)){
            return 2;
        }else{
            return 0;
        }
    }

    // *** CORRIGIDO E COM FEEDBACK ***
    public IInscricao registarInscricao(Participante aluno, EventoAberto evento){
        int resultado = verificadorGeral(aluno, evento);
        
        switch (resultado) {
            case 0: // SUCESSO
                InscricaoConfirmada novaInscricao = new InscricaoConfirmada(nextId++, aluno, evento);
                this.repositorioInscricoes.put(novaInscricao.getIdInscricao(), novaInscricao);
                evento.adicionarInscricao(novaInscricao.getIdInscricao());
                
                System.out.println("[GESTOR-SUCESSO] Participante '" + aluno.nome() + "' inscrito (ID: " + novaInscricao.getIdInscricao() + ") no evento '" + evento.nome() + "'.");
                return novaInscricao;

            case 1: // CONFLITO DE HORÁRIO
                System.err.println("[GESTOR-FALHA] Participante '" + aluno.nome() + "': Erro. Conflito de horário detectado.");
                return null; // << CORREÇÃO: Adicionado 'return null' e 'break' implícito

            case 2: // EVENTO LOTADO (FILA)
                InscricaoEmFila novaInscricaoFila = new InscricaoEmFila(nextId++, aluno, evento);
                this.repositorioInscricoes.put(novaInscricaoFila.getIdInscricao(), novaInscricaoFila);
                evento.adicionarNaFila(novaInscricaoFila);
                
                System.out.println("[GESTOR-FILA] Participante '" + aluno.nome() + "': Evento cheio. Inscrição (ID: " + novaInscricaoFila.getIdInscricao() + ") na fila.");
                return novaInscricaoFila;

            default:
                throw new AssertionError("Resultado inesperado do verificador geral.");
        }
    }

    public IInscricao buscarInscricaoPorId(int idInscricao) {
        return this.repositorioInscricoes.get(idInscricao);
    }

    private void excluirInscricao(int idInscricao){
        // Apenas remove do repositório principal
        repositorioInscricoes.remove(idInscricao);
    }

    // *** CORRIGIDO (LÓGICA DE PROMOÇÃO DE FILA) E COM FEEDBACK ***
    public void cancelarInscricao(IInscricao inscricao, Participante participante){
        System.out.println("\n[GESTOR] Processando cancelamento da Inscrição ID: " + inscricao.getIdInscricao());

        if (inscricao instanceof InscricaoCancelada) {
            System.out.println("[GESTOR-AVISO] Esta inscrição já está cancelada.");
            return;
        }

        EventoAberto evento = (EventoAberto) inscricao.getEvento();

        // 1. Torna a inscrição atual "Cancelada"
        InscricaoCancelada novaInscricaoCancelada = new InscricaoCancelada(inscricao);
        // Substitui a inscrição antiga pela cancelada no repositório
        this.repositorioInscricoes.put(novaInscricaoCancelada.getIdInscricao(), novaInscricaoCancelada);

        if(inscricao instanceof InscricaoConfirmada){
            System.out.println("[GESTOR] Cancelando uma 'Inscrição Confirmada'. Verificando fila...");
            evento.cancelarInscricao(inscricao.getIdInscricao()); // Remove da lista de confirmados do evento

            // 2. Lógica de Promoção: Puxa o próximo da fila
            if (evento.fila.temFila()) {
                int idParaPromover = evento.movimentarFila(); // Puxa o ID mais antigo e remove da fila
                IInscricao inscricaoDaFila = buscarInscricaoPorId(idParaPromover);

                // 3. Verifica se o item da fila é válido e o promove
                if (inscricaoDaFila instanceof InscricaoEmFila) {
                    InscricaoEmFila inscricaoParaConfirmar = (InscricaoEmFila) inscricaoDaFila;
                    
                    // Cria a nova inscrição confirmada substituindo a antiga
                    InscricaoConfirmada promovida = new InscricaoConfirmada(
                        inscricaoParaConfirmar.getIdInscricao(),
                        inscricaoParaConfirmar.getParticipante(), // Usa o getter
                        inscricaoParaConfirmar.getEvento()
                    );
                    
                    // Atualiza o repositório com a inscrição promovida
                    this.repositorioInscricoes.put(promovida.getIdInscricao(), promovida);
                    evento.adicionarInscricao(promovida.getIdInscricao()); // Adiciona na lista de confirmados
                    
                    System.out.println("[GESTOR-PROMOÇÃO] Vaga aberta! Inscrição ID " + promovida.getIdInscricao() + " (" + promovida.getParticipante().nome() + ") foi promovida da Fila para Confirmada.");
                
                }
            } else {
                System.out.println("[GESTOR] Vaga aberta, mas a fila está vazia.");
            }

        } else if (inscricao instanceof InscricaoEmFila) {
            // Apenas remove da fila
            System.out.println("[GESTOR] Cancelando uma 'Inscrição em Fila'.");
            evento.removerDaFila(inscricao.getIdInscricao());
        }
    }

    //Marcar presença(Pega a inscrição) 
        //(Adicionar status na inscrição)
    

    //Novo método emitircertificado(Inscrição)
        //Testar se o aluno tem presença nessa inscrição(Adicionar status na inscrição)
        //Se não tiver presença printar erro
        //Se tiver presença printar - Certificado emitido


}