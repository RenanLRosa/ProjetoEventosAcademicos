public interface IInscricao {
    void marcarPresenca();
    boolean temPresenca();
    boolean solicitarCancelamento();
    void tornarConfirmado();
    int getIdInscricao();
    IEvento getEvento();
}
