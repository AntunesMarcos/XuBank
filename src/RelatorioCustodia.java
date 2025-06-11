import java.util.List;

public class RelatorioCustodia {
    private List<Cliente> clientes;

    public RelatorioCustodia(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void ImprimirRelatorio() {
        System.out.println("\n=== RELATÓRIO DE CUSTÓDIA ===");
        System.out.println(RelatorioCustodia());
    }

    public String RelatorioCustodia() {
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
                    case 1 -> totalCorrente += saldo;
                    case 2 -> totalPoupanca += saldo;
                    case 3 -> totalRendaFixa += saldo;
                    case 4 -> totalInvestimento += saldo;
                }
            }
        }

        return String.format(
                "Saldo em custódia:\nCorrente: R$ %.2f\nPoupança: R$ %.2f\nRenda Fixa: R$ %.2f\nInvestimento: R$ %.2f",
                totalCorrente, totalPoupanca, totalRendaFixa, totalInvestimento);
    }

    public String EncontrarClientesExtremos() {
        if (clientes.isEmpty()) {
            return "Nenhum cliente cadastrado.";
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

        return String.format("Cliente com maior saldo: %s - R$ %.2f\nCliente com menor saldo: %s - R$ %.2f",
                maior.getNome(), saldoMaior, menor.getNome(), saldoMenor);
    }
}