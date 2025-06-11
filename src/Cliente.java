import java.util.concurrent.locks.ReentrantLock;

public class Cliente {
    private final String nome;
    private final String cpf;
    private final String senha;
    private final double rendaMensal;
    private final List<Conta> contas;
    private final ReentrantLock lock = new ReentrantLock();

    public Cliente(String cpf, String senha, String nome, double rendaMensal) {
        this.cpf = cpf;
        this.senha = senha;
        this.nome = nome;
        this.rendaMensal = rendaMensal;
        this.contas = new ArrayList<>();
    }

    public double GetSaldoTotal() {
        lock.lock();
        try {
            double total = 0.0;
            for (Conta conta : contas) {
                total += conta.getSaldo();
            }
            return total;
        } finally {
            lock.unlock();
        }
    }

    public List<Conta> getContas() {
        return contas;
    }

    public String getNome() {
        return nome;
    }
}