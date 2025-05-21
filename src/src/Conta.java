import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Conta {
    private static Set<Integer> numerosUsados = new HashSet<>();
    private static Random random = new Random();

    protected int numero;
    protected double saldo;
    protected Cliente cliente;

    public Conta(Cliente cliente) {
        this.numero = gerarNumeroUnico();
        this.saldo = 0.0;
        this.cliente = cliente;
    }

    private int gerarNumeroUnico() {
        int num;
        do {
            num = 100000 + random.nextInt(900000);
        } while (numerosUsados.contains(num));
        numerosUsados.add(num);
        return num;
    }


    public boolean Sacar(double valor) {
        if (valor > 0 && valor <= saldo) {
            saldo -= valor;
            return true;
        }
        return false;
    }

    public void Depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
        }
    }

    public abstract double CalcularRendimento();

    public String GerarExtrato() {
        return "Conta: " + numero + "\nSaldo: R$ " + saldo;
    }

    public int getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
