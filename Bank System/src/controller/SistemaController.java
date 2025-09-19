package controller;

import model.Cliente;
import model.Conta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SistemaController {

    private List<Cliente> clientes = new ArrayList<>();
    private List<Conta> contas = new ArrayList<>();

    private int proximoIdCliente = 1;
    private int proximoIdConta = 1;
    private Random random = new Random();

    // ===================== CLIENTE =====================

    public Cliente criarCliente(String nome, int nuit, String endereco, int telefone,
                                String email, LocalDate idade, String documento, String senha) {
        Cliente c = new Cliente(
                nome,
                proximoIdCliente++,
                nuit,
                endereco,
                telefone,
                email,
                idade,
                Cliente.Status.ATIVO,
                documento,
                senha
        );
        clientes.add(c);
        return c;
    }

    public List<Cliente> listarClientes() {
        return clientes;
    }

    public Cliente buscarClientePorId(int id) {
        return clientes.stream()
                .filter(cli -> cli.getIdCliente() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean editarCliente(int id, String novoNome, String novoEmail, int novoTelefone) {
        Cliente cli = buscarClientePorId(id);
        if (cli != null) {
            cli.setNomeCli(novoNome);
            cli.setEmailCli(novoEmail);
            cli.setTelefoneCli(novoTelefone);
            return true;
        }
        return false;
    }

    public boolean eliminarCliente(int id) {
        return clientes.removeIf(c -> c.getIdCliente() == id);
    }

    // ===================== CONTA =====================

    public Conta criarConta(int idCliente, Conta.TipoConta tipoConta) {
        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) return null;

        int numeroConta = gerarNumeroConta();
        int niubConta = gerarNiub();
        int nib = gerarNib();

        Conta conta = new Conta(
                proximoIdConta++,
                numeroConta,
                tipoConta,
                cliente,
                niubConta,
                nib
        );

        contas.add(conta);
        return conta;
    }

    public List<Conta> listarContas() {
        return contas;
    }

    public Conta buscarContaPorId(int idConta) {
        return contas.stream()
                .filter(c -> c.getIdConta() == idConta)
                .findFirst()
                .orElse(null);
    }

    public boolean editarConta(int idConta, Conta.TipoConta novoTipo) {
        Conta conta = buscarContaPorId(idConta);
        if (conta != null) {
            conta.setTipoConta(novoTipo);
            return true;
        }
        return false;
    }

    public boolean eliminarConta(int idConta) {
        return contas.removeIf(c -> c.getIdConta() == idConta);
    }

    public boolean depositar(int idConta, double valor) {
        Conta conta = buscarContaPorId(idConta);
        if (conta != null && valor > 0) {
            conta.depositar(valor);
            return true;
        }
        return false;
    }

    // logica verdadeira do saque/levantamento

    public boolean sacar(int idConta, double valor) {
        Conta conta = buscarContaPorId(idConta);
        if (conta != null && valor > 0 && conta.getSaldo() >= valor) {
            conta.sacar(valor);
            return true;
        }
        return false; // saldo insuficiente ou conta inexistente
    }

    // este metodo serve para fazer trandferenncia para mesmo banco
    public boolean transferirMesmaInstituicao(int idOrigem, int idDestino, double valor) {
        Conta origem = buscarContaPorId(idOrigem);
        Conta destino = buscarContaPorId(idDestino);

        if (origem == null || destino == null || origem == destino) return false;
        if (valor <= 0 || origem.getSaldo() < valor) return false;

        origem.sacar(valor);
        destino.depositar(valor);
        return true;
    }

    public boolean transferirOutroBanco(int idOrigem, String nibDestino, double valor) {
        Conta origem = buscarContaPorId(idOrigem);
        if (origem == null || valor <= 0 || origem.getSaldo() < valor) return false;

        origem.sacar(valor);
        // Aqui apenas simula o envio para outro banco usando NIB
        // Em um sistema real, seria uma chamada a uma API externa
        return true;
    }

    public boolean transferirCarteiraMovel(int idOrigem, String numeroTelefone, double valor) {
        Conta origem = buscarContaPorId(idOrigem);
        if (origem == null || valor <= 0 || origem.getSaldo() < valor) return false;

        origem.sacar(valor);
        // Aqui apenas simula o envio para a carteira móvel (M-Pesa, etc.)
        return true;
    }


    

    // geradores automatios, do numero de conta, NIB e NUIB

    private int gerarNumeroConta() {
        return 100000 + random.nextInt(900000);
    }

    private int gerarNiub() {
        return 10000000 + random.nextInt(90000000);
    }

    private int gerarNib() {
        return 200000000 + random.nextInt(100000000);
    }
}
