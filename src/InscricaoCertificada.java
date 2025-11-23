public class InscricaoCertificada implements IInscricao {
    private int id;

    public InscricaoCertificada(InscricaoConfirmada inscricao){
        this.id = inscricao.getIdInscricao();
    }

    @Override
    public void marcarPresenca() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'marcarPresenca'");
    }

    @Override
    public boolean solicitarCancelamento() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solicitarCancelamento'");
    }

    @Override
    public void tornarConfirmado() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tornarConfirmado'");
    }

    @Override
    public int getIdInscricao() {
        return id;
    }

    @Override
    public IEvento getEvento() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEvento'");
    }

    @Override
    public boolean temPresenca() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'temPresenca'");
    }

}
