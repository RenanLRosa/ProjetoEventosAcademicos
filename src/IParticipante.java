import java.util.List;

public interface IParticipante extends IUsuario {
    String inscrever(EventoAberto evento);
    String cancelarInscricao(EventoAberto evento);
    String marcarPresenca(EventoAberto evento);
    String requisitarCertificado(EventoAberto evento);
    List<Certificado> getCertificados();
}