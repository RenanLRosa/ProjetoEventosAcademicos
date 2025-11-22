// *** VERSÃO CORRIGIDA (Bugs de Nulo e Iteração) ***
import java.util.ArrayList;
import java.util.List;

public class Participante implements IParticipante {
    private String nome;
    private String email;
    private List<IInscricao> minhasInscricoes;
    gestorDeIncricoes gestor = gestorDeIncricoes.getInstancia();

    public Participante(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.minhasInscricoes = new ArrayList<>();
    }

    // *** CORRIGIDO (Adiciona verificação de nulo) E COM FEEDBACK ***
    @Override
    public void inscrever(EventoAberto evento) {
        System.out.println("\n[PARTICIPANTE] " + this.nome() + " tentando se inscrever no evento: " + evento.nome());
        IInscricao novaInscricao = gestor.registarInscricao(this, evento);
        
        // CORREÇÃO: Só adiciona na lista local se a inscrição não for nula
        if (novaInscricao != null) {
            minhasInscricoes.add(novaInscricao);
        }
    }

    // *** CORRIGIDO (Evita ConcurrentModificationException) E COM FEEDBACK ***
    @Override
    public void cancelarInscricao(EventoAberto evento) {
        System.out.println("\n[PARTICIPANTE] " + this.nome() + " tentando cancelar inscrição no evento: " + evento.nome());
        
        IInscricao inscricaoParaRemover = null;
        
        // 1. Acha a inscrição ativa (não cancelada) para este evento
        for (IInscricao inscricao : minhasInscricoes){
            // Compara referências de evento E verifica se não é uma já cancelada
            if(inscricao.getEvento() == evento && !(inscricao instanceof InscricaoCancelada)){
                inscricaoParaRemover = inscricao;
                break; // Para o loop
            }
        }

        // 2. Age FORA do loop para evitar o erro
        if (inscricaoParaRemover != null) {
            gestor.cancelarInscricao(inscricaoParaRemover, this); 
            // 3. Atualiza a lista local (substitui pela versão cancelada)
            // Encontra o índice da inscrição antiga
            int index = minhasInscricoes.indexOf(inscricaoParaRemover);
            if (index != -1) {
                 // Pega a nova referência (cancelada) do gestor
                IInscricao inscricaoCancelada = gestor.buscarInscricaoPorId(inscricaoParaRemover.getIdInscricao());
                minhasInscricoes.set(index, inscricaoCancelada);
            }
            
            System.out.println("[PARTICIPANTE] " + this.nome() + " processou o cancelamento.");
        } else {
            System.out.println("[PARTICIPANTE] " + this.nome() + " não encontrou inscrição ativa para este evento.");
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

    public List<IInscricao> getInscricoes(){
        return minhasInscricoes;
    }

    public Certificado requisitarCertificado(InscricaoConfirmada){
        
    }


    //Adicionar método: requisitarCertificado(Evento)
        //achar e usar o metodo que busca a inscrição pelo evento
            /*for (IInscricao inscricao : minhasInscricoes){
                // Compara referências de evento E verifica se não é uma já cancelada
                if(inscricao.getEvento() == evento && !(inscricao instanceof InscricaoCancelada)){
                    inscricaoParaCertificado = inscricao;
                    break; // Para o loop
                }
            } */
}
        //Passar a inscriçaõ para o gestor pedindo para mandar o certificado(Resto no próprio gestor)
        