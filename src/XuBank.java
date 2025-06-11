import java.util.ArrayList;
import java.util.List;

public class XuBank {
    private List<Cliente> clientes;
    private RelatorioCustodia relatorios;

    public XuBank() {
        clientes = new ArrayList<>();
        relatorios = new RelatorioCustodia(clientes);
        SecurityLogger.logSecurityEvent("SISTEMA_INICIADO", "Sistema XuBank iniciado");
    }

    public String RelatorioCustodia() {
        try {
            return relatorios.RelatorioCustodia();
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RELATORIO_CUSTODIA", "Erro ao gerar relatório de custódia", e);
            return "Erro ao gerar relatório de custódia.";
        }
    }

    public String ClientesExtremos() {
        try {
            return relatorios.EncontrarClientesExtremos();
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RELATORIO_EXTREMOS", "Erro ao gerar relatório de extremos", e);
            return "Erro ao gerar relatório de extremos.";
        }
    }
}