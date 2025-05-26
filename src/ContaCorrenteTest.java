import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ContaCorrenteTest {

    @Test
    public void testCalculoLimiteComRendaAlta() {
        Cliente cliente = new Cliente("12345678900", "senha", "Maria", 5000);
        ContaCorrente cc = new ContaCorrente(cliente);
        assertEquals(2000, cc.calcularLimite(), 0.01);
    }

    @Test
    public void testCalculoLimiteComRendaBaixa() {
        Cliente cliente = new Cliente("12345678900", "senha", "João", 50);
        ContaCorrente cc = new ContaCorrente(cliente);
        assertEquals(100, cc.calcularLimite(), 0.01);
    }
}