import java.util.List;

public class RelatorioCustodia {
    private List<Cliente> clientes;

    public RelatorioCustodia(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void ImprimirRelatorio() {
        System.out.println("\n=== RELATÓRIO DE CUSTÓDIA ===");
        System.out.println(RelatorioeCustodia());
    }

    public void CalcularCustodaPorTipo() {
        double totalCorrente = 0, totalPoupanca = 0, totalRendaFixa = 0, totalInvestimento = 0;

        for (Cliente cliente : clientes) {
            List<Conta> contas = cliente.getContas();

            for (Conta conta : contas) {
                double saldo = conta.getSaldo();

                if (!ValidationUtils.validarValor(saldo)) {
                    SecurityLogger.logError("SALDO_INVALIDO_RELATORIO",
                            "Saldo inválido encontrado na conta: " + conta.getNumero(), null);
                    continue;
                }

                switch (conta.getTipoConta()) {
                    case 1: totalCorrente += saldo; break;
                    case 2: totalPoupanca += saldo; break;
                    case 3: totalRendaFixa += saldo; break;
                    case 4: totalInvestimento += saldo; break;
                }
            }
        }

        System.out.println("Custódia por tipo de conta:");
        System.out.printf("Corrente: R$ %.2f%n", totalCorrente);
        System.out.printf("Poupança: R$ %.2f%n", totalPoupanca);
        System.out.printf("Renda Fixa: R$ %.2f%n", totalRendaFixa);
        System.out.printf("Investimento: R$ %.2f%n", totalInvestimento);
    }

    public void CalcularSaldoMedioGeral() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        double saldoTotal = 0;
        int totalContas = 0;

        for (Cliente cliente : clientes) {
            List<Conta> contas = cliente.getContas();
            for (Conta conta : contas) {
                saldoTotal += conta.getSaldo();
                totalContas++;
            }
        }

        double saldoMedio = totalContas > 0 ? saldoTotal / totalContas : 0;
        System.out.printf("Saldo médio geral: R$ %.2f%n", saldoMedio);
    }

    public void EncontrarClientesExtremos() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        Cliente maior = clientes.get(0);
        Cliente menor = clientes.get(0);
        double saldoMaior = maior.GetSaldoTotal();
        double saldoMenor = menor.GetSaldoTotal();

        for (Cliente cliente : clientes) {
            double saldoTotal = cliente.GetSaldoTotal();

            if (!ValidationUtils.validarValor(saldoTotal)) {
                SecurityLogger.logError("SALDO_INVALIDO_EXTREMOS",
                        "Saldo inválido para cliente: " + cliente.getCpfOfuscado(), null);
                continue;
            }

            if (saldoTotal > saldoMaior) {
                maior = cliente;
                saldoMaior = saldoTotal;
            }
            if (saldoTotal < saldoMenor) {
                menor = cliente;
                saldoMenor = saldoTotal;
            }
        }

        System.out.printf("Cliente com maior saldo: %s - R$ %.2f%n",
                ValidationUtils.sanitizarString(maior.getNome()), saldoMaior);
        System.out.printf("Cliente com menor saldo: %s - R$ %.2f%n",
                ValidationUtils.sanitizarString(menor.getNome()), saldoMenor);
    }

    private String RelatorioeCustodia() {
        double totalCorrente = 0, totalPoupanca = 0, totalRendaFixa = 0, totalInvestimento = 0;

        for (Cliente cliente : clientes) {
            List<Conta> contas = cliente.getContas();

            for (Conta conta : contas) {
                double saldo = conta.getSaldo();

                if (!ValidationUtils.validarValor(saldo)) {
                    SecurityLogger.logError("SALDO_INVALIDO_RELATORIO",
                            "Saldo inválido encontrado na conta: " + conta.getNumero(), null);
                    continue;
                }

                switch (conta.getTipoConta()) {
                    case 1: totalCorrente += saldo; break;
                    case 2: totalPoupanca += saldo; break;
                    case 3: totalRendaFixa += saldo; break;
                    case 4: totalInvestimento += saldo; break;
                }
            }
        }

        return String.format(
                "Saldo em custódia:\nCorrente: R$ %.2f\nPoupança: R$ %.2f\nRenda Fixa: R$ %.2f\nInvestimento: R$ %.2f",
                totalCorrente, totalPoupanca, totalRendaFixa, totalInvestimento);
    }
}