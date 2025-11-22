public interface IAdministrador extends IUsuario {
    void criarEvento(IEvento evento);
    void cancelarEvento(IEvento evento);

}
