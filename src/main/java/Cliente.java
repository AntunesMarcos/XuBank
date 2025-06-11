import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Cliente {
    private String nome;
    private String cpf;
    private String senhaHash;
    private String salt;
    private double rendaMensal;
    private List<Conta> contas;
    private final ReentrantLock lock = new ReentrantLock();

    public Cliente(String cpf, String senha, String nome, double rendaMensal) throws SecurityException {
        if (!ValidationUtils.validarCPF(cpf)) {
            throw new SecurityException("CPF inválido");
        }

        if (!ValidationUtils.validarNome(nome)) {
            throw new SecurityException("Nome inválido");
        }

        if (!ValidationUtils.validarSenha(senha)) {
            throw new SecurityException("Senha deve ter pelo menos 8 caracteres, incluindo maiúscula, minúscula, número e caractere especial");
        }

        if (rendaMensal < 0) {
            throw new SecurityException("Renda mensal não pode ser negativa");
        }

        this.cpf = ValidationUtils.sanitizarString(cpf.replaceAll("[^0-9]", ""));
        this.nome = ValidationUtils.sanitizarString(nome);
        this.salt = PasswordUtils.generateSalt();
        this.senhaHash = PasswordUtils.hashPassword(senha, this.salt);
        this.rendaMensal = rendaMensal;
        this.contas = new ArrayList<>();

        SecurityLogger.logSecurityEvent("CLIENTE_CRIADO", "Cliente criado: " + getCpfOfuscado());
    }

    public boolean verificarSenha(String senha) throws SecurityException {
        if (senha == null) {
            return false;
        }

        try {
            return PasswordUtils.verifyPassword(senha, this.senhaHash, this.salt);
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_VERIFICACAO_SENHA",
                    "Erro ao verificar senha para cliente: " + getCpfOfuscado(), e);
            return false;
        }
    }

    public void alterarSenha(String senhaAtual, String novaSenha) throws SecurityException {
        if (!verificarSenha(senhaAtual)) {
            SecurityLogger.logSecurityEvent("TENTATIVA_ALTERACAO_SENHA",
                    "Tentativa de alteração de senha com senha atual incorreta: " + getCpfOfuscado());
            throw new SecurityException("Senha atual incorreta");
        }

        if (!ValidationUtils.validarSenha(novaSenha)) {
            throw new SecurityException("Nova senha não atende aos critérios de segurança");
        }

        this.salt = PasswordUtils.generateSalt();
        this.senhaHash = PasswordUtils.hashPassword(novaSenha, this.salt);

        SecurityLogger.logSecurityEvent("SENHA_ALTERADA",
                "Senha alterada para cliente: " + getCpfOfuscado());
    }

    public boolean AdicionarConta(Conta novaConta) throws SecurityException {
        if (novaConta == null) {
            throw new SecurityException("Conta não pode ser nula");
        }

        lock.lock();
        try {
            for (Conta conta : contas) {
                if (conta.getTipoConta() == novaConta.getTipoConta()) {
                    SecurityLogger.logSecurityEvent("CONTA_DUPLICADA",
                            "Tentativa de adicionar conta duplicada - Cliente: " + getCpfOfuscado() +
                                    " Tipo: " + novaConta.getTipoContaNome());
                    return false;
                }
            }

            contas.add(novaConta);
            SecurityLogger.logSecurityEvent("CONTA_ADICIONADA",
                    "Conta adicionada - Cliente: " + getCpfOfuscado() +
                            " Tipo: " + novaConta.getTipoContaNome());
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void ListarContas() {
        lock.lock();
        try {
            if (contas.isEmpty()) {
                System.out.println("Nenhuma conta cadastrada.");
                return;
            }

            for (Conta conta : contas) {
                System.out.println(conta.GerarExtrato());
            }
        } finally {
            lock.unlock();
        }
    }

    public double GetSaldoTotal() throws SecurityException {
        lock.lock();
        try {
            double total = 0.0;
            for (Conta conta : contas) {
                double saldo = conta.getSaldo();
                if (!ValidationUtils.validarValor(saldo)) {
                    throw new SecurityException("Saldo inválido encontrado na conta: " + conta.getNumero());
                }
                total += saldo;
            }
            return total;
        } finally {
            lock.unlock();
        }
    }

    public double getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(double rendaMensal) throws SecurityException {
        if (rendaMensal < 0) {
            throw new SecurityException("Renda mensal não pode ser negativa");
        }

        lock.lock();
        try {
            this.rendaMensal = rendaMensal;

            // Atualizar limite das contas correntes
            for (Conta conta : contas) {
                if (conta instanceof ContaCorrente) {
                    ((ContaCorrente) conta).atualizarLimite();
                }
            }

            SecurityLogger.logSecurityEvent("RENDA_ATUALIZADA",
                    "Renda atualizada para cliente: " + getCpfOfuscado());
        } finally {
            lock.unlock();
        }
    }

    public String getCpf() {
        return cpf;
    }

    public String getCpfOfuscado() {
        if (cpf == null || cpf.length() < 4) {
            return "***";
        }
        return "***" + cpf.substring(cpf.length() - 3);
    }

    public String getNome() {
        return nome;
    }

    public List<Conta> getContas() {
        lock.lock();
        try {
            return new ArrayList<>(contas); // Retorna cópia para evitar modificação externa
        } finally {
            lock.unlock();
        }
    }

    public Conta buscarContaPorNumero(int numero) {
        lock.lock();
        try {
            for (Conta conta : contas) {
                if (conta.getNumero() == numero) {
                    return conta;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }
}