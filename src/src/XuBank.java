import java.util.ArrayList;
import java.util.List;

public class XuBank {
    protected List<Cliente> clientes;

    public XuBank(List<Cliente> clientes) {
        this.clientes = new ArrayList<>(clientes);
    }

    public boolean CadastrarCliente(String nome, String cpf, String senha) {
        Cliente novoCliente = new Cliente(nome, cpf, senha);
        clientes.add(novoCliente);
        System.out.println("Cliente " + nome + " cadastrado com sucesso.");

        novoCliente.AdicionarConta();

        return true;
    }

}
