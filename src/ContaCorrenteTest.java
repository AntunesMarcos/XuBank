import org.junit.jupiter.api.Test;

public class ContaCorrenteTest {

    @Test
    public void testCalculoLimiteComRendaAlta() {
        Cliente cliente = new Cliente("12345678900", "senha", "Maria", 5000);
        ContaCorrente cc = new ContaCorrente(cliente);
        double diferenca = Math.abs(2000 - cc.calcularLimite());
        boolean resultado = diferenca < 0.01;
        if (!resultado) {
            System.out.println("Teste falhou: limite calculado incorretamente para renda alta");
        }
    }

    @Test
    public void testCalculoLimiteComRendaBaixa() {
        Cliente cliente = new Cliente("12345678900", "senha", "João", 50);
        ContaCorrente cc = new ContaCorrente(cliente);
        double diferenca = Math.abs(100 - cc.calcularLimite());
        boolean resultado = diferenca < 0.01;
        if (!resultado) {
            System.out.println("Teste falhou: limite calculado incorretamente para renda baixa");
        }
    }
}