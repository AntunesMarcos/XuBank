import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Conta {
    private static final Set<Integer> numerosUsados = new HashSet<>();
    private static final ReentrantLock lock = new ReentrantLock();

    protected int numero;
    protected double saldo;
    protected Cliente cliente;
    protected LocalDate dataUltimaAtualizacao;
    protected int tipoConta;

    public Conta(Cliente cliente) throws SecurityException {
        if (cliente == null) {
            throw new SecurityException("Cliente não pode ser nulo");
        }

        this.numero = gerarNumeroUnico();
        this.saldo = 0.0;
        this.cliente = cliente;
        this.dataUltimaAtualizacao = LocalDate.now();

        SecurityLogger.logSecurityEvent("CONTA_CRIADA",
                "Conta criada para cliente: " + cliente.getCpfOfuscado());
    }

    private int gerarNumeroUnico() throws SecurityException {
        lock.lock();
        try {
            Random random = new Random();
            int tentativas = 0;
            int num;

            do {
                num = 100000 + random.nextInt(900000);
                tentativas++;

                if (tentativas > 1000) {
                    throw new SecurityException("Não foi possível gerar número único para conta");
                }
            } while (numerosUsados.contains(num));

            numerosUsados.add(num);
            return num;
        } finally {
            lock.unlock();
        }
    }

    public abstract double CalcularRendimento() throws SecurityException;

    public abstract String getTipoContaNome();

    public int getTipoConta() {
        return tipoConta;
    }

    public boolean Sacar(double valor) throws SecurityException {
        if (!ValidationUtils.validarValor(valor)) {
            SecurityLogger.logSecurityEvent("SAQUE_INVALIDO",
                    "Tentativa de saque com valor inválido: " + valor);
            throw new SecurityException("Valor de saque inválido");
        }

        if (valor <= 0) {
            return false;
        }

        if (valor > saldo) {
            SecurityLogger.logSecurityEvent("SAQUE_NEGADO",
                    "Saque negado por saldo insuficiente - Conta: " + numero);
            return false;
        }

        saldo -= valor;
        SecurityLogger.logSecurityEvent("SAQUE_REALIZADO",
                "Saque realizado - Conta: " + numero + " Valor: " + String.format("%.2f", valor));
        return true;
    }

    public boolean Depositar(double valor) throws SecurityException {
        if (!ValidationUtils.validarValor(valor)) {
            SecurityLogger.logSecurityEvent("DEPOSITO_INVALIDO",
                    "Tentativa de depósito com valor inválido: " + valor);
            throw new SecurityException("Valor de depósito inválido");
        }

        if (valor <= 0) {
            return false;
        }

        saldo += valor;
        SecurityLogger.logSecurityEvent("DEPOSITO_REALIZADO",
                "Depósito realizado - Conta: " + numero + " Valor: " + String.format("%.2f", valor));
        return true;
    }

    public void AtualizarRendimento() throws SecurityException {
        try {
            LocalDate hoje = LocalDate.now();
            long meses = ChronoUnit.MONTHS.between(dataUltimaAtualizacao, hoje);

            if (meses > 120) { // Limite de 10 anos
                throw new SecurityException("Período de atualização muito longo");
            }

            for (int i = 0; i < meses; i++) {
                CalcularRendimento();
            }

            if (meses > 0) {
                dataUltimaAtualizacao = hoje;
                SecurityLogger.logSecurityEvent("RENDIMENTO_ATUALIZADO",
                        "Rendimento atualizado - Conta: " + numero);
            }
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RENDIMENTO",
                    "Erro ao atualizar rendimento - Conta: " + numero, e);
            throw new SecurityException("Erro ao atualizar rendimento", e);
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

    protected void setSaldo(double novoSaldo) throws SecurityException {
        if (!ValidationUtils.validarValor(novoSaldo)) {
            throw new SecurityException("Valor de saldo inválido");
        }
        this.saldo = novoSaldo;
    }
}