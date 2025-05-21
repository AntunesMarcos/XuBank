public class ContaPoupanca extends Conta {
    public ContaPoupanca(Cliente cliente) {
        super(cliente);
    }

    @Override
    public double CalcularRendimento() {
        double rendimento = saldo * 0.006;
        saldo += rendimento;
        return rendimento;
    }
}
