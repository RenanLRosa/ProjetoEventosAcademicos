public interface IAdministrador extends IUsuario {
    void criarEvento(IEvento evento);
    boolean cancelarEvento(IEvento evento); // Agora retorna se obteve sucesso
}