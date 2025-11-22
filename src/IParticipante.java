public interface IParticipante extends IUsuario {
    void inscrever(EventoAberto evento);
    void cancelarInscricao(EventoAberto evento);
}
