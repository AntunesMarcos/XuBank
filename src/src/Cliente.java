import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nome;
    private String cpf;
    private String senha;
    public List<Conta> contas;

    public Cliente(String cpf, String senha, String nome) {
        this.cpf = cpf;
        this.senha = senha;
        this.nome = nome;
    }
    public Cliente(List<Conta> contas) {
        this.contas = new ArrayList<>(contas);
    }
    public boolean AdicionarConta(Conta novaConta) {

        for (Conta c : contas) {
            if (c.getClass().equals(novaConta.getClass())) {
                String finaisCpf = cpf.length() >= 3 ? cpf.substring(cpf.length() - 3) : cpf;
                System.out.println("Já existe uma conta do tipo " + c.getClass().getSimpleName()
                        + " para o cliente " + this.nome + " (CPF: ***" + finaisCpf + ")");
                return false;
            }
        }
        contas.add(novaConta);
        System.out.println("Conta do tipo " + novaConta.getClass().getSimpleName()
                + " adicionada com sucesso para o cliente " + this.nome);
        return true;
    }

    public void ListarContas() {
        if(contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }
        for (Conta c : contas) {
            System.out.println(c.GerarExtrato());
        }
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    public List<Conta> getContas() {
        return contas;
    }

}
