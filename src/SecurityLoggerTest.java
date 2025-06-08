import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SecurityLoggerTest {

    @Test
    public void testLogSecurityEvent() throws IOException {
        String evento = "TESTE_EVENTO";
        String detalhes = "Detalhes do teste";

        SecurityLogger.logSecurityEvent(evento, detalhes);

        // Verificar se o arquivo de log foi criado
        File logFile = new File("security.log");
        Assertions.assertTrue(logFile.exists());

        // Verificar se o conteúdo foi escrito
        String conteudo = new String(Files.readAllBytes(Paths.get("security.log")));
        Assertions.assertTrue(conteudo.contains("SECURITY"));
        Assertions.assertTrue(conteudo.contains(evento));
        Assertions.assertTrue(conteudo.contains(detalhes));
    }

    @Test
    public void testLogError() throws IOException {
        String evento = "TESTE_ERRO";
        String detalhes = "Detalhes do erro";
        Exception ex = new RuntimeException("Exceção de teste");

        SecurityLogger.logError(evento, detalhes, ex);

        // Verificar se o arquivo de log foi criado
        File logFile = new File("security.log");
        Assertions.assertTrue(logFile.exists());

        // Verificar se o conteúdo foi escrito
        String conteudo = new String(Files.readAllBytes(Paths.get("security.log")));
        Assertions.assertTrue(conteudo.contains("ERROR"));
        Assertions.assertTrue(conteudo.contains(evento));
        Assertions.assertTrue(conteudo.contains(detalhes));
        Assertions.assertTrue(conteudo.contains("Exceção de teste"));
    }

    @Test
    public void testLogErrorComExcecaoNula() throws IOException {
        String evento = "TESTE_ERRO_SEM_EXCECAO";
        String detalhes = "Detalhes sem exceção";

        SecurityLogger.logError(evento, detalhes, null);

        // Verificar se o arquivo de log foi criado
        File logFile = new File("security.log");
        Assertions.assertTrue(logFile.exists());

        // Verificar se o conteúdo foi escrito
        String conteudo = new String(Files.readAllBytes(Paths.get("security.log")));
        Assertions.assertTrue(conteudo.contains("ERROR"));
        Assertions.assertTrue(conteudo.contains(evento));
        Assertions.assertTrue(conteudo.contains(detalhes));
    }
}