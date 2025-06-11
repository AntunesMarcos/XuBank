import java.security.SecureRandom;

public class ContaInvestimento extends Conta implements TaxaImposto,IRendimento {
    private static final double VARIACAO_MIN = -0.6;
    private static final double VARIACAO_MAX = 1.5;
    private static final double TAXA_ADMINISTRACAO = 0.01;
    private static final double TAXA_IMPOSTO = 0.225;
    private static final SecureRandom RANDOM = new SecureRandom();

    public ContaInvestimento(Cliente cliente) throws SecurityException {
        super(cliente);
        this.tipoConta = 4;
    }

    @Override
    public boolean Sacar(double valor) throws SecurityException {
        if (!ValidationUtils.validarValor(valor)) {
            SecurityLogger.logSecurityEvent("SAQUE_INVALIDO",
                    "Tentativa de saque com valor inválido: " + valor);
            throw new SecurityException("Valor de saque inválido");
        }

        if (valor <= 0) {
            return false;
        }

        try {
            double rendimento = CalcularRendimento();
            double imposto = aplicarImposto(rendimento);
            double valorTotal = valor + imposto;

            if (valorTotal > saldo) {
                SecurityLogger.logSecurityEvent("SAQUE_NEGADO",
                        "Saque negado por saldo insuficiente - Conta: " + numero);
                return false;
            }

            setSaldo(saldo - valorTotal);

            SecurityLogger.logSecurityEvent("SAQUE_INVESTIMENTO",
                    "Saque realizado em investimento - Conta: " + numero +
                            " Valor: " + String.format("%.2f", valor) +
                            " Imposto: " + String.format("%.2f", imposto));
            return true;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_SAQUE_INVESTIMENTO",
                    "Erro ao processar saque - Conta: " + numero, e);
            throw new SecurityException("Erro ao processar saque", e);
        }
    }

    @Override
    public double CalcularRendimento() throws SecurityException {
        try {
            if (!ValidationUtils.validarValor(saldo)) {
                throw new SecurityException("Saldo inválido para cálculo de rendimento");
            }

            double variacao = VARIACAO_MIN + (VARIACAO_MAX - VARIACAO_MIN) * RANDOM.nextDouble();
            double rendimento = saldo * (variacao / 100.0);

            if (!ValidationUtils.validarValor(rendimento)) {
                throw new SecurityException("Rendimento calculado inválido");
            }

            double novoSaldo = saldo + rendimento;

            // Aplicar taxa de administração apenas se houve rendimento positivo
            if (rendimento > 0) {
                double taxa = rendimento * TAXA_ADMINISTRACAO;
                if (!ValidationUtils.validarValor(taxa)) {
                    throw new SecurityException("Taxa de administração inválida");
                }
                novoSaldo -= taxa;
            }

            setSaldo(Math.max(novoSaldo, 0)); // Não permite saldo negativo

            SecurityLogger.logSecurityEvent("RENDIMENTO_INVESTIMENTO",
                    "Rendimento calculado - Conta: " + numero +
                            " Rendimento: " + String.format("%.2f", rendimento));

            return rendimento;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RENDIMENTO_INVESTIMENTO",
                    "Erro ao calcular rendimento - Conta: " + numero, e);
            throw new SecurityException("Erro ao calcular rendimento", e);
        }
    }

    public boolean VerificarRendimento() {
        return saldo > 0;
    }

    @Override
    public String getTipoContaNome() {
        return "ContaInvestimento";
    }

    @Override
    public double aplicarImposto(double rendimento) throws SecurityException {
        try {
            if (!ValidationUtils.validarValor(rendimento)) {
                throw new SecurityException("Rendimento inválido para cálculo de imposto");
            }

            double imposto = rendimento > 0 ? rendimento * TAXA_IMPOSTO : 0;

            if (!ValidationUtils.validarValor(imposto)) {
                throw new SecurityException("Imposto calculado inválido");
            }

            return imposto;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_CALCULO_IMPOSTO_INVESTIMENTO",
                    "Erro ao calcular imposto - Conta: " + numero, e);
            throw new SecurityException("Erro ao calcular imposto", e);
        }
    }
}