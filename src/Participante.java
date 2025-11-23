import java.util.ArrayList;
import java.util.List;

public class Participante implements IParticipante {
    private String nome;
    private String email;
    private List<IInscricao> minhasInscricoes;
    private List<Certificado> meusCertificados;
    gestorDeIncricoes gestor = gestorDeIncricoes.getInstancia();

    public Participante(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.minhasInscricoes = new ArrayList<>();
        this.meusCertificados = new ArrayList<>();
    }

    @Override
    public String inscrever(EventoAberto evento) {
        IInscricao novaInscricao = gestor.registarInscricao(this, evento);
        
        if (novaInscricao == null) {
            return "Falha na inscrição: Conflito de horário detectado.";
        } else {
            minhasInscricoes.add(novaInscricao);
            if (novaInscricao instanceof InscricaoEmFila) {
                return "Evento lotado. Você entrou na FILA de espera (Posição " + evento.fila.temFila() + ").";
            } else {
                return "Inscrição CONFIRMADA com sucesso!";
            }
        }
    }

    @Override
    public String cancelarInscricao(EventoAberto evento) {
        IInscricao inscricaoParaRemover = null;
        
        for (IInscricao inscricao : minhasInscricoes){
            if(inscricao.getEvento() == evento && !(inscricao instanceof InscricaoCancelada)){
                inscricaoParaRemover = inscricao;
                break;
            }
        }

        if (inscricaoParaRemover != null) {
            String log = gestor.cancelarInscricao(inscricaoParaRemover, this);
            
            // Atualiza referência local
            int index = minhasInscricoes.indexOf(inscricaoParaRemover);
            if (index != -1) {
                IInscricao inscricaoCancelada = gestor.buscarInscricaoPorId(inscricaoParaRemover.getIdInscricao());
                minhasInscricoes.set(index, inscricaoCancelada);
            }
            return log;
        } else {
            return "Erro: Nenhuma inscrição ativa encontrada para este evento.";
        }
    }

    @Override
    public String marcarPresenca(EventoAberto evento) {
        IInscricao inscricaoParaPresenca = null;

        for (IInscricao inscricao : minhasInscricoes) {
            if (inscricao.getEvento() == evento && inscricao instanceof InscricaoConfirmada) {
                inscricaoParaPresenca = inscricao;
                break;
            }
        }

        if (inscricaoParaPresenca != null) {
            gestor.marcarPresenca(inscricaoParaPresenca);
            return "SUCESSO: Presença registrada no evento '" + evento.nome() + "'.";
        } else {
            return "ERRO: Você não possui uma inscrição confirmada ativa neste evento para marcar presença.";
        }
    }

    @Override
    public String requisitarCertificado(EventoAberto evento) {
        IInscricao inscricaoParaCertificado = null;

        for (IInscricao inscricao : minhasInscricoes) {
            if (inscricao.getEvento() == evento && !(inscricao instanceof InscricaoCancelada)) {
                inscricaoParaCertificado = inscricao;
                break;
            }
        }

        if (inscricaoParaCertificado == null) {
            return "ERRO: Nenhuma inscrição encontrada para este evento.";
        }

        for (Certificado certificado : meusCertificados) {
            if (certificado.inscricao().getIdInscricao() == inscricaoParaCertificado.getIdInscricao()) {
                return "INFO: Você já possui este certificado.\n   -> " + certificado.toString();
            }
        }

        Certificado certificado = gestor.emitirCertificado(inscricaoParaCertificado);
        
        if (certificado != null) {
            meusCertificados.add(certificado);
            return "SUCESSO: Certificado emitido!\n   -> " + certificado.toString();
        } else {
            return "NEGADO: Você não tem presença confirmada neste evento para gerar certificado.";
        }
    }

    @Override
    public List<Certificado> getCertificados() {
        return new ArrayList<>(this.meusCertificados);
    }

    @Override public String nome() { return nome; }
    @Override public String email() { return email; }
    public List<IInscricao> getInscricoes(){ return minhasInscricoes; }
}