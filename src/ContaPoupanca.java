public class ContaPoupanca extends Conta {
    private static final double TAXA_RENDIMENTO = 0.006;

    public ContaPoupanca(Cliente cliente) throws SecurityException {
        super(cliente);
        this.tipoConta = 2;
    }

    @Override
    public double CalcularRendimento() throws SecurityException {
        try {
            if (!ValidationUtils.validarValor(saldo)) {
                throw new SecurityException("Saldo inválido para cálculo de rendimento");
            }

            double rendimento = saldo * TAXA_RENDIMENTO;

            if (!ValidationUtils.validarValor(rendimento)) {
                throw new SecurityException("Rendimento calculado inválido");
            }

            setSaldo(saldo + rendimento);

            SecurityLogger.logSecurityEvent("RENDIMENTO_CALCULADO",
                    "Rendimento calculado em poupança - Conta: " + numero + " Valor: " + String.format("%.2f", rendimento));

            return rendimento;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RENDIMENTO_POUPANCA",
                    "Erro ao calcular rendimento - Conta: " + numero, e);
            throw new SecurityException("Erro ao calcular rendimento", e);
        }
    }

    @Override
    public String getTipoContaNome() {
        return "ContaPoupanca";
    }
}