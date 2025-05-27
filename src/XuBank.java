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
        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            
            if (compararStrings(c.getCpf(), cpf)) {
                return c;
            }
        }
        return null;
    }
    
    
    private boolean compararStrings(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;
        }
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public String RelatorioCustodia() {
        double totalCorrente = 0, totalPoupanca = 0, totalRendaFixa = 0, totalInvestimento = 0;

        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            List<Conta> contas = cliente.getContas();
            
            for (int j = 0; j < contas.size(); j++) {
                Conta conta = contas.get(j);
                
                if (conta.getTipoConta() == 1) totalCorrente += conta.getSaldo();
                else if (conta.getTipoConta() == 2) totalPoupanca += conta.getSaldo();
                else if (conta.getTipoConta() == 3) totalRendaFixa += conta.getSaldo();
                else if (conta.getTipoConta() == 4) totalInvestimento += conta.getSaldo();
            }
        }

        return String.format(
                "Saldo em custódia:\nCorrente: R$ %.2f\nPoupança: R$ %.2f\nRenda Fixa: R$ %.2f\nInvestimento: R$ %.2f",
                totalCorrente, totalPoupanca, totalRendaFixa, totalInvestimento);
    }

    public String ClientesExtremos() {
        if (clientes.size() == 0) return "Nenhum cliente cadastrado.";

        Cliente maior = clientes.get(0);
        Cliente menor = clientes.get(0);

        for (int i = 1; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            if (c.GetSaldoTotal() > maior.GetSaldoTotal()) maior = c;
            if (c.GetSaldoTotal() < menor.GetSaldoTotal()) menor = c;
        }

        return String.format("Cliente com maior saldo: %s - R$ %.2f\nCliente com menor saldo: %s - R$ %.2f",
                maior.getNome(), maior.GetSaldoTotal(), menor.getNome(), menor.GetSaldoTotal());
    }
}
