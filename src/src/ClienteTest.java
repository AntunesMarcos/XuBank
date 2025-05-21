import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ClienteTest {

    @Test
    public void testSetRendaMensalValida() {
        Cliente c = new Cliente("12345678900", "senha", "João", 3000);
        c.setRendaMensal(5000);
        assertEquals(5000, c.getRendaMensal());
    }

    @Test
    public void testSetRendaMensalNegativa() {
        Cliente c = new Cliente("12345678900", "senha", "João", 3000);
        assertThrows(IllegalArgumentException.class, () -> {
            c.setRendaMensal(-1000);
        });
    }
}