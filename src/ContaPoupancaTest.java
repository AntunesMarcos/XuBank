import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ContaPoupancaTest {

    private Cliente criarClienteValido() throws SecurityException {
        return new Cliente("11144477735", "MinhaSenh@123", "João Silva", 3000);
    }

    @Test
    public void testCriacaoContaPoupanca() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaPoupanca conta = new ContaPoupanca(cliente);

        Assertions.assertEquals(2, conta.getTipoConta());
        Assertions.assertEquals("ContaPoupanca", conta.getTipoContaNome());
        Assertions.assertEquals(0.0, conta.getSaldo(), 0.01);
    }

    @Test
    public void testCalcularRendimento() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaPoupanca conta = new ContaPoupanca(cliente);

        // Depositar valor para calcular rendimento
        conta.Depositar(1000);
        double saldoAnterior = conta.getSaldo();

        double rendimento = conta.CalcularRendimento();

        Assertions.assertTrue(rendimento > 0);
        Assertions.assertEquals(saldoAnterior * 0.006, rendimento, 0.01);
        Assertions.assertEquals(saldoAnterior + rendimento, conta.getSaldo(), 0.01);
    }

    @Test
    public void testCalcularRendimentoSemSaldo() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaPoupanca conta = new ContaPoupanca(cliente);

        double rendimento = conta.CalcularRendimento();

        Assertions.assertEquals(0.0, rendimento, 0.01);
        Assertions.assertEquals(0.0, conta.getSaldo(), 0.01);
    }

    @Test
    public void testDepositarESacar() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaPoupanca conta = new ContaPoupanca(cliente);

        // Testar depósito
        boolean depositoOk = conta.Depositar(500);
        Assertions.assertTrue(depositoOk);
        Assertions.assertEquals(500.0, conta.getSaldo(), 0.01);

        // Testar saque
        boolean saqueOk = conta.Sacar(200);
        Assertions.assertTrue(saqueOk);
        Assertions.assertEquals(300.0, conta.getSaldo(), 0.01);

        // Testar saque maior que saldo
        boolean saqueFalhou = conta.Sacar(400);
        Assertions.assertFalse(saqueFalhou);
        Assertions.assertEquals(300.0, conta.getSaldo(), 0.01);
    }

    @Test
    public void testAtualizarRendimento() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaPoupanca conta = new ContaPoupanca(cliente);

        conta.Depositar(1000);
        double saldoAnterior = conta.getSaldo();

        conta.AtualizarRendimento();

        // O saldo deve ter aumentado devido ao rendimento
        Assertions.assertTrue(conta.getSaldo() >= saldoAnterior);
    }
}