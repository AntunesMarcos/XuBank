import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        XuBank banco = new XuBank();
        Scanner sc = new Scanner(System.in);
        System.out.println("Bem-vindo ao XuBank!");

        boolean sair = false;
        while (!sair) {
            System.out.println("\nMenu:");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Adicionar conta a cliente");
            System.out.println("3 - Depositar");
            System.out.println("4 - Sacar");
            System.out.println("5 - Listar contas de cliente");
            System.out.println("6 - Relatório de custódia");
            System.out.println("7 - Clientes extremos");
            System.out.println("0 - Sair");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 0: sair = true; break;
                case 1: {
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();
                    System.out.print("CPF: ");
                    String cpf = sc.nextLine();
                    System.out.print("Senha: ");
                    String senha = sc.nextLine();

                    double rendaMensal;
                    while (true) {
                        System.out.print("Informe sua renda mensal (R$): ");
                        String rendaStr = sc.nextLine();
                        try {
                            rendaMensal = Double.parseDouble(rendaStr.replace(",", "."));
                            if (rendaMensal < 0) {
                                System.out.println("Renda não pode ser negativa. Tente novamente.");
                            } else {
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Valor inválido. Use números, ex: 2500.00");
                        }
                    }

                    banco.CadastrarCliente(nome, cpf, senha, rendaMensal);
                    break;
                }
                case 2: {
                    System.out.print("CPF do cliente: ");
                    String cpf = sc.nextLine();
                    Cliente cliente = banco.buscarClientePorCpf(cpf);
                    if (cliente == null) {
                        System.out.println("Cliente não encontrado.");
                        break;
                    }
                    System.out.println("Tipos de conta: 1-Corrente, 2-Poupança, 3-Renda Fixa, 4-Investimento");
                    int tipo = sc.nextInt();
                    sc.nextLine();
                    Conta conta = null;
                    switch (tipo) {
                        case 1 -> conta = new ContaCorrente(cliente);
                        case 2 -> conta = new ContaPoupanca(cliente);
                        case 3 -> conta = new ContaRendaFixa(cliente);
                        case 4 -> conta = new ContaInvestimento(cliente);
                        default -> {
                            System.out.println("Tipo inválido.");
                            break;
                        }
                    }
                    if (conta != null) {
                        cliente.AdicionarConta(conta);
                    }
                    break;
                }
                case 3: {
                    System.out.print("CPF do cliente: ");
                    String cpf = sc.nextLine();
                    Cliente cliente = banco.buscarClientePorCpf(cpf);
                    if (cliente == null) {
                        System.out.println("Cliente não encontrado.");
                        break;
                    }
                    System.out.print("Número da conta: ");
                    int numConta = sc.nextInt();
                    sc.nextLine();
                    Conta conta = cliente.getContas().stream()
                            .filter(c -> c.getNumero() == numConta)
                            .findFirst()
                            .orElse(null);
                    if (conta == null) {
                        System.out.println("Conta não encontrada.");
                        break;
                    }
                    System.out.print("Valor para depositar: ");
                    double valor = sc.nextDouble();
                    sc.nextLine();
                    conta.Depositar(valor);
                    System.out.println("Depósito realizado.");
                    break;
                }
                case 4: {
                    System.out.print("CPF do cliente: ");
                    String cpf = sc.nextLine();
                    Cliente cliente = banco.buscarClientePorCpf(cpf);
                    if (cliente == null) {
                        System.out.println("Cliente não encontrado.");
                        break;
                    }
                    System.out.print("Número da conta: ");
                    int numConta = sc.nextInt();
                    sc.nextLine();
                    Conta conta = cliente.getContas().stream()
                            .filter(c -> c.getNumero() == numConta)
                            .findFirst()
                            .orElse(null);
                    if (conta == null) {
                        System.out.println("Conta não encontrada.");
                        break;
                    }
                    System.out.print("Valor para sacar: ");
                    double valor = sc.nextDouble();
                    sc.nextLine();
                    boolean sucesso = conta.Sacar(valor);
                    System.out.println(sucesso ? "Saque realizado." : "Saque não autorizado.");
                    break;
                }
                case 5: {
                    System.out.print("CPF do cliente: ");
                    String cpf = sc.nextLine();
                    Cliente cliente = banco.buscarClientePorCpf(cpf);
                    if (cliente == null) {
                        System.out.println("Cliente não encontrado.");
                        break;
                    }
                    cliente.ListarContas();
                    break;
                }
                case 6:
                    System.out.println(banco.RelatorioCustodia());
                    break;
                case 7:
                    System.out.println(banco.ClientesExtremos());
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

        sc.close();
        System.out.println("Sistema encerrado.");
    }
}
