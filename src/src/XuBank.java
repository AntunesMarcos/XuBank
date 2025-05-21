import java.util.ArrayList;
import java.util.List;

public class XuBank {
    private List<Cliente> clientes;

    public XuBank() {
        clientes = new ArrayList<>();
    }

    public boolean CadastrarCliente(String nome, String cpf, String senha, double rendaMensal) {
        if (rendaMensal < 0) {
            System.out.println("Renda mensal não pode ser negativa.");
            return false;
        }
        Cliente novoCliente = new Cliente(cpf, senha, nome, rendaMensal);
        clientes.add(novoCliente);
        System.out.println("Cliente " + nome + " cadastrado com sucesso com renda mensal R$ " + String.format("%.2f", rendaMensal));
        return true;
    }

    public Cliente buscarClientePorCpf(String cpf) {
        for (Cliente c : clientes) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null;
    }

    public String RelatorioCustodia() {
        double totalCorrente = 0, totalPoupanca = 0, totalRendaFixa = 0, totalInvestimento = 0;

        for (Cliente cliente : clientes) {
            for (Conta conta : cliente.getContas()) {
                if (conta instanceof ContaCorrente) totalCorrente += conta.getSaldo();
                else if (conta instanceof ContaPoupanca) totalPoupanca += conta.getSaldo();
                else if (conta instanceof ContaRendaFixa) totalRendaFixa += conta.getSaldo();
                else if (conta instanceof ContaInvestimento) totalInvestimento += conta.getSaldo();
            }
        }

        return String.format(
                "Saldo em custódia:\nCorrente: R$ %.2f\nPoupança: R$ %.2f\nRenda Fixa: R$ %.2f\nInvestimento: R$ %.2f",
                totalCorrente, totalPoupanca, totalRendaFixa, totalInvestimento);
    }

    public String ClientesExtremos() {
        if (clientes.isEmpty()) return "Nenhum cliente cadastrado.";

        Cliente maior = clientes.get(0);
        Cliente menor = clientes.get(0);

        for (Cliente c : clientes) {
            if (c.GetSaldoTotal() > maior.GetSaldoTotal()) maior = c;
            if (c.GetSaldoTotal() < menor.GetSaldoTotal()) menor = c;
        }

        return String.format("Cliente com maior saldo: %s - R$ %.2f\nCliente com menor saldo: %s - R$ %.2f",
                maior.getNome(), maior.GetSaldoTotal(), menor.getNome(), menor.GetSaldoTotal());
    }
}
