import java.util.Random;

public class ContaRendaFixa extends Conta implements TaxaImposto {
    public ContaRendaFixa(Cliente cliente) {
        super(cliente);
        this.tipoConta = 3; 
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
        double variacao = 0.5 + (0.85 - 0.5) * rand.nextDouble();
        double rendimento = saldo * (variacao / 100.0);
        saldo += rendimento;
        saldo -= 20;
        return rendimento;
    }
    
    @Override
    public String getTipoContaNome() {
        return "ContaRendaFixa";
    }
    
    @Override
    public double aplicarImposto(double rendimento) {
       
        return rendimento * 0.15;
    }
}