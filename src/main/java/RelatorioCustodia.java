import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

    

    public String gerarRelatorioSaldoMedio() {
        try {

            Map<Integer, Double> saldosTotaisPorTipo = new HashMap<>();
            Map<Integer, Integer> contagemContasPorTipo = new HashMap<>();


            saldosTotaisPorTipo.put(1, 0.0);
            saldosTotaisPorTipo.put(2, 0.0);
            saldosTotaisPorTipo.put(3, 0.0);
            saldosTotaisPorTipo.put(4, 0.0);

            contagemContasPorTipo.put(1, 0);
            contagemContasPorTipo.put(2, 0);
            contagemContasPorTipo.put(3, 0);
            contagemContasPorTipo.put(4, 0);

            for (Cliente cliente : clientes) {
                List<Conta> contas = cliente.getContas();
                for (Conta conta : contas) {
                    double saldo = conta.getSaldo();
                    int tipoConta = conta.getTipoConta();

                    if (!ValidationUtils.validarValor(saldo)) {
                        SecurityLogger.logError("SALDO_INVALIDO_RELATORIO_MEDIO",
                                "Saldo inválido encontrado na conta (saldo médio): " + conta.getNumero(), null);
                        continue;
                    }

                    // Adiciona o saldo ao total do tipo e incrementa a contagem
                    saldosTotaisPorTipo.put(tipoConta, saldosTotaisPorTipo.getOrDefault(tipoConta, 0.0) + saldo);
                    contagemContasPorTipo.put(tipoConta, contagemContasPorTipo.getOrDefault(tipoConta, 0) + 1);
                }
            }

            StringBuilder relatorio = new StringBuilder();
            relatorio.append("--- Saldo Médio por Tipo de Conta ---\n");


            double saldoMedioCorrente = contagemContasPorTipo.get(1) > 0 ?
                    saldosTotaisPorTipo.get(1) / contagemContasPorTipo.get(1) : 0.0;
            relatorio.append(String.format("Corrente: R$ %.2f (Baseado em %d conta(s))\n", saldoMedioCorrente, contagemContasPorTipo.get(1)));


            double saldoMedioPoupanca = contagemContasPorTipo.get(2) > 0 ?
                    saldosTotaisPorTipo.get(2) / contagemContasPorTipo.get(2) : 0.0;
            relatorio.append(String.format("Poupança: R$ %.2f (Baseado em %d conta(s))\n", saldoMedioPoupanca, contagemContasPorTipo.get(2)));


            double saldoMedioRendaFixa = contagemContasPorTipo.get(3) > 0 ?
                    saldosTotaisPorTipo.get(3) / contagemContasPorTipo.get(3) : 0.0;
            relatorio.append(String.format("Renda Fixa: R$ %.2f (Baseado em %d conta(s))\n", saldoMedioRendaFixa, contagemContasPorTipo.get(3)));


            double saldoMedioInvestimento = contagemContasPorTipo.get(4) > 0 ?
                    saldosTotaisPorTipo.get(4) / contagemContasPorTipo.get(4) : 0.0;
            relatorio.append(String.format("Investimento: R$ %.2f (Baseado em %d conta(s))\n", saldoMedioInvestimento, contagemContasPorTipo.get(4)));

            relatorio.append("--------------------------------------");

            SecurityLogger.logSecurityEvent("RELATORIO_SALDO_MEDIO", "Relatório de saldo médio gerado");

            return relatorio.toString();

        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RELATORIO_SALDO_MEDIO",
                    "Erro ao gerar relatório de saldo médio", e);
            return "Erro ao gerar relatório de saldo médio.";
        }
    }
}