import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

public class IntegrationTest {

    private XuBank banco;
    private Cliente cliente;

    @BeforeEach
    public void setUp() throws SecurityException {
        banco = new XuBank();
        banco.CadastrarCliente("João Silva", "11144477735", "MinhaSenh@123", 5000);
        cliente = banco.buscarClientePorCpf("11144477735");
    }

    @Test
    public void testFluxoCompletoContaCorrente() throws SecurityException {
        // Criar conta corrente
        ContaCorrente cc = new ContaCorrente(cliente);
        boolean contaAdicionada = cliente.AdicionarConta(cc);
        Assertions.assertTrue(contaAdicionada);

        // Verificar limite calculado
        double limiteEsperado = 5000 * 0.4; // 2000
        Assertions.assertEquals(limiteEsperado, cc.getLimite(), 0.01);

        // Depositar
        boolean depositoOk = cc.Depositar(1000);
        Assertions.assertTrue(depositoOk);
        Assertions.assertEquals(1000.0, cc.getSaldo(), 0.01);

        // Sacar dentro do saldo
        boolean saqueOk = cc.Sacar(500);
        Assertions.assertTrue(saqueOk);
        Assertions.assertEquals(500.0, cc.getSaldo(), 0.01);

        // Sacar usando limite especial
        boolean saqueLimiteOk = cc.Sacar(2000);
        Assertions.assertTrue(saqueLimiteOk);
        Assertions.assertEquals(-1500.0, cc.getSaldo(), 0.01);

        // Depositar com saldo negativo (cobra taxa)
        double saldoAnterior = cc.getSaldo();
        boolean depositoComTaxaOk = cc.Depositar(1000);
        Assertions.assertTrue(depositoComTaxaOk);
        // Saldo deve ser menor que saldoAnterior + 1000 devido à taxa
        Assertions.assertTrue(cc.getSaldo() < saldoAnterior + 1000);
        Assertions.assertTrue(cc.getSaldo() > saldoAnterior);
    }

    @Test
    public void testFluxoCompletoContaPoupanca() throws SecurityException {
        // Criar conta poupança
        ContaPoupanca cp = new ContaPoupanca(cliente);
        boolean contaAdicionada = cliente.AdicionarConta(cp);
        Assertions.assertTrue(contaAdicionada);

        // Depositar
        boolean depositoOk = cp.Depositar(10000);
        Assertions.assertTrue(depositoOk);
        Assertions.assertEquals(10000.0, cp.getSaldo(), 0.01);

        // Calcular rendimento
        double saldoAnterior = cp.getSaldo();
        double rendimento = cp.CalcularRendimento();
        Assertions.assertTrue(rendimento > 0);
        Assertions.assertEquals(saldoAnterior + rendimento, cp.getSaldo(), 0.01);

        // Atualizar rendimento
        saldoAnterior = cp.getSaldo();
        cp.AtualizarRendimento();
        Assertions.assertTrue(cp.getSaldo() >= saldoAnterior);

        // Sacar
        boolean saqueOk = cp.Sacar(5000);
        Assertions.assertTrue(saqueOk);
        Assertions.assertTrue(cp.getSaldo() > 5000); // Ainda deve ter mais que 5000 devido aos rendimentos
    }

    @Test
    public void testFluxoCompletoContaRendaFixa() throws SecurityException {
        // Criar conta renda fixa
        ContaRendaFixa crf = new ContaRendaFixa(cliente);
        boolean contaAdicionada = cliente.AdicionarConta(crf);
        Assertions.assertTrue(contaAdicionada);

        // Depositar valor significativo
        boolean depositoOk = crf.Depositar(20000);
        Assertions.assertTrue(depositoOk);
        Assertions.assertEquals(20000.0, crf.getSaldo(), 0.01);

        // Calcular rendimento
        double saldoAnterior = crf.getSaldo();
        double rendimento = crf.CalcularRendimento();

        // Verificar que o rendimento foi aplicado e a taxa fixa descontada
        double saldoEsperado = saldoAnterior + rendimento - 20.0;
        Assertions.assertTrue(Math.abs(crf.getSaldo() - saldoEsperado) < 0.01);

        // Testar aplicação de imposto
        double imposto = crf.aplicarImposto(1000);
        Assertions.assertEquals(150.0, imposto, 0.01); // 15% de 1000

        // Sacar (deve aplicar imposto)
        saldoAnterior = crf.getSaldo();
        boolean saqueOk = crf.Sacar(5000);
        Assertions.assertTrue(saqueOk);
        // O saldo deve ser menor que saldoAnterior - 5000 devido ao imposto
        Assertions.assertTrue(crf.getSaldo() < saldoAnterior - 5000);
    }

