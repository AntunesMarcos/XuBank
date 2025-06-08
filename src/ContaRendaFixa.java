import java.security.SecureRandom;

public class ContaRendaFixa extends Conta implements TaxaImposto {
    private static final double TAXA_MIN = 0.5;
    private static final double TAXA_MAX = 0.85;
    private static final double TAXA_FIXA = 20.0;
    private static final SecureRandom RANDOM = new SecureRandom();

    public ContaRendaFixa(Cliente cliente) throws SecurityException {
        super(cliente);
        this.tipoConta = 3;
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

            SecurityLogger.logSecurityEvent("SAQUE_RENDA_FIXA",
                    "Saque realizado em renda fixa - Conta: " + numero +
                            " Valor: " + String.format("%.2f", valor) +
                            " Imposto: " + String.format("%.2f", imposto));
            return true;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_SAQUE_RENDA_FIXA",
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

            double variacao = TAXA_MIN + (TAXA_MAX - TAXA_MIN) * RANDOM.nextDouble();
            double rendimento = saldo * (variacao / 100.0);

            if (!ValidationUtils.validarValor(rendimento) || !ValidationUtils.validarValor(TAXA_FIXA)) {
                throw new SecurityException("Valores calculados inválidos");
            }

            double novoSaldo = saldo + rendimento - TAXA_FIXA;
            setSaldo(Math.max(novoSaldo, 0)); // Não permite saldo negativo

            SecurityLogger.logSecurityEvent("RENDIMENTO_RENDA_FIXA",
                    "Rendimento calculado - Conta: " + numero +
                            " Rendimento: " + String.format("%.2f", rendimento) +
                            " Taxa: " + String.format("%.2f", TAXA_FIXA));

            return rendimento;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RENDIMENTO_RENDA_FIXA",
                    "Erro ao calcular rendimento - Conta: " + numero, e);
            throw new SecurityException("Erro ao calcular rendimento", e);
        }
    }

    @Override
    public String getTipoContaNome() {
        return "ContaRendaFixa";
    }

    @Override
    public double aplicarImposto(double rendimento) throws SecurityException {
        try {
            if (!ValidationUtils.validarValor(rendimento)) {
                throw new SecurityException("Rendimento inválido para cálculo de imposto");
            }

            double imposto = rendimento * 0.15;

            if (!ValidationUtils.validarValor(imposto)) {
                throw new SecurityException("Imposto calculado inválido");
            }

            return imposto;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_CALCULO_IMPOSTO",
                    "Erro ao calcular imposto - Conta: " + numero, e);
            throw new SecurityException("Erro ao calcular imposto", e);
        }
    }
}