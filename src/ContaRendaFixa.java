public class ContaRendaFixa extends Conta implements iRendimento {
    public ContaRendaFixa(Cliente cliente) throws SecurityException {
        super(cliente);
        this.tipoConta = 3;
    }

    @Override
    public double CalcularRendimento() throws SecurityException {
        double rendimento = saldo * 0.01;
        if (!ValidationUtils.validarValor(rendimento)) {
            throw new SecurityException("Rendimento inválido");
        }
        saldo += rendimento;
        return rendimento;
    }

    @Override
    public String getTipoContaNome() {
        return "ContaRendaFixa";
    }
}