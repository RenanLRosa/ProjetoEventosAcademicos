import java.time.LocalDateTime;

public class InformacaoEvento {
    private String nome;
    private String descricao;
    private Sala sala;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;

    public InformacaoEvento(String nome, String descricao, Sala sala, LocalDateTime dataInicial, LocalDateTime dataFinal) { //Data inicial e final são as datas de concepção do evento(fins de ilustraçaõ)
        this.nome = nome;
        this.descricao = descricao;
        this.sala = sala;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataInicial() {
        return dataInicial;
    }

    public LocalDateTime getDataFinal() {
        return dataFinal;
    }

}
