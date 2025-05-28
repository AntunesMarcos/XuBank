import java.util.Random;

public class ContaInvestimento extends Conta implements TaxaImposto {
    public ContaInvestimento(Cliente cliente) {
        super(cliente);
        this.tipoConta = 4; 
    }

    @Override
    public boolean Sacar(double valor) {
        if (valor > 0 && valor <= saldo) {
            double rendimento = CalcularRendimento();
            double imposto = aplicarImposto(rendimento);
            saldo -= (valor + imposto);
            return true;
        }
        return false;
    }

    @Override
    public boolean Depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
            return true;
        }
        return false;
    }

    @Override
    public double CalcularRendimento() {
        Random rand = new Random();
        double variacao = -0.6 + (1.5 + 0.6) * rand.nextDouble();
        double rendimento = saldo * (variacao / 100.0);
        saldo += rendimento;
        if (rendimento > 0) {
            saldo -= rendimento * 0.01;
        }
        return rendimento;
    }

    public boolean VerificarRendimento() {
        return saldo > 0;
    }
    
    @Override
    public String getTipoContaNome() {
        return "ContaInvestimento";
    }
    
    @Override
    public double aplicarImposto(double rendimento) {
       
        return rendimento > 0 ? rendimento * 0.225 : 0;
    }
}
