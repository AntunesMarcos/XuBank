
import org.junit.Test;

import static org.junit.Assert.*;

public class ClienteTest {

    @Test
    public void testCriarClienteValido() throws SecurityException {
        Cliente cliente = new Cliente("123.456.789-09", "Senha@123", "João Silva", 5000.0);

        assertEquals("12345678909", cliente.getCpf());
        assertEquals("João Silva", cliente.getNome());

        assertEquals(5000.0, cliente.getRendaMensal(), 0.0);
        assertTrue(cliente.verificarSenha("Senha@123"));
    }


    @Test(expected = SecurityException.class)
    public void testCriarClienteComCPFInvalido() throws SecurityException {
        new Cliente("123.456.789-00", "Senha@123", "João Silva", 5000.0);
    }

    @Test(expected = SecurityException.class)
    public void testCriarClienteComSenhaFraca() throws SecurityException {
        new Cliente("123.456.789-09", "senha", "João Silva", 5000.0);
    }
}