    @Test
    public void testFluxoCompletoContaInvestimento() throws SecurityException {
        // Criar conta investimento
        ContaInvestimento ci = new ContaInvestimento(cliente);
        boolean contaAdicionada = cliente.AdicionarConta(ci);
        Assertions.assertTrue(contaAdicionada);

        // Depositar
        boolean depositoOk = ci.Depositar(15000);
        Assertions.assertTrue(depositoOk);
        Assertions.assertEquals(15000.0, ci.getSaldo(), 0.01);

        // Verificar rendimento
        Assertions.assertTrue(ci.VerificarRendimento());

        // Calcular rendimento múltiplas vezes
        for (int i = 0; i < 10; i++) {
            double saldoAnterior = ci.getSaldo();
            ci.CalcularRendimento();
            // Saldo nunca deve ficar negativo
            Assertions.assertTrue(ci.getSaldo() >= 0);
        }

        // Testar aplicação de imposto
        double impostoPositivo = ci.aplicarImposto(1000);
        Assertions.assertEquals(225.0, impostoPositivo, 0.01); // 22.5% de 1000

        double impostoNegativo = ci.aplicarImposto(-500);
        Assertions.assertEquals(0.0, impostoNegativo, 0.01); // Não cobra imposto sobre perda

        // Sacar
        if (ci.getSaldo() > 1000) {
            boolean saqueOk = ci.Sacar(1000);
            Assertions.assertTrue(saqueOk);
        }
    }

    @Test
    public void testMultiplasContasPorCliente() throws SecurityException {
        ContaCorrente cc = new ContaCorrente(cliente);
        ContaPoupanca cp = new ContaPoupanca(cliente);
        ContaRendaFixa crf = new ContaRendaFixa(cliente);
        ContaInvestimento ci = new ContaInvestimento(cliente);

        // Adicionar todas as contas
        Assertions.assertTrue(cliente.AdicionarConta(cc));
        Assertions.assertTrue(cliente.AdicionarConta(cp));
        Assertions.assertTrue(cliente.AdicionarConta(crf));
        Assertions.assertTrue(cliente.AdicionarConta(ci));

        // Tentar adicionar conta duplicada
        ContaCorrente cc2 = new ContaCorrente(cliente);
        Assertions.assertFalse(cliente.AdicionarConta(cc2));

        // Depositar em cada conta
        cc.Depositar(1000);
        cp.Depositar(2000);
        crf.Depositar(3000);
        ci.Depositar(4000);

        // Verificar saldo total
        double saldoTotal = cliente.GetSaldoTotal();
        Assertions.assertEquals(10000.0, saldoTotal, 0.01);

        // Verificar que todas as contas estão na lista
        Assertions.assertEquals(4, cliente.getContas().size());
    }

    @Test
    public void testRelatoriosComDados() throws SecurityException {
        // Cadastrar múltiplos clientes
        banco.CadastrarCliente("Maria Santos", "22233344456", "OutraSenh@456", 3000);
        Cliente cliente2 = banco.buscarClientePorCpf("22233344456");

        // Adicionar contas e depósitos
        ContaCorrente cc1 = new ContaCorrente(cliente);
        ContaPoupanca cp1 = new ContaPoupanca(cliente);
        cc1.Depositar(5000);
        cp1.Depositar(3000);
        cliente.AdicionarConta(cc1);
        cliente.AdicionarConta(cp1);

        ContaRendaFixa crf2 = new ContaRendaFixa(cliente2);
        ContaInvestimento ci2 = new ContaInvestimento(cliente2);
        crf2.Depositar(2000);
        ci2.Depositar(1500);
        cliente2.AdicionarConta(crf2);
        cliente2.AdicionarConta(ci2);

        // Testar relatório de custódia
        String custodia = banco.RelatorioCustodia();
        Assertions.assertNotNull(custodia);
        Assertions.assertTrue(custodia.contains("Corrente: R$ 5000,00"));
        Assertions.assertTrue(custodia.contains("Poupança: R$ 3000,00"));
        Assertions.assertTrue(custodia.contains("Renda Fixa: R$ 2000,00"));
        Assertions.assertTrue(custodia.contains("Investimento: R$ 1500,00"));

        // Testar relatório de clientes extremos
        String extremos = banco.ClientesExtremos();
        Assertions.assertNotNull(extremos);
        Assertions.assertTrue(extremos.contains("João Silva")); // Maior saldo (8000)
        Assertions.assertTrue(extremos.contains("Maria Santos")); // Menor saldo (3500)
    }

    @Test
    public void testAutenticacaoESeguranca() throws SecurityException {
        // Testar autenticação válida
        boolean authOk = banco.autenticarCliente("11144477735", "MinhaSenh@123");
        Assertions.assertTrue(authOk);

        // Testar autenticação inválida
        boolean authFail1 = banco.autenticarCliente("11144477735", "SenhaErrada");
        Assertions.assertFalse(authFail1);

        boolean authFail2 = banco.autenticarCliente("99999999999", "MinhaSenh@123");
        Assertions.assertFalse(authFail2);

        // Testar alteração de senha
        cliente.alterarSenha("MinhaSenh@123", "NovaSenha@456");

        // Verificar que a senha antiga não funciona mais
        boolean authOldFail = banco.autenticarCliente("11144477735", "MinhaSenh@123");
        Assertions.assertFalse(authOldFail);

        // Verificar que a nova senha funciona
        boolean authNewOk = banco.autenticarCliente("11144477735", "NovaSenha@456");
        Assertions.assertTrue(authNewOk);
    }
}