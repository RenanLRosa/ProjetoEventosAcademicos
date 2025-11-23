import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoAberto implements IEvento {
    private InformacaoEvento informacoes;
    private Sala sala;
    private List<Integer> idInscricoes;
    private int idItemFila;

    public Fila fila = new Fila(); 

    public EventoAberto(InformacaoEvento informacoes, Sala sala) {
        this.informacoes = informacoes;
        this.sala = sala;
        this.idInscricoes = new ArrayList<>();
    }

    public EventoConcluido concluir() {
        if (LocalDateTime.now().isBefore(this.dataInicial())) {
            return null; // ou lançar Exception
        }
        return new EventoConcluido(this);
    }

    public EventoCancelado cancelar(String motivo) {
        if (LocalDateTime.now().isAfter(this.dataFinal())) {
            return null; // ou lançar Exception
        }
        return new EventoCancelado(this, motivo);
    }
    
    public void adicionarInscricao(int id){ idInscricoes.add(id); }
    public void cancelarInscricao(int id){ idInscricoes.remove(Integer.valueOf(id)); }
    public int numeroDeInscricoes(){ return idInscricoes.size(); }
    public void removerDaFila(int id){ fila.remover(id); }
    public void adicionarNaFila(InscricaoEmFila inscricao){ fila.adicionar(inscricao); }

    public int movimentarFila(){
        idItemFila = fila.getIdMaisAntigo();
        fila.remover(idItemFila);
        return idItemFila;
    }

    @Override public Sala sala() { return this.sala; }
    @Override public InformacaoEvento informacoes() { return this.informacoes; }
    @Override public String nome() { return informacoes.getNome(); }
    @Override public String descricao() { return informacoes.getDescricao(); }
    @Override public LocalDateTime dataInicial() { return informacoes.getDataInicial(); }
    @Override public LocalDateTime dataFinal() { return informacoes.getDataFinal(); }
}