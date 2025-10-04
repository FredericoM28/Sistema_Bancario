package controller;

import model.Cliente;
import model.Conta;
import model.Emprestimo;
import model.Funcionario;
//import model.Emprestimo.emprestimos;
import model.Transacoes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SistemaController {
    private double taxaJuros = 0.05; // 5% padrão


    private List<Cliente> clientes = new ArrayList<>();
    private List<Conta> contas = new ArrayList<>();
    private List<Transacoes> transacoes = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();
    private List<Funcionario> funcionarios = new ArrayList<>();
private int idFuncionario = 1;


    private int proximoIdCliente = 1;
    private int proximoIdConta = 1;
    private Random random = new Random();
    private int proximoIdTransacao = 1;

private int gerarIdTransacao() {
    return proximoIdTransacao++;
}
private int proximoIdEmprestimo = 1;

private int gerarIdEmprestimo() {
    return proximoIdEmprestimo++;
}



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

    // Solicitar empréstimo (fica pendente)
    /*public boolean solicitarEmprestimo(int idConta, double valor, int prazoMeses) {
        Conta conta = buscarContaPorId(idConta);
        if (conta != null) {
           // Emprestimo emp = new Emprestimo(conta, valor, prazoMeses, Emprestimo.Status.PENDENTE);
           Emprestimo emp = new Emprestimo(conta, valor, prazoMeses, Emprestimo.StatusEmprestimo.PENDENTE);

            emprestimos.add(emp);
            return true;
        }
        return false;
    }*/
    public boolean solicitarEmprestimo(int idConta, double valor, int prazoMeses) {
    Conta conta = buscarContaPorId(idConta);
    if (conta == null) return false;

    Cliente cliente = conta.getClienteId(); // tua Conta tem getClienteId()
    Emprestimo emp = new Emprestimo(
            gerarIdEmprestimo(),
            cliente,
            valor,
            this.taxaJuros,   // taxa global definida no controller
            prazoMeses
    );
    // o construtor de Emprestimo já define status = PENDENTE por padrão
    emprestimos.add(emp);
    return true;
}


    public double consultarSaldo(int idConta) {
       Conta conta = buscarContaPorId(idConta);
       return (conta != null) ? conta.getSaldo() : -1;
    }

    // Consultar histórico completo
    public List<Transacoes> consultarHistorico(int idConta) {
        List<Transacoes> historico = new ArrayList<>();
        for (Transacoes t : transacoes) {
            if (t.getConta().getIdConta() == idConta) {
                historico.add(t);
            }
        }
        return historico;
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

    // Creditar
    public void creditar(int idConta, double valor) {
        Conta conta = buscarContaPorId(idConta);
        if (conta != null) {
            conta.depositar(valor);
        }
    }

    // Debitar
    public void debitar(int idConta, double valor) {
        Conta conta = buscarContaPorId(idConta);
        if (conta != null && conta.getSaldo() >= valor) {
            conta.sacar(valor);
        }
    }

    // Consultar status
    public Conta.StatusConta consultarStatus(int idConta) {
        Conta conta = buscarContaPorId(idConta);
        return (conta != null) ? conta.getStatus() : null;
    }

    // Alterar status
    public boolean alterarStatus(int idConta, Conta.StatusConta novoStatus) {
        Conta conta = buscarContaPorId(idConta);
        if (conta != null) {
            conta.setStatus(novoStatus);
            return true;
        }
        return false;
    }


    //Metodos para Funcionario 1
    // Registrar depósito (chama SistemaController.depositar)
    public boolean registrarDeposito(int idConta, double valor) {
        return depositar(idConta, valor);
    }

    // Registrar levantamento (chama SistemaController.sacar)
    public boolean registrarLevantamento(int idConta,int numeroConta, double valor) {
        return sacar(idConta, /*numeroConta,*/ valor);
    }

    // Emitir recibo (aqui só retorna String, mas poderia gerar PDF)
    public String emitirRecibo(int idTransacao) {
        for (Transacoes t : transacoes) {
            if (t.getId() == idTransacao) {
                return "RECIBO - Transação: " + t.getCategoria() +
                    " | Valor: " + t.getValor() +
                    " | Data: " + t.getData();
            }
        }
        return "Transação não encontrada.";
    }

    // Consultar operação por ID
    public Transacoes consultarOperacao(int idTransacao) {
        for (Transacoes t : transacoes) {
            if (t.getIdCliente() == idTransacao) return t;
        }
        return null;
    }

    // Funcionraio 22222222222222222222222222222222222
    // Abrir conta para cliente (wrapper para abrirConta)
   // public Conta abrirContaCliente(String nomeCli, int nuitcli, String endereco, int telefone, String email, String documento) {
    //    return criarConta/*abrirConta*/(nomeCli, nuitcli, endereco, telefone, email, documento);
        
    //}
  public Conta abrirContaCliente(String nomeCli, int nuitcli, String endereco, int telefone, String email, String documento, Conta.TipoConta tipoConta) {
    // Criar um novo cliente com valores padrão para campos obrigatórios que não tens
    Cliente novoCliente = new Cliente(
        nomeCli,                       // nome do cliente
        proximoIdCliente++,             // idCliente gerado automaticamente
        nuitcli,                        // nuit do cliente
        endereco,                       // endereço
        telefone,                       // telefone
        email,                          // email
        LocalDate.now(),                // idadeCli (pode ser ajustado depois)
        Cliente.Status.ATIVO,           // status do cliente
        documento,                      // documento
        "1234"                          // senha padrão (pode pedir alteração depois)
    );

    // Adicionar cliente à lista de clientes
    clientes.add(novoCliente);

    // Criar e retornar a conta usando o id do cliente
    return criarConta(novoCliente.getIdCliente(), tipoConta);
}


    

    // Atualizar dados do cliente
    public boolean atualizarDadosCliente(int id, String novoNome, String novoEmail, int novoTelefone) {
        return editarCliente(id, novoNome, novoEmail, novoTelefone);
    }

    // Encerrar conta de cliente
    public boolean encerrarContaCliente(int idConta) {
        return encerrarConta(idConta);
    }

    // Consultar dados do cliente
    public Cliente consultarDadosCliente(int idCliente) {
        return buscarClientePorId(idCliente);
    }

    // Reemitir cartão (simula gerando novo número aleatório)
    public String reemitirCartao(int idConta) {
        Conta conta = buscarContaPorId(idConta);
        if (conta != null) {
            String novoCartao = "CARTAO-" + gerarNumeroConta();
            return novoCartao; // em real, salvaria no banco
        }
        return "Conta não encontrada.";
    }

    // Fornecer suporte (abrir ticket de suporte)
    public String fornecerSuporte(int idCliente, String descricao) {
        return "Ticket de suporte criado para cliente " + idCliente + ": " + descricao;
    }



    //Transacoes
    // Registrar transação
   public Transacoes registrarTransacao(int idConta, String categoria, double valor) {
    Conta conta = buscarContaPorId(idConta);
    if (conta == null) return null;

    int idT = gerarIdTransacao();

    // tenta inferir tipo por categoria (fallback para DEPOSITO)
    Transacoes.TipoTransacao tipo = Transacoes.TipoTransacao.DEPOSITO;
    if (categoria != null) {
        String c = categoria.toLowerCase();
        if (c.contains("saque") || c.contains("levant")) tipo = Transacoes.TipoTransacao.SAQUE;
        else if (c.contains("transfer")) tipo = Transacoes.TipoTransacao.TRANSFERENCIA;
        else tipo = Transacoes.TipoTransacao.DEPOSITO;
    }

    Transacoes.StatusTransacao status = Transacoes.StatusTransacao.CONCLUIDA; // ajusta se precisares PENDENTE

    LocalDateTime now = LocalDateTime.now();

    Transacoes t = new Transacoes(
            idT,
            tipo,
            valor,
            now,
            conta.getIdConta(),
            null,                       // contaDestinoId se houver
            status,
            categoria,                  // descricaoTransacao
            conta,
            categoria,                  // categoria
            now,
            false                       // estornada
    );

    transacoes.add(t);
    return t;
}


    // Definir categoria de transação
    public boolean definirCategoria(int idTransacao, String novaCategoria) {
        for (Transacoes t : transacoes) {
            if (t.getId() == idTransacao) {
                t.setCategoria(novaCategoria);
                return true;
            }
        }
        return false;
    }

    // Consultar transação
    public Transacoes consultarTransacao(int idTransacao) {
        for (Transacoes t : transacoes) {
            if (t.getId() == idTransacao) return t;
        }
        return null;
    }

    // Estornar transação (devolver valor para conta)
    public boolean estornarTransacao(int idTransacao) {
        for (Transacoes t : transacoes) {
            if (t.getId() == idTransacao && !t.isEstornada()) {
                Conta conta = t.getConta();
                conta.depositar(t.getValor()); // devolve valor
                t.setEstornada(true);
                return true;
            }
        }
        return false;
    }


    //Administrador
    // Gerir funcionários
    public Funcionario criarFuncionario(String nomeCompletoFunc, String cargo) {
        Funcionario f = new Funcionario(nomeCompletoFunc, cargo);
        funcionarios.add(f);
        return f;
    }

    public boolean editarFuncionario(int idFuncionario, String novoNome, String novoCargo) {
        for (Funcionario f : funcionarios) {
            if (f.getIdFuncionario() == idFuncionario) {
                f.setNomeCompletoFunc(novoNome);
                f.setCargo(novoCargo);
                return true;
            }
        }
        return false;
    }

   /*  public boolean removerFuncionario(int idFuncionario) {
        return funcionarios.removeIf(f -> f.getIdFuncionario() == id);
       // return funcionarios.removeIf(f -> f.getIdFuncionario() == idFuncionario);

    }*/

    public boolean removerFuncionario(int idFuncionario) {
        return funcionarios.removeIf(f -> f.getIdFuncionario() == idFuncionario);
    }


    // Visualizar todas transações
    /*public List<Transacao> visualizarTodasTransacoes() {
        return transacoes;
    }*/

    // Definir taxa de juros global
    public void definirTaxasJuros(double novaTaxa) {
        this.taxaJuros = novaTaxa;
    }

    // Analisar performance banco
    /*public Map<String, Object> analisarPerformanceBanco() {
        Map<String, Object> kpis = new HashMap<>();
        kpis.put("Clientes Ativos", clientes.size());
        kpis.put("Contas Ativas", contas.size());
        kpis.put("Total Transações", transacoes.size());
        kpis.put("Volume Financeiro", transacoes.stream().mapToDouble(Transacao::getValor).sum());
        return kpis;
    }*/
    // ======================== ADMINISTRADOR ========================

    // Ver todas as transações do banco
    public List<Transacoes> visualizarTodasTransacoes() {
        return new ArrayList<>(transacoes); // cópia para não manipular a lista original
    }

    // Analisar performance do banco (exemplo: total de transações e volume)
    public Map<String, Object> analisarPerformanceBanco() {
        Map<String, Object> analise = new HashMap<>();
        double totalValor = 0.0;

        for (Transacoes t : transacoes) {
            totalValor += t.getValor();
        }

        analise.put("TotalTransacoes", transacoes.size());
        analise.put("VolumeFinanceiro", totalValor);

        return analise;
    }

    // ======================== GESTOR ========================

    // Autorizar um empréstimo solicitado
    public boolean autorizarEmprestimo(int idSolicitacao) {
    for (Emprestimo e : emprestimos) {
        if (e.getIdEmprestimo() == idSolicitacao) {
            e.setStatus(Emprestimo.emprestimos.CONFIRMADA);
            return true;
        }
    }
    return false; // Retorna false se não encontrou o empréstimo
}


    // Bloquear conta (alterar status da conta)
    public boolean bloquearConta(int idConta) {
        for (Conta c : contas) {
            if (c.getId() == idConta) {
                c.setAtiva(false); // supondo que tens setAtiva(boolean)
                return true;
            }
        }
        return false;
    }

    // Ativar conta novamente
    public boolean ativarConta(int idConta) {
        for (Conta c : contas) {
            if (c.getId() == idConta) {
                c.setAtiva(true);
                return true;
            }
        }
        return false;
    }

    // Gerar relatório financeiro do banco num período
    public Map<String, Double> gerarRelatorioFinanceiro(LocalDateTime inicio, LocalDateTime fim) {
        Map<String, Double> relatorio = new HashMap<>();
        double entradas = 0.0;
        double saidas = 0.0;

        for (Transacoes t : transacoes) {
            if ((t.getData().isAfter(inicio) || t.getData().isEqual(inicio)) &&
                (t.getData().isBefore(fim) || t.getData().isEqual(fim))) {
                
                if (t.getCategoria().equalsIgnoreCase("Depósito")) {
                    entradas += t.getValor();
                } else {
                    saidas += t.getValor();
                }
            }
        }

        relatorio.put("Entradas", entradas);
        relatorio.put("Saídas", saidas);
        relatorio.put("Saldo Final", entradas - saidas);

        return relatorio;
    }


   public boolean encerrarConta(int idConta) {
        Conta conta = buscarContaPorId(idConta);
        if (conta != null) {
            conta.setAtiva(false);
            return true;
        }
        return false;
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

    public static void main(String[] args) {
        
    }
}
