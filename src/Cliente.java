import java.util.ArrayList;
import java.util.List;


public class Cliente {
    private String nome;
    private String cpf;
    private String senha;
    private double rendaMensal;
    private List<Conta> contas;

    public Cliente(String cpf, String senha, String nome, double rendaMensal) {
        this.cpf = cpf;
        this.senha = senha;
        this.nome = nome;
        this.rendaMensal = rendaMensal;
        this.contas = new ArrayList<>();
    }

    public boolean AdicionarConta(Conta novaConta) {
        for (Conta c : contas) {
            if (c.getClass().equals(novaConta.getClass())) {
                String finaisCpf = cpf.length() >= 3 ? cpf.substring(cpf.length() - 3) : cpf;
                System.out.println("Já existe uma conta do tipo " + c.getClass().getSimpleName()
                        + " para o cliente " + nome + " (CPF: ***" + finaisCpf + ")");
                return false;
            }
        }
        contas.add(novaConta);
        System.out.println("Conta do tipo " + novaConta.getClass().getSimpleName()
                + " adicionada com sucesso para o cliente " + nome);
        return true;
    }

    public void ListarContas() {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }
        for (Conta c : contas) {
            System.out.println(c.GerarExtrato());
        }
    }

    public double GetSaldoTotal() {
        double total = 0.0;
        for (Conta c : contas) {
            total += c.getSaldo();
        }
        return total;
    }

    public double getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(double rendaMensal) {
        if (rendaMensal < 0) {
            throw new IllegalArgumentException("Renda mensal não pode ser negativa.");
        }
        this.rendaMensal = rendaMensal;
        // Se quiser, atualize limite das contas correntes aqui
        for (Conta c : contas) {
            if (c instanceof ContaCorrente) {
                ((ContaCorrente) c).atualizarLimite();
            }
        }
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public List<Conta> getContas() {
        return contas;
    }
}
