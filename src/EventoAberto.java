import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoAberto implements IEvento {
    private InformacaoEvento informacoes;
    private Sala sala;
    private List<Integer> idInscricoes; // Lista de IDs de Inscrições Confirmadas
    private int idItemFila;

    // A Fila agora é pública para o Gestor e o App acessarem
    public Fila fila = new Fila(); 

    public EventoAberto(InformacaoEvento informacoes, Sala sala) {
        this.informacoes = informacoes;
        this.sala = sala;
        this.idInscricoes = new ArrayList<Integer>();
    }

    // ... (métodos concluir() e cancelar()) ...
    public EventoConcluido concluir() {
        if (LocalDateTime.now().isBefore(this.dataInicial())) {
            System.err.println("ERRO: Não é possível concluir um evento que ainda nem começou.");
            return null;
        }
        System.out.println("Evento '" + nome() + "' concluído e 'congelado'.");
        return new EventoConcluido(this);
    }

    public EventoCancelado cancelar(String motivo) {
        if (LocalDateTime.now().isAfter(this.dataFinal())) {
            System.err.println("ERRO: Não é possível cancelar um evento que já terminou.");
            return null;
        }
        System.out.println("Evento '" + nome() + "' foi cancelado.");
        return new EventoCancelado(this, motivo);
    }
    
    // Adiciona na lista de confirmados
    public void adicionarInscricao(int id){
        idInscricoes.add(id);
    }

    // Remove da lista de confirmados (quando cancela)
    public void cancelarInscricao(int id){
        // Precisa iterar e remover o Integer correto
        idInscricoes.remove(Integer.valueOf(id));
    }

    public int numeroDeInscricoes(){
        return idInscricoes.size();
    }

    // Métodos da Fila
    public void removerDaFila(int id){
        fila.remover(id);
    }

    public void adicionarNaFila(InscricaoEmFila inscricao){
        fila.adicionar(inscricao);
    }

    // Retorna o ID mais antigo E remove da fila
    public int movimentarFila(){
        idItemFila = fila.getIdMaisAntigo();
        fila.remover(idItemFila);
        return idItemFila;
    }

    // Getters da Interface IEvento
    @Override
    public Sala sala() {
        return this.sala;
    }

    @Override
    public InformacaoEvento informacoes() {
        return this.informacoes;
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
}