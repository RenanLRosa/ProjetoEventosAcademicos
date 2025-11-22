public interface IInscricao {
    boolean marcarPresenca();
    boolean solicitarCancelamento();
    void tornarConfirmado();
    int getIdInscricao();
    IEvento getEvento();
}
