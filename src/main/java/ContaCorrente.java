public class ContaCorrente extends Conta {
    private double limite;

    public ContaCorrente(Cliente cliente) throws SecurityException {
        super(cliente);
        this.tipoConta = 1;
        this.limite = calcularLimite();
    }

    public double calcularLimite() throws SecurityException {
        try {
            double rendaMensal = cliente.getRendaMensal();
            if (!ValidationUtils.validarValor(rendaMensal)) {
                throw new SecurityException("Renda mensal inválida para cálculo de limite");
            }

            double limiteCalculado = rendaMensal * 0.4;
            return Math.max(limiteCalculado, 100.0);
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_CALCULO_LIMITE",
                    "Erro ao calcular limite - Conta: " + numero, e);
            throw new SecurityException("Erro ao calcular limite", e);
        }
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

        if (valor > (saldo + limite)) {
            SecurityLogger.logSecurityEvent("SAQUE_NEGADO",
                    "Saque negado por limite insuficiente - Conta: " + numero);
            return false;
        }

        saldo -= valor;
        SecurityLogger.logSecurityEvent("SAQUE_REALIZADO",
                "Saque realizado em conta corrente - Conta: " + numero + " Valor: " + String.format("%.2f", valor));
        return true;
    }

    @Override
    public boolean Depositar(double valor) throws SecurityException {
        if (!ValidationUtils.validarValor(valor)) {
            SecurityLogger.logSecurityEvent("DEPOSITO_INVALIDO",
                    "Tentativa de depósito com valor inválido: " + valor);
            throw new SecurityException("Valor de depósito inválido");
        }

        if (valor <= 0) {
            return false;
        }

        try {
            if (saldo < 0) {
                double saldoNegativo = Math.abs(saldo);
                double taxa = saldoNegativo * 0.03 + 10;

                if (!ValidationUtils.validarValor(taxa)) {
                    throw new SecurityException("Taxa calculada inválida");
                }

                saldo += valor - taxa;
                SecurityLogger.logSecurityEvent("TAXA_APLICADA",
                        "Taxa de " + String.format("%.2f", taxa) + " aplicada - Conta: " + numero);
            } else {
                saldo += valor;
            }

            SecurityLogger.logSecurityEvent("DEPOSITO_REALIZADO",
                    "Depósito realizado em conta corrente - Conta: " + numero + " Valor: " + String.format("%.2f", valor));
            return true;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_DEPOSITO",
                    "Erro ao processar depósito - Conta: " + numero, e);
            throw new SecurityException("Erro ao processar depósito", e);
        }
    }

    @Override
    public double CalcularRendimento() throws SecurityException {
        return 0; // Conta corrente não rende
    }

    @Override
    public String getTipoContaNome() {
        return "ContaCorrente";
    }

    public double getLimite() {
        return limite;
    }

    public void atualizarLimite() throws SecurityException {
        try {
            this.limite = calcularLimite();
            SecurityLogger.logSecurityEvent("LIMITE_ATUALIZADO",
                    "Limite atualizado - Conta: " + numero + " Novo limite: " + String.format("%.2f", limite));
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_ATUALIZACAO_LIMITE",
                    "Erro ao atualizar limite - Conta: " + numero, e);
            throw new SecurityException("Erro ao atualizar limite", e);
        }
    }

    @Override
    public String GerarExtrato() {
        return super.GerarExtrato() + String.format(" - Limite especial: R$ %.2f", limite);
    }
}