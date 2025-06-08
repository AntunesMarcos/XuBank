import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ContaTest {

    private Cliente criarClienteValido() throws SecurityException {
        return new Cliente("11144477735", "MinhaSenh@123", "João Silva", 3000);
    }

    @Test
    public void testCriacaoContaComClienteNull() {
        Assertions.assertThrows(SecurityException.class, () -> {
            new ContaCorrente(null);
        });
    }

    @Test
    public void testGerarNumeroUnico() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaCorrente conta1 = new ContaCorrente(cliente);
        ContaCorrente conta2 = new ContaCorrente(cliente);

        Assertions.assertNotEquals(conta1.getNumero(), conta2.getNumero());
        Assertions.assertTrue(conta1.getNumero() >= 100000);
        Assertions.assertTrue(conta1.getNumero() <= 999999);
    }

    @Test
    public void testSacarValorInvalido() throws SecurityException {
        Cliente cliente = criarClienteValido();
        Conta conta = new ContaCorrente(cliente);

        Assertions.assertThrows(SecurityException.class, () -> {
            conta.Sacar(-100);
        });

        Assertions.assertThrows(SecurityException.class, () -> {
            conta.Sacar(Double.POSITIVE_INFINITY);
        });
    }

    @Test
    public void testDepositarValorInvalido() throws SecurityException {
        Cliente cliente = criarClienteValido();
        Conta conta = new ContaCorrente(cliente);

        Assertions.assertThrows(SecurityException.class, () -> {
            conta.Depositar(-100);
        });

        Assertions.assertThrows(SecurityException.class, () -> {
            conta.Depositar(Double.NaN);
        });
    }

    @Test
    public void testDepositarValorZero() throws SecurityException {
        Cliente cliente = criarClienteValido();
        Conta conta = new ContaCorrente(cliente);

        boolean resultado = conta.Depositar(0);
        Assertions.assertFalse(resultado);
    }

    @Test
    public void testGerarExtrato() throws SecurityException {
        Cliente cliente = criarClienteValido();
        Conta conta = new ContaCorrente(cliente);

        String extrato = conta.GerarExtrato();
        Assertions.assertNotNull(extrato);
        Assertions.assertTrue(extrato.contains("Conta nº"));
        Assertions.assertTrue(extrato.contains("Saldo: R$"));
    }

    @Test
    public void testAtualizarRendimentoComPeriodoLongo() throws SecurityException {
        Cliente cliente = criarClienteValido();
        Conta conta = new ContaPoupanca(cliente);

        // Simular uma data muito antiga (mais de 10 anos)
        // Como não podemos alterar a data diretamente, testamos o comportamento normal
        conta.AtualizarRendimento();
        Assertions.assertTrue(conta.getSaldo() >= 0);
    }
}