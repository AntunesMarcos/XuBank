import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

public class XuBankTest {

    private XuBank banco;

    @BeforeEach
    public void setUp() {
        banco = new XuBank();
    }

    @Test
    public void testCadastrarClienteValido() {
        boolean resultado = banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", 3000);
        Assertions.assertTrue(resultado);
        Assertions.assertEquals(1, banco.getNumeroClientes());
    }

    @Test
    public void testCadastrarClienteComCpfInvalido() {
        boolean resultado = banco.CadastrarCliente("João Silva", "123", "MinhaSenh@123", 3000);
        Assertions.assertFalse(resultado);
        Assertions.assertEquals(0, banco.getNumeroClientes());
    }

    @Test
    public void testCadastrarClienteComSenhaFraca() {
        boolean resultado = banco.CadastrarCliente("João Silva", "11144477735", "123", 3000);
        Assertions.assertFalse(resultado);
        Assertions.assertEquals(0, banco.getNumeroClientes());
    }

    @Test
    public void testCadastrarClienteComRendaNegativa() {
        boolean resultado = banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", -1000);
        Assertions.assertFalse(resultado);
        Assertions.assertEquals(0, banco.getNumeroClientes());
    }

    @Test
    public void testCadastrarClienteDuplicado() {
        // Cadastrar cliente pela primeira vez
        banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", 3000);

        // Tentar cadastrar novamente com mesmo CPF
        boolean resultado = banco.CadastrarCliente("Maria Silva", "11144477735", "OutraSenh@456", 4000);
        Assertions.assertFalse(resultado);
        Assertions.assertEquals(1, banco.getNumeroClientes());
    }

    @Test
    public void testBuscarClientePorCpf() {
        banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", 3000);

        Cliente cliente = banco.buscarClientePorCpf("11144477735");
        Assertions.assertNotNull(cliente);
        Assertions.assertEquals("João Silva", cliente.getNome());

        Cliente clienteInexistente = banco.buscarClientePorCpf("99999999999");
        Assertions.assertNull(clienteInexistente);
    }

    @Test
    public void testBuscarClienteComCpfFormatado() {
        banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", 3000);

        Cliente cliente = banco.buscarClientePorCpf("111.444.777-35");
        Assertions.assertNotNull(cliente);
        Assertions.assertEquals("João Silva", cliente.getNome());
    }

    @Test
    public void testBuscarClienteComCpfNulo() {
        Cliente cliente = banco.buscarClientePorCpf(null);
        Assertions.assertNull(cliente);
    }

    @Test
    public void testAutenticarClienteValido() {
        banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", 3000);

        boolean autenticado = banco.autenticarCliente("11144477735", "MinhaSenh@123");
        Assertions.assertTrue(autenticado);
    }

    @Test
    public void testAutenticarClienteComSenhaIncorreta() {
        banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", 3000);

        boolean autenticado = banco.autenticarCliente("11144477735", "SenhaErrada");
        Assertions.assertFalse(autenticado);
    }

    @Test
    public void testAutenticarClienteInexistente() {
        boolean autenticado = banco.autenticarCliente("99999999999", "MinhaSenh@123");
        Assertions.assertFalse(autenticado);
    }

    @Test
    public void testRelatorioCustodiaVazio() {
        String relatorio = banco.RelatorioCustodia();
        Assertions.assertNotNull(relatorio);
        Assertions.assertTrue(relatorio.contains("Corrente: R$ 0,00"));
        Assertions.assertTrue(relatorio.contains("Poupança: R$ 0,00"));
        Assertions.assertTrue(relatorio.contains("Renda Fixa: R$ 0,00"));
        Assertions.assertTrue(relatorio.contains("Investimento: R$ 0,00"));
    }

    @Test
    public void testRelatorioCustodiaComContas() throws SecurityException {
        // Cadastrar cliente
        banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", 3000);
        Cliente cliente = banco.buscarClientePorCpf("11144477735");

        // Adicionar contas
        ContaCorrente cc = new ContaCorrente(cliente);
        ContaPoupanca cp = new ContaPoupanca(cliente);

        cc.Depositar(1000);
        cp.Depositar(2000);

        cliente.AdicionarConta(cc);
        cliente.AdicionarConta(cp);

        String relatorio = banco.RelatorioCustodia();
        Assertions.assertNotNull(relatorio);
        Assertions.assertTrue(relatorio.contains("Corrente: R$ 1000,00"));
        Assertions.assertTrue(relatorio.contains("Poupança: R$ 2000,00"));
    }

    @Test
    public void testClientesExtremosVazio() {
        String relatorio = banco.ClientesExtremos();
        Assertions.assertEquals("Nenhum cliente cadastrado.", relatorio);
    }

    @Test
    public void testClientesExtremosComUmCliente() throws SecurityException {
        banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", 3000);
        Cliente cliente = banco.buscarClientePorCpf("11144477735");

        ContaCorrente cc = new ContaCorrente(cliente);
        cc.Depositar(1500);
        cliente.AdicionarConta(cc);

        String relatorio = banco.ClientesExtremos();
        Assertions.assertNotNull(relatorio);
        Assertions.assertTrue(relatorio.contains("João Silva"));
        Assertions.assertTrue(relatorio.contains("1500,00"));
    }

    @Test
    public void testClientesExtremosComMultiplosClientes() throws SecurityException {
        // Cliente 1
        banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", 3000);
        Cliente cliente1 = banco.buscarClientePorCpf("11144477735");
        ContaCorrente cc1 = new ContaCorrente(cliente1);
        cc1.Depositar(5000);
        cliente1.AdicionarConta(cc1);

        // Cliente 2
        banco.CadastrarCliente("Maria Santos", "22233344456", "OutraSenh@456", 2000);
        Cliente cliente2 = banco.buscarClientePorCpf("22233344456");
        ContaCorrente cc2 = new ContaCorrente(cliente2);
        cc2.Depositar(1000);
        cliente2.AdicionarConta(cc2);

        String relatorio = banco.ClientesExtremos();
        Assertions.assertNotNull(relatorio);
        Assertions.assertTrue(relatorio.contains("João Silva")); // Maior saldo
        Assertions.assertTrue(relatorio.contains("Maria Santos")); // Menor saldo
        Assertions.assertTrue(relatorio.contains("5000,00"));
        Assertions.assertTrue(relatorio.contains("1000,00"));
    }
}