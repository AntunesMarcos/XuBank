public class ContaPoupanca extends Conta {
    public ContaPoupanca(Cliente cliente) {
        super(cliente);
        this.tipoConta = 2; 
    }

    @Override
    public double CalcularRendimento() {
        double rendimento = saldo * 0.006;
        saldo += rendimento;
        return rendimento;
    }
    
    @Override
    public String getTipoContaNome() {
        return "ContaPoupanca";
    }
}
