package controller;

import model.Banco;
import model.Cliente;
import model.Conta;
import model.Emprestimo;
import model.Funcionario;
//import model.Emprestimo.emprestimos;
import model.Transacoes;
import DAO.BancoDAO;

import java.sql.SQLException;
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
    private Banco banco = new Banco("Nexus Bank", 1000000); // capital inicial de exemplo
     


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

 private BancoDAO bancoDAO; // declara o DAO

    public SistemaController() {
        bancoDAO = new BancoDAO(null); // inicializa o DAO
    }

     public void registrarLucroTransacao(double taxa) {
        banco.registrarLucroTaxa(taxa);

        try {
            BancoDAO bancoDAO = new BancoDAO(null);
            bancoDAO.atualizarBanco(banco);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    // ====== DEFINIR TAXA ======
    double taxa = valor * 0.01; // 1% de taxa sobre o valor da transferência

    // Verifica se há saldo suficiente para valor + taxa
    if (origem.getSaldo() < (valor + taxa)) return false;

    // Debita valor e taxa da conta
    origem.sacar(valor);
    origem.sacar(taxa);

    // Registra lucro da taxa no banco
    banco.registrarLucroTaxa(taxa);
   // bancoDAO.atualizarBanco(banco); // persiste o lucro e capital atualizado

    // Simula envio para outro banco (externo)
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


    // ====== Depósito Flexível ======
public boolean registrarDepositoFlexivel(Integer idConta, Integer numeroConta, Double valor, String referencia, String entidade) {
    if (valor == null || valor <= 0) return false;

    Conta conta = null;

    // tentar encontrar a conta pelo id ou pelo número
    if (idConta != null) {
        conta = buscarContaPorId(idConta);
    }
    if (conta == null && numeroConta != null) {
        conta = buscarContaPorNumero(numeroConta);
    }

    if (conta != null) {
        conta.depositar(valor);
        registrarTransacao(conta.getIdConta(), "Depósito", valor, referencia, entidade);
        return true;
    }
    return false;
}

// ====== Levantamento Flexível ======
public boolean registrarLevantamentoFlexivel(Integer idConta, Integer numeroConta, Double valor, String referencia, String entidade) {
    if (valor == null || valor <= 0) return false;

    Conta conta = null;

    if (idConta != null) {
        conta = buscarContaPorId(idConta);
    }
    if (conta == null && numeroConta != null) {
        conta = buscarContaPorNumero(numeroConta);
    }

    if (conta != null && conta.getSaldo() >= valor) {
        conta.sacar(valor);
        registrarTransacao(conta.getIdConta(), "Saque", valor, referencia, entidade);
        return true;
    }
    return false;
}

// Atualizado registrarTransacao para incluir referência e entidade
public Transacoes registrarTransacao(int idConta, String categoria, double valor, 
                                     String referencia, String entidade) {
    Conta conta = buscarContaPorId(idConta);
    if (conta == null) return null;

    int idT = gerarIdTransacao();
    LocalDateTime now = LocalDateTime.now();

    Transacoes.TipoTransacao tipo = Transacoes.TipoTransacao.DEPOSITO;
    if (categoria != null) {
        String c = categoria.toLowerCase();
        if (c.contains("saque") || c.contains("levant")) tipo = Transacoes.TipoTransacao.SAQUE;
        else if (c.contains("transfer")) tipo = Transacoes.TipoTransacao.TRANSFERENCIA;
    }

    Transacoes.StatusTransacao status = Transacoes.StatusTransacao.CONCLUIDA;

    // Taxa de exemplo
    double taxa = valor * 0.01;
    if (tipo != Transacoes.TipoTransacao.DEPOSITO && conta.getSaldo() >= taxa) {
        conta.sacar(taxa);
        banco.registrarLucroTaxa(taxa);
    }

    Transacoes t = new Transacoes(
        idT,
        tipo,
        valor,
        now,
        conta.getIdConta(),
        null,
        status,
        categoria,
        conta,
        categoria,
        now,
        false
    );
    
    // Guarda as informações de referência e entidade se tua classe Transacoes tiver campos
    t.setReferencia(referencia);
    t.setEntidade(entidade);

    transacoes.add(t);
    return t;
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

    /**
 * Pesquisa clientes por termo, podendo filtrar por ID, nome ou número da conta.
 * tipo: "ID", "Nome" ou "Nº Conta"
 */
    public List<Cliente> pesquisarClientes(String termo, String tipo) {
        if (termo == null || termo.trim().isEmpty()) return listarClientes();
        termo = termo.trim();
        List<Cliente> encontrados = new ArrayList<>();

        if ("ID".equalsIgnoreCase(tipo)) {
            try {
                int id = Integer.parseInt(termo);
                Cliente c = buscarClientePorId(id);
                if (c != null) encontrados.add(c);
                return encontrados;
            } catch (NumberFormatException e) {
                return encontrados;
            }
        }

        if ("Nome".equalsIgnoreCase(tipo)) {
            for (Cliente c : clientes) {
                // usa o getter de nome que tiveres na classe Cliente
                if (c.getNomeCli() != null && c.getNomeCli().toLowerCase().contains(termo.toLowerCase())) {
                    encontrados.add(c);
                }
            }
            return encontrados;
        }

        if ("Nº Conta".equalsIgnoreCase(tipo) || "NUM_CONTA".equalsIgnoreCase(tipo)) {
            try {
                int numero = Integer.parseInt(termo);
                for (Conta ct : contas) {
                    if (ct.getNumeroConta() == numero) {
                        // ct.getClienteId() pode retornar Cliente ou int dependendo da tua implementação
                        Object clienteRef = ct.getClienteId();
                        int cid = -1;
                        if (clienteRef instanceof Cliente) cid = ((Cliente) clienteRef).getIdCliente();
                        else if (clienteRef instanceof Integer) cid = (Integer) clienteRef;

                        if (cid != -1) {
                            Cliente c = buscarClientePorId(cid);
                            if (c != null) encontrados.add(c);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                // termo não numérico -> nada
            }
            return encontrados;
        }

        // fallback: pesquisa por nome
        for (Cliente c : clientes) {
            if (c.getNomeCli() != null && c.getNomeCli().toLowerCase().contains(termo.toLowerCase())) encontrados.add(c);
        }
        return encontrados;
    }

    public Conta abrirContaCliente(String nomeCli, int nuitcli, String endereco, int telefone, String email, String     documento, Conta.TipoConta tipoConta) {
        Cliente novoCliente = new Cliente(
            nomeCli,
            proximoIdCliente++,
            nuitcli,
            endereco,
            telefone,
            email,
            LocalDate.now(),
            Cliente.Status.ATIVO,
            documento,
            "1234"
        );

        clientes.add(novoCliente);

        // Cria a conta e armazena numa variável
        Conta novaConta = criarConta(novoCliente.getIdCliente(), tipoConta);

        // Atualiza banco
        banco.adicionarConta(novaConta); // agora funciona

        return novaConta;
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
   // Registrar transação com taxa do banco
public Transacoes registrarTransacao(int idConta, String categoria, double valor) {
    Conta conta = buscarContaPorId(idConta);
    if (conta == null) return null;

    int idT = gerarIdTransacao();

    // Determinar tipo de transação
    Transacoes.TipoTransacao tipo = Transacoes.TipoTransacao.DEPOSITO;
    if (categoria != null) {
        String c = categoria.toLowerCase();
        if (c.contains("saque") || c.contains("levant")) tipo = Transacoes.TipoTransacao.SAQUE;
        else if (c.contains("transfer")) tipo = Transacoes.TipoTransacao.TRANSFERENCIA;
        else tipo = Transacoes.TipoTransacao.DEPOSITO;
    }

    Transacoes.StatusTransacao status = Transacoes.StatusTransacao.CONCLUIDA;

    LocalDateTime now = LocalDateTime.now();

    // ======== COBRAR TAXA ========
    double taxa = valor * 0.01; // exemplo: 1% de taxa sobre valor da transação
    if (tipo != Transacoes.TipoTransacao.DEPOSITO && conta.getSaldo() >= taxa) {
        conta.sacar(taxa);                 // desconta da conta
        banco.registrarLucroTaxa(taxa);    // atualiza lucro do banco
       // bancoDAO.atualizarBanco(banco);    // persiste no banco de dados
    }

    // Criar objeto Transação
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
            // Registrar lucro automático do banco
            // Calcula os juros com base no valor solicitado e na taxa de juros do empréstimo
            double juros = e.getValorSolicitado() * e.getTaxaJuro(); 

            // Registra o lucro no banco
            banco.registrarLucro(juros);

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


   public boolean sacarComTaxa(int idConta, double valor) {
    Conta conta = buscarContaPorId(idConta);
    if (conta != null && valor > 0 && conta.getSaldo() >= valor) {
        double taxa = valor * 0.005; // 0.5% de taxa
        conta.sacar(valor + taxa);    // saca valor + taxa
        banco.registrarLucroTaxa(taxa);

        // registra a transação
        registrarTransacao(idConta, "Saque", valor);
        return true;
    }
    return false;
}

    public boolean transferirComTaxa(int idOrigem, int idDestino, double valor) {
        Conta origem = buscarContaPorId(idOrigem);
        Conta destino = buscarContaPorId(idDestino);
        if (origem != null && destino != null && valor > 0 && origem.getSaldo() >= valor) {
            double taxa = valor * 0.002; // 0.2% de taxa
            origem.sacar(valor + taxa);
            destino.depositar(valor);
            banco.registrarLucroTaxa(taxa);

            registrarTransacao(idOrigem, "Transferência", valor);
            return true;
        }
        return false;
    }

    // ===================== MÉTODOS DE AUTENTICAÇÃO =====================

/**
 * Autentica um usuário baseado no identificador (email, username ou ID)
 * e retorna o tipo de usuário autenticado
 */
public String autenticarUsuario(String identificador, String senha) {
    if (identificador == null || senha == null) {
        return "invalido";
    }
    
    identificador = identificador.trim().toLowerCase();
    
    // 1. Verifica se é Administrador
    if (autenticarAdmin(identificador, senha)) {
        return "admin";
    }
    
    // 2. Verifica se é Gestor
    if (autenticarGestor(identificador, senha)) {
        return "gestor";
    }
    
    // 3. Verifica se é Funcionário
    if (autenticarFuncionario(identificador, senha)) {
        return "funcionario";
    }
    
    // 4. Verifica se é Cliente (por email ou ID)
    if (autenticarCliente(identificador, senha)) {
        return "cliente";
    }
    
    return "invalido";
}

/**
 * Autenticação para Administrador
 */
private boolean autenticarAdmin(String identificador, String senha) {
    return ("admin".equals(identificador) || "administrador".equals(identificador)) 
           && "admin123".equals(senha);
}

/**
 * Autenticação para Gestor
 */
private boolean autenticarGestor(String identificador, String senha) {
    // Pode ser "gestor" ou "gestorX" onde X é o ID
    if (identificador.startsWith("gestor")) {
        if ("gestor".equals(identificador) && "gestor123".equals(senha)) {
            return true;
        }
        // Verifica se é gestor com ID específico
        try {
            String idStr = identificador.substring(6); // Remove "gestor"
            if (!idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                for (Funcionario func : funcionarios) {
                    if (func.getIdFuncionario() == id && 
                        "Gestor".equalsIgnoreCase(func.getCargo()) &&
                        "gestor123".equals(senha)) {
                        return true;
                    }
                }
            }
        } catch (NumberFormatException e) {
            // Não é um ID numérico, continua
        }
    }
    return false;
}

/**
 * Autenticação para Funcionário
 */
private boolean autenticarFuncionario(String identificador, String senha) {
    // Pode ser "funcionario", "funcX" ou email do funcionário
    if (identificador.startsWith("func")) {
        if ("funcionario".equals(identificador) && "funcionario123".equals(senha)) {
            return true;
        }
        // Verifica se é funcionário com ID específico
        try {
            String idStr = identificador.substring(4); // Remove "func"
            if (!idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                for (Funcionario func : funcionarios) {
                    if (func.getIdFuncionario() == id && 
                        "funcionario123".equals(senha)) {
                        return true;
                    }
                }
            }
        } catch (NumberFormatException e) {
            // Não é um ID numérico, continua
        }
    }
    
    // Verifica por email (se tiver email nos funcionários)
    for (Funcionario func : funcionarios) {
        // Se a classe Funcionario tiver email, verifica aqui
        if (func.getNomeCompletoFunc().toLowerCase().contains(identificador) &&
            "funcionario123".equals(senha)) {
            return true;
        }
    }
    
    return false;
}

/**
 * Autenticação para Cliente (por email ou ID)
 */
private boolean autenticarCliente(String identificador, String senha) {
    for (Cliente cliente : clientes) {
        // Verifica por email
        if (cliente.getEmailCli() != null && 
            cliente.getEmailCli().toLowerCase().equals(identificador) &&
            cliente.getSenhacli().equals(senha)) {
            return true;
        }
        
        // Verifica por ID (ex: "cliente1", "123")
        try {
            int id = Integer.parseInt(identificador);
            if (cliente.getIdCliente() == id && cliente.getSenhacli().equals(senha)) {
                return true;
            }
        } catch (NumberFormatException e) {
            // Não é um ID numérico, continua
        }
        
        // Verifica por nome (se nome for único)
        if (cliente.getNomeCli() != null && 
            cliente.getNomeCli().toLowerCase().equals(identificador) &&
            cliente.getSenhacli().equals(senha)) {
            return true;
        }
    }
    return false;
}

/**
 * Retorna o cliente autenticado para uso na TelaCliente
 */
public Cliente getClienteAutenticado(String identificador) {
    for (Cliente cliente : clientes) {
        // Verifica por email
        if (cliente.getEmailCli() != null && 
            cliente.getEmailCli().equalsIgnoreCase(identificador)) {
            return cliente;
        }
        
        // Verifica por ID
        try {
            int id = Integer.parseInt(identificador);
            if (cliente.getIdCliente() == id) {
                return cliente;
            }
        } catch (NumberFormatException e) {
            // Não é ID numérico
        }
        
        // Verifica por nome
        if (cliente.getNomeCli() != null && 
            cliente.getNomeCli().equalsIgnoreCase(identificador)) {
            return cliente;
        }
    }
    return null;
}

/**
 * Retorna dados do usuário para exibição (nome)
 */
public String getNomeUsuario(String identificador, String tipoUsuario) {
    switch (tipoUsuario) {
        case "admin":
            return "Administrador";
        case "gestor":
            return "Gestor do Sistema";
        case "funcionario":
            return "Funcionário";
        case "cliente":
            Cliente cliente = getClienteAutenticado(identificador);
            return cliente != null ? cliente.getNomeCli() : "Cliente";
        default:
            return "Usuário";
    }
}

    // ================= MÉTODOS PARA RELATÓRIOS E CÁLCULOS DO CAIXA =================

    // Retorna o saldo total de todas as contas
    public double calcularSaldoTotal() {
        double total = 0.0;
        for (Conta conta : contas) {
            total += conta.getSaldo();
        }
        return total;
    }

    // Retorna o total de depósitos feitos (baseado em histórico simples)
    public double getTotalDepositos() {
        double totalDepositos = 0.0;
        for (Conta conta : contas) {
            totalDepositos += conta.getTotalDepositos(); // Este método deve existir em Conta
        }
        return totalDepositos;
    }

    // Retorna o total de saques realizados
    public double getTotalSaques() {
        double totalSaques = 0.0;
        for (Conta conta : contas) {
            totalSaques += conta.getTotalSaques(); // Este método deve existir em Conta
        }
        return totalSaques;
    }

    // Gera um pequeno relatório textual com informações resumidas do caixa
    public String gerarRelatorioCaixa() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DO CAIXA ===\n\n");
        sb.append("Total de Contas: ").append(contas.size()).append("\n");
        sb.append("Saldo Total no Banco: ").append(String.format("%.2f", calcularSaldoTotal())).append(" MZN\n");
        sb.append("Total de Depósitos: ").append(String.format("%.2f", getTotalDepositos())).append(" MZN\n");
        sb.append("Total de Saques: ").append(String.format("%.2f", getTotalSaques())).append(" MZN\n");
        sb.append("\n-----------------------------\n");
        sb.append("Data: ").append(java.time.LocalDate.now()).append("\n");
        return sb.toString();
    }

     public List<Transacoes> listarDepositos() {
        List<Transacoes> res = new ArrayList<>();
        for (Transacoes t : transacoes) {
            String cat = t.getCategoria();
            if (cat != null && cat.toLowerCase().contains("dep")) {
                res.add(t);
            }
        }
        return res;
     }

    /**
     * Lista apenas as transações categorizadas como Saque
     */
    public List<Transacoes> listarSaques() {
        List<Transacoes> res = new ArrayList<>();
        for (Transacoes t : transacoes) {
            String cat = t.getCategoria();
            if (cat != null && (cat.toLowerCase().contains("saque") || cat.toLowerCase().contains("levant"))) {
                res.add(t);
            }
        }
        return res;
    }
    
    // Buscar conta pelo número da conta (usado em operações flexíveis)
public Conta buscarContaPorNumero(int numeroConta) {
    for (Conta c : contas) { // supondo que tens uma lista de contas chamada 'contas'
        if (c.getNumeroConta() == numeroConta) {
            return c;
        }
    }
    return null;
}

// Gera um novo ID incremental para transações
private int gerarNovoIdTransacao() {
    return transacoes.size() + 1; // assumindo que tens uma lista chamada 'transacoes'
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
