import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class XuBank {
    private final List<Cliente> clientes;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private RelatorioCustodia relatorios;

    public XuBank() {
        clientes = new ArrayList<>();
        relatorios = new RelatorioCustodia(clientes);
        SecurityLogger.logSecurityEvent("SISTEMA_INICIADO", "Sistema XuBank iniciado");
    }

    public boolean CadastrarCliente(String nome, String cpf, String senha, double rendaMensal) {
        lock.writeLock().lock();
        try {
            // Verificar se CPF já existe
            if (buscarClientePorCpf(cpf) != null) {
                SecurityLogger.logSecurityEvent("CADASTRO_DUPLICADO",
                        "Tentativa de cadastro com CPF já existente: " + ValidationUtils.sanitizarString(cpf));
                System.out.println("CPF já cadastrado no sistema.");
                return false;
            }

            Cliente novoCliente = new Cliente(cpf, senha, nome, rendaMensal);
            clientes.add(novoCliente);

            SecurityLogger.logSecurityEvent("CLIENTE_CADASTRADO",
                    "Cliente cadastrado: " + novoCliente.getCpfOfuscado());
            System.out.println("Cliente " + ValidationUtils.sanitizarString(nome) +
                    " cadastrado com sucesso com renda mensal R$ " + String.format("%.2f", rendaMensal));
            return true;

        } catch (SecurityException e) {
            SecurityLogger.logError("ERRO_CADASTRO",
                    "Erro ao cadastrar cliente: " + e.getMessage(), e);
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
            return false;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_INESPERADO_CADASTRO",
                    "Erro inesperado ao cadastrar cliente", e);
            System.out.println("Erro interno do sistema.");
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Cliente buscarClientePorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return null;
        }

        lock.readLock().lock();
        try {
            String cpfLimpo = cpf.replaceAll("[^0-9]", "");

            for (Cliente cliente : clientes) {
                if (constantTimeEquals(cliente.getCpf(), cpfLimpo)) {
                    return cliente;
                }
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }

    public boolean autenticarCliente(String cpf, String senha) {
        if (cpf == null || senha == null) {
            return false;
        }

        try {
            Cliente cliente = buscarClientePorCpf(cpf);
            if (cliente == null) {
                SecurityLogger.logSecurityEvent("LOGIN_FALHOU",
                        "Tentativa de login com CPF inexistente");
                return false;
            }

            boolean autenticado = cliente.verificarSenha(senha);
            if (autenticado) {
                SecurityLogger.logSecurityEvent("LOGIN_SUCESSO",
                        "Login realizado: " + cliente.getCpfOfuscado());
            } else {
                SecurityLogger.logSecurityEvent("LOGIN_FALHOU",
                        "Tentativa de login com senha incorreta: " + cliente.getCpfOfuscado());
            }

            return autenticado;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_AUTENTICACAO",
                    "Erro durante autenticação", e);
            return false;
        }
    }

    public String RelatorioCustodia() {
        lock.readLock().lock();
        try {
            relatorios = new RelatorioCustodia(clientes);
            relatorios.ImprimirRelatorio();
            return "Relatório de custódia gerado com sucesso.";
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RELATORIO_CUSTODIA",
                    "Erro ao gerar relatório de custódia", e);
            return "Erro ao gerar relatório de custódia.";
        } finally {
            lock.readLock().unlock();
        }
    }

    public String ClientesExtremos() {
        lock.readLock().lock();
        try {
            relatorios = new RelatorioCustodia(clientes);
            relatorios.EncontrarClientesExtremos();
            return "Relatório de clientes extremos gerado com sucesso.";
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_RELATORIO_EXTREMOS",
                    "Erro ao gerar relatório de extremos", e);
            return "Erro ao gerar relatório de extremos.";
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getNumeroClientes() {
        lock.readLock().lock();
        try {
            return clientes.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public RelatorioCustodia getRelatorios() {
        return new RelatorioCustodia(clientes);
    }
}