import java.time.LocalDateTime;

public class EventoCancelado implements IEvento{
    private InformacaoEvento informacoes;
    private Sala sala;
    private LocalDateTime dataDeCancelamento;
    private String motivo;

    protected EventoCancelado(EventoAberto eventoAberto, String motivo){
        this.informacoes = eventoAberto.informacoes();
        this.sala = eventoAberto.sala();
        this.dataDeCancelamento = LocalDateTime.now();
        this.motivo = motivo;
    }

    @Override
    public String nome() {
        return informacoes.getNome();
    }

    @Override
    public String descricao() {
        return informacoes.getDescricao();
    }

    @Override
    public LocalDateTime dataInicial() {
        return informacoes.getDataInicial();
    }

    @Override
    public LocalDateTime dataFinal() {
        return informacoes.getDataFinal();
    }

    @Override
    public Sala sala() {
        return this.sala;
    }

    @Override
    public InformacaoEvento informacoes() {
        return this.informacoes;
    }

    public LocalDateTime getDataDeCancelamento() {
        return dataDeCancelamento;
    }

    public String getMotivo() {
        return motivo;
    }
}
