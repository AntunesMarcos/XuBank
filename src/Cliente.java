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
        for (int i = 0; i < contas.size(); i++) {
            Conta c = contas.get(i);
            if (c.getTipoConta() == novaConta.getTipoConta()) {
                
                String finaisCpf = "";
                int tam = cpf.length();
                for (int j = Math.max(0, tam - 3); j < tam; j++) {
                    finaisCpf += cpf.charAt(j);
                }
                System.out.println("Já existe uma conta do tipo " + c.getTipoContaNome()
                        + " para o cliente " + nome + " (CPF: ***" + finaisCpf + ")");
                return false;
            }
        }
        contas.add(novaConta);
        System.out.println("Conta do tipo " + novaConta.getTipoContaNome()
                + " adicionada com sucesso para o cliente " + nome);
        return true;
    }

    public void ListarContas() {
        int contador = 0;
        for (int i = 0; i < contas.size(); i++) {
            System.out.println(contas.get(i).GerarExtrato());
            contador++;
        }
        if (contador == 0) {
            System.out.println("Nenhuma conta cadastrada.");
        }
    }

    public double GetSaldoTotal() {
        double total = 0.0;
        for (int i = 0; i < contas.size(); i++) {
            total += contas.get(i).getSaldo();
        }
        return total;
    }

    public double getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(double rendaMensal) {
        if (rendaMensal < 0) {
            throw new RuntimeException("Renda mensal não pode ser negativa.");
        }
        this.rendaMensal = rendaMensal;
        
        for (int i = 0; i < contas.size(); i++) {
            Conta c = contas.get(i);
            if (c.getTipoConta() == 1) { 
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
