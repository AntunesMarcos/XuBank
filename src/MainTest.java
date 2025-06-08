import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainTest {

    @Test
    public void testMainComEntradaInvalida() {
        // Simular entrada inválida seguida de saída
        String entrada = "abc\n0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(entrada.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        // Executar main
        Main.main(new String[]{});

        String saida = outputStream.toString();
        Assertions.assertTrue(saida.contains("Bem-vindo ao XuBank!"));
        Assertions.assertTrue(saida.contains("Opção inválida"));

        // Restaurar streams
        System.setIn(System.in);
        System.setOut(System.out);
    }

    @Test
    public void testMainComSaidaImediata() {
        // Simular entrada para sair imediatamente
        String entrada = "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(entrada.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        // Executar main
        Main.main(new String[]{});

        String saida = outputStream.toString();
        Assertions.assertTrue(saida.contains("Bem-vindo ao XuBank!"));
        Assertions.assertTrue(saida.contains("Sistema encerrado"));

        // Restaurar streams
        System.setIn(System.in);
        System.setOut(System.out);
    }

    @Test
    public void testMainComCadastroCliente() {
        // Simular cadastro de cliente válido seguido de saída
        String entrada = "1\nJoão Silva\n11144477735\nMinhaSenh@123\n3000.00\n0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(entrada.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        // Executar main
        Main.main(new String[]{});

        String saida = outputStream.toString();
        Assertions.assertTrue(saida.contains("Bem-vindo ao XuBank!"));
        Assertions.assertTrue(saida.contains("cadastrado com sucesso"));

        // Restaurar streams
        System.setIn(System.in);
        System.setOut(System.out);
    }

    @Test
    public void testMainComCadastroClienteInvalido() {
        // Simular cadastro com dados inválidos
        String entrada = "1\nJoão Silva\n123\nMinhaSenh@123\n3000.00\n0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(entrada.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        // Executar main
        Main.main(new String[]{});

        String saida = outputStream.toString();
        Assertions.assertTrue(saida.contains("Erro ao cadastrar cliente"));

        // Restaurar streams
        System.setIn(System.in);
        System.setOut(System.out);
    }
}