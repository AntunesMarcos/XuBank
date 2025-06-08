import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class PerformanceTest {

    @Test
    public void testCriacaoMultiplosClientes() {
        XuBank banco = new XuBank();
        long startTime = System.currentTimeMillis();

        // Criar 1000 clientes
        for (int i = 0; i < 1000; i++) {
            String cpf = String.format("%011d", i + 10000000000L);
            boolean resultado = banco.CadastrarCliente("Cliente " + i, cpf, "MinhaSenh@123", 3000);
            Assertions.assertTrue(resultado);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        Assertions.assertEquals(1000, banco.getNumeroClientes());
        System.out.println("Tempo para criar 1000 clientes: " + duration + "ms");

        // Deve completar em menos de 5 segundos
        Assertions.assertTrue(duration < 5000);
    }

    @Test
    public void testBuscaClientesEmLote() {
        XuBank banco = new XuBank();

        // Criar 100 clientes
        for (int i = 0; i < 100; i++) {
            String cpf = String.format("%011d", i + 10000000000L);
            banco.CadastrarCliente("Cliente " + i, cpf, "MinhaSenh@123", 3000);
        }

        long startTime = System.currentTimeMillis();

        // Buscar todos os clientes 10 vezes
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 100; j++) {
                String cpf = String.format("%011d", j + 10000000000L);
                Cliente cliente = banco.buscarClientePorCpf(cpf);
                Assertions.assertNotNull(cliente);
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Tempo para 1000 buscas: " + duration + "ms");

        // Deve completar em menos de 1 segundo
        Assertions.assertTrue(duration < 1000);
    }

    @Test
    public void testOperacoesContasConcorrentes() throws SecurityException {
        Cliente cliente = new Cliente("11144477735", "MinhaSenh@123", "João Silva", 5000);
        ContaCorrente conta = new ContaCorrente(cliente);

        // Depositar valor inicial
        conta.Depositar(10000);

        long startTime = System.currentTimeMillis();

        // Realizar 1000 operações mistas
        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0) {
                conta.Depositar(100);
            } else {
                conta.Sacar(50);
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Tempo para 1000 operações: " + duration + "ms");

        // Verificar saldo final
        double saldoEsperado = 10000 + (500 * 100) - (500 * 50); // 35000
        Assertions.assertEquals(35000.0, conta.getSaldo(), 0.01);

        // Deve completar em menos de 500ms
        Assertions.assertTrue(duration < 500);
    }

    @Test
    public void testGeracaoRelatoriosGrandes() throws SecurityException {
        XuBank banco = new XuBank();

        // Criar 50 clientes com múltiplas contas
        for (int i = 0; i < 50; i++) {
            String cpf = String.format("%011d", i + 10000000000L);
            banco.CadastrarCliente("Cliente " + i, cpf, "MinhaSenh@123", 3000 + i * 100);

            Cliente cliente = banco.buscarClientePorCpf(cpf);

            // Adicionar 4 tipos de conta para cada cliente
            ContaCorrente cc = new ContaCorrente(cliente);
            ContaPoupanca cp = new ContaPoupanca(cliente);
            ContaRendaFixa crf = new ContaRendaFixa(cliente);
            ContaInvestimento ci = new ContaInvestimento(cliente);

            cc.Depositar(1000 + i * 10);
            cp.Depositar(2000 + i * 20);
            crf.Depositar(3000 + i * 30);
            ci.Depositar(4000 + i * 40);

            cliente.AdicionarConta(cc);
            cliente.AdicionarConta(cp);
            cliente.AdicionarConta(crf);
            cliente.AdicionarConta(ci);
        }

        long startTime = System.currentTimeMillis();

        // Gerar relatórios 100 vezes
        for (int i = 0; i < 100; i++) {
            String custodia = banco.RelatorioCustodia();
            String extremos = banco.ClientesExtremos();

            Assertions.assertNotNull(custodia);
            Assertions.assertNotNull(extremos);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Tempo para 100 relatórios: " + duration + "ms");

        // Deve completar em menos de 2 segundos
        Assertions.assertTrue(duration < 2000);
    }
}
