public class ContaInvestimento extends Conta implements iRendimento {
    public ContaInvestimento(Cliente cliente) throws SecurityException {
        super(cliente);
        this.tipoConta = 4;
    }

    @Override
    public double CalcularRendimento() throws SecurityException {
        double rendimento = saldo * 0.015;
        if (!ValidationUtils.validarValor(rendimento)) {
            throw new SecurityException("Rendimento inválido");
        }
        saldo += rendimento;
        return rendimento;
    }

    public double ObterAnoRendimento() {
        return 0.015;
    }

    @Override
    public String getTipoContaNome() {
        return "ContaInvestimento";
    }
}