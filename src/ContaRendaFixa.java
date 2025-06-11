public class ContaRendaFixa extends Conta implements iRendimento {

    public ContaRendaFixa(Cliente cliente) throws SecurityException {
        super(cliente);
        this.tipoConta = 3;
    }

    @Override
    public double CalcularRendimento() throws SecurityException {
        try {
            double rendimento = saldo * 0.01; // 1% ao mês

            if (!ValidationUtils.validarValor(rendimento)) {
                throw new SecurityException("Rendimento calculado inválido");
            }

            double imposto = TaxaImposto.AplicarImposto(rendimento);
            double rendimentoLiquido = rendimento - imposto;

            saldo += rendimentoLiquido;

            SecurityLogger.logSecurityEvent("RENDIMENTO_APLICADO",
                    "Rendimento aplicado em renda fixa - Conta: " + numero +
                            " Rendimento bruto: " + String.format("%.2f", rendimento) +
                            " Imposto: " + String.format("%.2f", imposto) +
                            " Rendimento líquido: " + String.format("%.2f", rendimentoLiquido));

            return rendimentoLiquido;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RENDIMENTO_RENDA_FIXA",
                    "Erro ao calcular rendimento - Conta: " + numero, e);
            throw new SecurityException("Erro ao calcular rendimento", e);
        }
    }

    public double ObteranoMes() {
        // Retorna o rendimento mensal da renda fixa (1%)
        return 0.01;
    }

    @Override
    public String getTipoContaNome() {
        return "ContaRendaFixa";
    }
}