import java.util.LinkedHashMap;
import java.util.Map;

public class Fila {
    // LinkedHashMap mantém a ordem de inserção (essencial para fila)
    private Map<Integer, InscricaoEmFila> fila = new LinkedHashMap<>();

    public void adicionar(InscricaoEmFila inscricao){
        fila.put(inscricao.getIdInscricao(), inscricao);
    }

    public void remover(InscricaoEmFila inscricao){
        fila.remove(inscricao.getIdInscricao());
    }

    public void remover(int id){
        fila.remove(id);
    }

    public int getIdMaisAntigo(){
        // Retorna o ID do primeiro item na ordem de inserção
        return fila.keySet().iterator().next();
    }
    
    // *** MÉTODO ADICIONADO (Necessário para o Gestor) ***
    public boolean temFila() {
        return !fila.isEmpty();
    }
}