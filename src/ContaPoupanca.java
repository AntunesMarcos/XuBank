import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ContaPoupanca extends Conta implements iRendimento {

    public ContaPoupanca(Cliente cliente) throws SecurityException {
        super(cliente);
        this.tipoConta = 2;
    }

    @Override
    public double CalcularRendimento() throws SecurityException {
        try {
            double rendimento = saldo * 0.005; // 0.5% ao mês

            if (!ValidationUtils.validarValor(rendimento)) {
                throw new SecurityException("Rendimento calculado inválido");
            }

            double imposto = TaxaImposto.AplicarImposto(rendimento);
            double rendimentoLiquido = rendimento - imposto;

            saldo += rendimentoLiquido;

            SecurityLogger.logSecurityEvent("RENDIMENTO_APLICADO",
                    "Rendimento aplicado em poupança - Conta: " + numero +
                            " Rendimento bruto: " + String.format("%.2f", rendimento) +
                            " Imposto: " + String.format("%.2f", imposto) +
                            " Rendimento líquido: " + String.format("%.2f", rendimentoLiquido));

            return rendimentoLiquido;
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

    public double ObteranoMes() {
        // Retorna o rendimento mensal da poupança (0.5%)
        return 0.005;
    }
}