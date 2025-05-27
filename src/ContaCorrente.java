public class ContaCorrente extends Conta {
    private double limite;

    public ContaCorrente(Cliente cliente) {
        super(cliente);
        this.tipoConta = 1; 
        this.limite = calcularLimite();
    }

    public double calcularLimite() {
        double limiteCalculado = cliente.getRendaMensal() * 0.4;
        return Math.max(limiteCalculado, 100.0);
    }

    @Override
    public boolean Sacar(double valor) {
        if (valor > 0 && valor <= (saldo + limite)) {
            saldo -= valor;
            return true;
        }
        return false;
    }

    @Override
    public boolean Depositar(double valor) {
        if (valor > 0) {
            if (saldo < 0) {
                double taxa = (-saldo) * 0.03 + 10;
                saldo += valor - taxa;
            } else {
                saldo += valor;
            }
            return true;
        }
        return false;
    }

    @Override
    public double CalcularRendimento() {
        return 0;
    }
    
    @Override
    public String getTipoContaNome() {
        return "ContaCorrente";
    }

    public double getLimite() {
        return limite;
    }

    public void atualizarLimite() {
        this.limite = calcularLimite();
    }

    @Override
    public String GerarExtrato() {
        return super.GerarExtrato() + String.format(" - Limite especial: R$ %.2f", limite);
    }
}
