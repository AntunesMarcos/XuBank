import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    private static XuBank banco = new XuBank();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao XuBank!");
        SecurityLogger.logSecurityEvent("APLICACAO_INICIADA", "Aplicação principal iniciada");

        boolean sair = false;
        while (!sair) {
            try {
                exibirMenu();
                int opcao = lerOpcaoSegura();
                sair = processarOpcao(opcao);
            } catch (Exception e) {
                SecurityLogger.logError("ERRO_MENU_PRINCIPAL",
                        "Erro no menu principal", e);
                System.out.println("Erro inesperado. Tente novamente.");
            }
        }

        sc.close();
        SecurityLogger.logSecurityEvent("APLICACAO_ENCERRADA", "Aplicação encerrada");
        System.out.println("Sistema encerrado com segurança.");
    }

    private static void exibirMenu() {
        System.out.println("\n=== MENU XUBANK ===");
        System.out.println("1 - Cadastrar cliente");
        System.out.println("2 - Adicionar conta a cliente");
        System.out.println("3 - Depositar");
        System.out.println("4 - Sacar");
        System.out.println("5 - Listar contas de cliente");
        System.out.println("6 - Relatório de custódia");
        System.out.println("7 - Clientes extremos");
        System.out.println("8 - Alterar senha");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcaoSegura() {
        try {
            int opcao = sc.nextInt();
            sc.nextLine(); // Limpar buffer
            return opcao;
        } catch (InputMismatchException e) {
            sc.nextLine(); // Limpar buffer
            System.out.println("Opção inválida. Digite um número.");
            return -1;
        }
    }

    private static boolean processarOpcao(int opcao) {
        try {
            switch (opcao) {
                case 0: return true;
                case 1: cadastrarCliente(); break;
                case 2: adicionarConta(); break;
                case 3: depositar(); break;
                case 4: sacar(); break;
                case 5: listarContas(); break;
                case 6: System.out.println(banco.RelatorioCustodia()); break;
                case 7: System.out.println(banco.ClientesExtremos()); break;
                case 8: alterarSenha(); break;
                default: System.out.println("Opção inválida.");
            }
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_PROCESSAMENTO",
                    "Erro ao processar opção: " + opcao, e);
            System.out.println("Erro interno. Tente novamente.");
        }
        return false;
    }

    private static void cadastrarCliente() {
        try {
            System.out.print("Nome: ");
            String nome = sc.nextLine().trim();

            System.out.print("CPF (apenas números): ");
            String cpf = sc.nextLine().trim();

            System.out.print("Senha (min 8 caracteres, maiúscula, minúscula, número e especial): ");
            String senha = sc.nextLine();

            double rendaMensal = lerRendaMensalSegura();

            banco.CadastrarCliente(nome, cpf, senha, rendaMensal);

        } catch (Exception e) {
            SecurityLogger.logError("ERRO_CADASTRO_INTERFACE",
                    "Erro na interface de cadastro", e);
            System.out.println("Erro ao cadastrar cliente.");
        }
    }

    private static double lerRendaMensalSegura() {
        while (true) {
            try {
                System.out.print("Renda mensal (R$): ");
                String rendaStr = sc.nextLine().replace(",", ".");
                double rendaMensal = Double.parseDouble(rendaStr);

                if (rendaMensal < 0) {
                    System.out.println("Renda não pode ser negativa.");
                    continue;
                }

                if (!ValidationUtils.validarValor(rendaMensal)) {
                    System.out.println("Valor de renda inválido.");
                    continue;
                }

                return rendaMensal;
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Use números, ex: 2500.00");
            }
        }
    }

    private static void adicionarConta() {
        try {
            Cliente cliente = buscarClienteComAutenticacao();
            if (cliente == null) return;

            System.out.println("Tipos de conta:");
            System.out.println("1 - Corrente");
            System.out.println("2 - Poupança");
            System.out.println("3 - Renda Fixa");
            System.out.println("4 - Investimento");
            System.out.print("Escolha o tipo: ");

            int tipo = lerOpcaoSegura();

            Conta conta = criarConta(tipo, cliente);
            if (conta != null) {
                boolean sucesso = cliente.AdicionarConta(conta);
                if (sucesso) {
                    System.out.println("Conta adicionada com sucesso!");
                } else {
                    System.out.println("Não foi possível adicionar a conta.");
                }
            }

        } catch (Exception e) {
            SecurityLogger.logError("ERRO_ADICIONAR_CONTA",
                    "Erro ao adicionar conta", e);
            System.out.println("Erro ao adicionar conta.");
        }
    }

    private static Conta criarConta(int tipo, Cliente cliente) throws SecurityException {
        switch (tipo) {
            case 1: return new ContaCorrente(cliente);
            case 2: return new ContaPoupanca(cliente);
            case 3: return new ContaRendaFixa(cliente);
            case 4: return new ContaInvestimento(cliente);
            default:
                System.out.println("Tipo de conta inválido.");
                return null;
        }
    }

    private static void depositar() {
        try {
            Cliente cliente = buscarClienteComAutenticacao();
            if (cliente == null) return;

            Conta conta = buscarContaDoCliente(cliente);
            if (conta == null) return;

            double valor = lerValorSeguro("Valor para depositar: ");
            if (valor <= 0) return;

            boolean sucesso = conta.Depositar(valor);
            System.out.println(sucesso ? "Depósito realizado com sucesso!" : "Falha no depósito.");

        } catch (Exception e) {
            SecurityLogger.logError("ERRO_DEPOSITO_INTERFACE",
                    "Erro na interface de depósito", e);
            System.out.println("Erro ao processar depósito.");
        }
    }

    private static void sacar() {
        try {
            Cliente cliente = buscarClienteComAutenticacao();
            if (cliente == null) return;

            Conta conta = buscarContaDoCliente(cliente);
            if (conta == null) return;

            double valor = lerValorSeguro("Valor para sacar: ");
            if (valor <= 0) return;

            boolean sucesso = conta.Sacar(valor);
            System.out.println(sucesso ? "Saque realizado com sucesso!" : "Saque não autorizado.");

        } catch (Exception e) {
            SecurityLogger.logError("ERRO_SAQUE_INTERFACE",
                    "Erro na interface de saque", e);
            System.out.println("Erro ao processar saque.");
        }
    }

    private static double lerValorSeguro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String valorStr = sc.nextLine().replace(",", ".");
                double valor = Double.parseDouble(valorStr);

                if (!ValidationUtils.validarValor(valor)) {
                    System.out.println("Valor inválido.");
                    continue;
                }

                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Use números, ex: 100.50");
            }
        }
    }

    private static void listarContas() {
        try {
            Cliente cliente = buscarClienteComAutenticacao();
            if (cliente == null) return;

            System.out.println("\n=== CONTAS DO CLIENTE ===");
            cliente.ListarContas();

        } catch (Exception e) {
            SecurityLogger.logError("ERRO_LISTAR_CONTAS",
                    "Erro ao listar contas", e);
            System.out.println("Erro ao listar contas.");
        }
    }

    private static void alterarSenha() {
        try {
            Cliente cliente = buscarClienteComAutenticacao();
            if (cliente == null) return;

            System.out.print("Nova senha: ");
            String novaSenha = sc.nextLine();

            System.out.print("Confirme a senha atual: ");
            String senhaAtual = sc.nextLine();

            cliente.alterarSenha(senhaAtual, novaSenha);
            System.out.println("Senha alterada com sucesso!");

        } catch (SecurityException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_ALTERAR_SENHA",
                    "Erro ao alterar senha", e);
            System.out.println("Erro ao alterar senha.");
        }
    }

    private static Cliente buscarClienteComAutenticacao() {
        try {
            System.out.print("CPF: ");
            String cpf = sc.nextLine().trim();

            Cliente cliente = banco.buscarClientePorCpf(cpf);
            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return null;
            }

            System.out.print("Senha: ");
            String senha = sc.nextLine();

            if (!banco.autenticarCliente(cpf, senha)) {
                System.out.println("Credenciais inválidas.");
                return null;
            }

            return cliente;
        } catch (Exception e) {
            SecurityLogger.logError("ERRO_AUTENTICACAO_INTERFACE",
                    "Erro na autenticação via interface", e);
            System.out.println("Erro na autenticação.");
            return null;
        }
    }

    private static Conta buscarContaDoCliente(Cliente cliente) {
        try {
            System.out.print("Número da conta: ");
            int numConta = sc.nextInt();
            sc.nextLine(); // Limpar buffer

            Conta conta = cliente.buscarContaPorNumero(numConta);
            if (conta == null) {
                System.out.println("Conta não encontrada.");
            }

            return conta;
        } catch (InputMismatchException e) {
            sc.nextLine(); // Limpar buffer
            System.out.println("Número de conta inválido.");
            return null;
        }
    }
}