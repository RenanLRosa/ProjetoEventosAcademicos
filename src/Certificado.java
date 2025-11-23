public class Certificado {
    private IInscricao inscricao;
    
    public Certificado(IInscricao inscricao){
       this.inscricao = inscricao;
     }

     public IInscricao inscricao(){
       return inscricao;
     }

     @Override
     public String toString() {
         return "Certificado Válido [Participante: " + ((InscricaoConfirmada)inscricao).getParticipante().nome() + 
                " | Evento: " + inscricao.getEvento().nome() + 
                " | ID Inscrição: " + inscricao.getIdInscricao() + "]";
     }
}