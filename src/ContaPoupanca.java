public class ContaPoupanca extends Conta implements iRendimento {
    public ContaPoupanca(Cliente cliente) throws SecurityException {
        super(cliente);
        this.tipoConta = 2;
    }

    @Override
    public double CalcularRendimento() throws SecurityException {
        double rendimento = saldo * 0.005;
        if (!ValidationUtils.validarValor(rendimento)) {
            throw new SecurityException("Rendimento inválido");
        }
        saldo += rendimento;
        return rendimento;
    }

    @Override
    public String getTipoContaNome() {
        return "ContaPoupanca";
    }
}