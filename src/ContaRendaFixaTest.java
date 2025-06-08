import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ContaRendaFixaTest {

    private Cliente criarClienteValido() throws SecurityException {
        return new Cliente("11144477735", "MinhaSenh@123", "João Silva", 3000);
    }

    @Test
    public void testCriacaoContaRendaFixa() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaRendaFixa conta = new ContaRendaFixa(cliente);

        Assertions.assertEquals(3, conta.getTipoConta());
        Assertions.assertEquals("ContaRendaFixa", conta.getTipoContaNome());
        Assertions.assertEquals(0.0, conta.getSaldo(), 0.01);
    }

    @Test
    public void testCalcularRendimento() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaRendaFixa conta = new ContaRendaFixa(cliente);

        // Depositar valor significativo para teste
        conta.Depositar(10000);
        double saldoAnterior = conta.getSaldo();

        double rendimento = conta.CalcularRendimento();

        // O rendimento deve estar entre 0.5% e 0.85% do saldo
        double rendimentoMin = saldoAnterior * 0.005;
        double rendimentoMax = saldoAnterior * 0.0085;

        Assertions.assertTrue(rendimento >= rendimentoMin - saldoAnterior * 0.001); // Margem para variação
        Assertions.assertTrue(rendimento <= rendimentoMax + saldoAnterior * 0.001);

        // Verificar se a taxa fixa foi descontada
        double saldoEsperado = saldoAnterior + rendimento - 20.0;
        Assertions.assertTrue(Math.abs(conta.getSaldo() - saldoEsperado) < 0.01);
    }

    @Test
    public void testSacarComImposto() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaRendaFixa conta = new ContaRendaFixa(cliente);

        // Depositar valor para ter saldo
        conta.Depositar(5000);
        double saldoAnterior = conta.getSaldo();

        // Realizar saque
        boolean saqueOk = conta.Sacar(1000);

        // O saque deve descontar o valor + imposto sobre rendimento
        Assertions.assertTrue(saqueOk);
        Assertions.assertTrue(conta.getSaldo() < saldoAnterior - 1000);
    }

    @Test
    public void testSaqueSemSaldoSuficiente() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaRendaFixa conta = new ContaRendaFixa(cliente);

        conta.Depositar(100);

        // Tentar sacar mais do que o saldo disponível
        boolean saqueFalhou = conta.Sacar(1000);
        Assertions.assertFalse(saqueFalhou);
    }

    @Test
    public void testAplicarImposto() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaRendaFixa conta = new ContaRendaFixa(cliente);

        double rendimento = 1000.0;
        double imposto = conta.aplicarImposto(rendimento);

        // O imposto deve ser 15% do rendimento
        Assertions.assertEquals(150.0, imposto, 0.01);
    }

    @Test
    public void testAplicarImpostoComRendimentoZero() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaRendaFixa conta = new ContaRendaFixa(cliente);

        double imposto = conta.aplicarImposto(0.0);
        Assertions.assertEquals(0.0, imposto, 0.01);
    }

    @Test
    public void testAplicarImpostoComValorInvalido() throws SecurityException {
        Cliente cliente = criarClienteValido();
        ContaRendaFixa conta = new ContaRendaFixa(cliente);

        Assertions.assertThrows(SecurityException.class, () -> {
            conta.aplicarImposto(Double.NaN);
        });
    }
}