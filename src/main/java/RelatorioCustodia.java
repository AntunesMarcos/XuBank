import java.util.List;


public class RelatorioCustodia {

    private final List<Cliente> clientes;


    public RelatorioCustodia(List<Cliente> clientes) {
        this.clientes = clientes;
    }


    public String gerarRelatorioCustodiaTotal() {
        try {
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
                        case 1:
                            totalCorrente += saldo;
                            break;
                        case 2:
                            totalPoupanca += saldo;
                            break;
                        case 3:
                            totalRendaFixa += saldo;
                            break;
                        case 4:
                            totalInvestimento += saldo;
                            break;
                    }
                }
            }

            SecurityLogger.logSecurityEvent("RELATORIO_CUSTODIA", "Relatório de custódia gerado");

            return String.format(
                    "Saldo em custódia:\nCorrente: R$ %.2f\nPoupança: R$ %.2f\nRenda Fixa: R$ %.2f\nInvestimento: R$ %.2f",
                    totalCorrente, totalPoupanca, totalRendaFixa, totalInvestimento);

        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RELATORIO_CUSTODIA",
                    "Erro ao gerar relatório de custódia", e);
            return "Erro ao gerar relatório de custódia.";
        }
    }


    public String gerarRelatorioClientesExtremos() {
        try {
            if (clientes.isEmpty()) {
                return "Nenhum cliente cadastrado.";
            }

           
            Cliente maior = clientes.get(0);
            Cliente menor = clientes.get(0);
            double saldoMaior = maior.getSaldoTotal();
            double saldoMenor = menor.getSaldoTotal();

            
            for (Cliente cliente : clientes) {
                double saldoTotal = cliente.getSaldoTotal();

               
                if (saldoTotal > saldoMaior) {
                    maior = cliente;
                    saldoMaior = saldoTotal;
                }
               
                if (saldoTotal < saldoMenor) {
                    menor = cliente;
                    saldoMenor = saldoTotal;
                }
            }

            SecurityLogger.logSecurityEvent("RELATORIO_EXTREMOS", "Relatório de clientes extremos gerado");

            return String.format("Cliente com maior saldo: %s - R$ %.2f\nCliente com menor saldo: %s - R$ %.2f",
                    ValidationUtils.sanitizarString(maior.getNome()), saldoMaior,
                    ValidationUtils.sanitizarString(menor.getNome()), saldoMenor);

        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RELATORIO_EXTREMOS",
                    "Erro ao gerar relatório de extremos", e);
            return "Erro ao gerar relatório de extremos.";
        }
    }
}
