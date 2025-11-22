public class Certificado {
    private InscricaoConfirmada inscricao;
    public Certificado(InscricaoConfirmada inscricao){
        this.inscricao = inscricao;
     }

     public InscricaoConfirmada inscricao(){
        return inscricao;
     }
}
