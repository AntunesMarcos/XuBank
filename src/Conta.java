import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Conta {
    private static Set<Integer> numerosUsados = new HashSet<>();
    protected int numero;
    protected double saldo;
    protected Cliente cliente;
    protected LocalDate dataUltimaAtualizacao;

    public Conta(Cliente cliente) {
        this.numero = gerarNumeroUnico();
        this.saldo = 0.0;
        this.cliente = cliente;
        this.dataUltimaAtualizacao = LocalDate.now();
    }

    private int gerarNumeroUnico() {
        Random random = new Random();
        int num;
        do {
            num = 100000 + random.nextInt(900000);
        } while (numerosUsados.contains(num));
        numerosUsados.add(num);
        return num;
    }

    public abstract double CalcularRendimento();

    public boolean Sacar(double valor) {
        if (valor > 0 && valor <= saldo) {
            saldo -= valor;
            return true;
        }
        return false;
    }

    public boolean Depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
            return true;
        }
        return false;
    }


    public void AtualizarRendimento() {
        LocalDate hoje = LocalDate.now();
        long meses = ChronoUnit.MONTHS.between(dataUltimaAtualizacao, hoje);
        for (int i = 0; i < meses; i++) {
            CalcularRendimento();
        }
        if (meses > 0) {
            dataUltimaAtualizacao = hoje;
        }
    }

    public String GerarExtrato() {
        return String.format("Conta nº %d - Saldo: R$ %.2f - Última atualização: %s",
                numero, saldo, dataUltimaAtualizacao);
    }

    public double getSaldo() {
        return saldo;
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
