public class ContaInvestimento extends Conta implements iRendimento {

    public ContaInvestimento(Cliente cliente) throws SecurityException {
        super(cliente);
        this.tipoConta = 4;
    }

    @Override
    public double CalcularRendimento() throws SecurityException {
        try {
            double rendimento = saldo * 0.015;

            if (!ValidationUtils.validarValor(rendimento)) {
                throw new SecurityException("Rendimento calculado inválido");
            }

            double imposto = TaxaImposto.AplicarImposto(rendimento);
            double rendimentoLiquido = rendimento - imposto;

            saldo += rendimentoLiquido;

            SecurityLogger.logSecurityEvent("RENDIMENTO_APLICADO",
                    "Rendimento aplicado em investimento - Conta: " + numero +
                            " Rendimento bruto: " + String.format("%.2f", rendimento) +
                            " Imposto: " + String.format("%.2f", imposto) +
                            " Rendimento líquido: " + String.format("%.2f", rendimentoLiquido));

            return rendimentoLiquido;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RENDIMENTO_INVESTIMENTO",
                    "Erro ao calcular rendimento - Conta: " + numero, e);
            throw new SecurityException("Erro ao calcular rendimento", e);
        }
    }

    public double ObteranoRendimento() {
        // Retorna o rendimento mensal do investimento (1.5%)
        return 0.015;
    }

    public void VerificarRentabilidade() {
        System.out.printf("Rentabilidade da conta %d: %.2f%% ao mês%n", numero, ObteranoRendimento() * 100);
    }

    @Override
    public String getTipoContaNome() {
        return "ContaInvestimento";
    }
}