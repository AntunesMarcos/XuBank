import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SecurityLogger {
    private static final String LOG_FILE = "security.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logSecurityEvent(String event, String details) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            pw.println(String.format("[%s] SECURITY: %s - %s", timestamp, event, details));
        } catch (IOException e) {
            System.err.println("Erro ao gravar log de segurança: " + e.getMessage());
        }
    }

    public static void logError(String event, String details, Throwable throwable) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            pw.println(String.format("[%s] ERROR: %s - %s", timestamp, event, details));
            if (throwable != null) {
                pw.println("Stack trace: " + throwable.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Erro ao gravar log de erro: " + e.getMessage());
        }
    }
}