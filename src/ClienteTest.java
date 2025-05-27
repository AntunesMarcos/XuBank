import org.junit.jupiter.api.Test;

public class ClienteTest {

    @Test
    public void testSetRendaMensalValida() {
        Cliente c = new Cliente("12345678900", "senha", "João", 3000);
        c.setRendaMensal(5000);
        boolean resultado = c.getRendaMensal() == 5000;
        if (!resultado) {
            System.out.println("Teste falhou: renda mensal não foi atualizada corretamente");
        }
    }

    @Test
    public void testSetRendaMensalNegativa() {
        Cliente c = new Cliente("12345678900", "senha", "João", 3000);
        boolean excecaoLancada = false;
        try {
            c.setRendaMensal(-1000);
        } catch (RuntimeException e) {
            excecaoLancada = true;
        }
        if (!excecaoLancada) {
            System.out.println("Teste falhou: não lançou exceção para renda negativa");
        }
    }
}