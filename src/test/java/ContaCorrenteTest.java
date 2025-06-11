import org.junit.Test;
import static org.junit.Assert.*;

public class ContaCorrenteTest {

    @Test
    public void testCalcularLimite() throws SecurityException {
        Cliente cliente = new Cliente("123.456.789-09", "Senha@123", "João Silva", 5000.0);
        ContaCorrente conta = new ContaCorrente(cliente);


        assertEquals(2000.0, conta.getLimite(), 0.0);
    }

    @Test
    public void testSaqueComLimite() throws SecurityException {
        Cliente cliente = new Cliente("123.456.789-09", "Senha@123", "João Silva", 5000.0);
        ContaCorrente conta = new ContaCorrente(cliente);

        conta.Depositar(1000.0);
        assertTrue(conta.Sacar(2500.0));

        assertEquals(-1500.0, conta.getSaldo(), 0.0);
    }

    @Test
    public void testAtualizarLimite() throws SecurityException {
        Cliente cliente = new Cliente("123.456.789-09", "Senha@123", "João Silva", 5000.0);
        ContaCorrente conta = new ContaCorrente(cliente);

        cliente.setRendaMensal(10000.0);
        conta.atualizarLimite();

        assertEquals(4000.0, conta.getLimite(), 0.0);
    }
}