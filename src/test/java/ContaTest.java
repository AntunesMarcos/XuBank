import org.junit.Test;
import static org.junit.Assert.*;

public class ContaTest {

    @Test
    public void testDepositoValido() throws SecurityException {
        Cliente cliente = new Cliente("123.456.789-09", "Senha@123", "João Silva", 5000.0);

        Conta conta = new ContaCorrente(cliente);

        assertTrue(conta.Depositar(1000.0));

        assertEquals(1000.0, conta.getSaldo(), 0.0);
    }

    @Test
    public void testSaqueValido() throws SecurityException {
        Cliente cliente = new Cliente("123.456.789-09", "Senha@123", "João Silva", 5000.0);
        Conta conta = new ContaCorrente(cliente);

        conta.Depositar(1000.0);
        assertTrue(conta.Sacar(500.0));

        assertEquals(500.0, conta.getSaldo(), 0.0);
    }


    @Test(expected = SecurityException.class)
    public void testSaqueComValorInvalido() throws SecurityException {
        Cliente cliente = new Cliente("123.456.789-09", "Senha@123", "João Silva", 5000.0);
        Conta conta = new ContaCorrente(cliente);


        conta.Sacar(-100.0);
    }
}