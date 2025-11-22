public class InscricaoCancelada implements IInscricao {

    private IInscricao inscricaoOriginal;

    public InscricaoCancelada(IInscricao inscricao) {
        this.inscricaoOriginal = inscricao;
    }

    @Override
    public boolean solicitarCancelamento() {
        System.err.println("AVISO: Esta inscrição já está cancelada.");
        return false;
    }

    @Override
    public void tornarConfirmado() {
        System.err.println("AVISO: Não é possível confirmar uma inscrição cancelada.");
    }

    @Override
    public int getIdInscricao() {
        return inscricaoOriginal.getIdInscricao();
    }

    @Override
    public boolean marcarPresenca() {
        System.err.println("AVISO: Não é possível marcar presença em uma inscrição cancelada.");
        return false;
    }

    @Override
    public IEvento getEvento() {
        return inscricaoOriginal.getEvento();
    }
}