import java.time.LocalDateTime;

public class EventoConcluido implements IEvento{
    private InformacaoEvento informacoes;
    private Sala sala;
    private LocalDateTime dataDeConclusao;

    protected EventoConcluido(EventoAberto eventoAberto){
        this.informacoes = eventoAberto.informacoes();
        this.sala = eventoAberto.sala();
        this.dataDeConclusao = LocalDateTime.now();
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

    public LocalDateTime getDataDeConclusao(){
        return dataDeConclusao;
    }

}
