import java.time.LocalDateTime;

public interface IEvento {
    String nome();
    String descricao();
    LocalDateTime dataInicial();
    LocalDateTime dataFinal();
    Sala sala();
    InformacaoEvento informacoes();
}
