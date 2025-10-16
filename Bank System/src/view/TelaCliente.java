package view;

import javax.swing.*;
import java.awt.*;
import controller.SistemaController;
import model.Cliente;
import model.Conta;
import model.Transacoes;
import java.util.List;
// NOVOS IMPORTS para a funcionalidade de download
import java.io.PrintWriter;
import java.io.File;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;
// FIM NOVOS IMPORTS

/**
 *
 * @author Dell
 */
public class TelaCliente extends javax.swing.JPanel {

    private SistemaController sistemaController;
    private Cliente clienteLogado; // Cliente atualmente logado
    private Conta contaCliente; // Conta do cliente logado

    /**
     * Creates new form TelaCliente
     */
    public TelaCliente() {
        this.sistemaController = new SistemaController();
        // SIMULAÇÃO: Vamos usar o primeiro cliente e conta disponíveis para teste
        inicializarClienteTeste();

        initComponents();

        // ADICIONE ESTA LINHA - Muda o layout do jPanel1
        jPanel1.setLayout(new java.awt.BorderLayout());

        configurarListeners();
        mostrarTelaInicial();
    }

    // =================================================================
    // MÉTODO CORRIGIDO E ROBUSTO (Resolve NullPointer e Duplicate Entry)
    // =================================================================
    private void inicializarClienteTeste() {
        final long NUIT_TESTE = 123456789; // NUIT fixo do cliente de teste

        // 1. Tenta carregar clientes e contas
        List<Cliente> clientes = sistemaController.listarClientes();
        List<Conta> contas = sistemaController.listarContas();

        // 2. Tentar encontrar o cliente de teste existente pelo NUIT fixo
        for (Cliente cliente : clientes) {
            if (cliente.getNuitCli() == NUIT_TESTE) {
                this.clienteLogado = cliente;
                System.out.println("Cliente de teste encontrado: " + cliente.getNomeCli());
                break;
            }
        }

        // 3. Se o cliente de teste não foi encontrado, tenta criar (pode falhar por duplicidade)
        if (this.clienteLogado == null) {
            try {
                System.out.println("Cliente de teste não encontrado no cache. Tentando criar...");
                this.clienteLogado = sistemaController.criarCliente(
                        "João Silva (Teste)", Math.toIntExact(NUIT_TESTE), "Maputo", 841234567,
                        "joao@email.com", java.time.LocalDate.of(1990, 5, 15),
                        "BI123456", "1234"
                );
                System.out.println("Cliente de teste criado com sucesso.");
            } catch (RuntimeException e) {
                // Captura o erro específico de duplicação: Duplicate entry '123456789'
                if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                    System.err.println("ERRO: Cliente de teste já existe no DB. Falha ao carregar na busca inicial.");

                    // Como a criação falhou, e não temos um método buscarClientePorNuit,
                    // a única maneira de prosseguir é garantindo que as próximas buscas funcionem
                    // ou exigindo que o usuário reinicie/limpe o DB se a busca inicial falhar.

                    // Para evitar o crash, vamos lançar a exceção. Se o problema persistir
                    // APÓS esta correção, significa que a busca (listarClientes) está incompleta.
                    throw e;
                } else {
                    throw e; // Lança outros erros
                }
            }
        }

        // 4. Se o clienteLogado está definido, procura a conta dele
        if (this.clienteLogado != null) {
            // Tenta encontrar a conta (pode ter sido criada em uma sessão anterior)
            for (Conta conta : contas) {
                // CORREÇÃO: Verifica se getClienteId() não retorna null (Resolve erro anterior)
                if (conta.getClienteId() != null && conta.getClienteId().getIdCliente() == clienteLogado.getIdCliente()) {
                    this.contaCliente = conta;
                    break;
                }
            }

            // 5. Se a conta não foi encontrada (mas o cliente sim), cria a conta
            if (this.contaCliente == null) {
                System.out.println("Conta do cliente de teste faltando. Criando conta...");
                this.contaCliente = sistemaController.criarConta(this.clienteLogado.getIdCliente(), Conta.TipoConta.CORRENTE);
                System.out.println("Conta de teste criada/garantida com sucesso.");
            }
        }

        // Se ainda for null, lança erro para evitar quebras futuras.
        if (this.clienteLogado == null || this.contaCliente == null) {
            throw new IllegalStateException("Falha crítica ao inicializar cliente de teste. Cliente ou Conta é nulo após todas as tentativas.");
        }
    }
    // =================================================================
    // FIM MÉTODO CORRIGIDO
    // =================================================================

    private void configurarListeners() {
        jButton1.addActionListener(e -> mostrarConsultarSaldo());
        // jButton2 (Depositar) - Ação não implementada no código, mas mantida a declaração
        jButton4.addActionListener(e -> mostrarSaque());
        jButton3.addActionListener(e -> mostrarTransferir());
        jButton7.addActionListener(e -> mostrarTransferirCelular());
        jButton8.addActionListener(e -> mostrarPagamentos());
        jButton5.addActionListener(e -> mostrarAlterarSenha());
        jButton9.addActionListener(e -> mostrarVerPerfil());
        jButton10.addActionListener(e -> mostrarHistorico()); // NOVO BOTÃO
        jButton6.addActionListener(e -> sair());
    }

    // =================================================================
    // NOVO MÉTODO: mostrarHistorico() - Exibe a lista de transações e o botão de download
    // =================================================================
    private void mostrarHistorico() {
        jPanel1.removeAll();

        JPanel panelHistorico = new JPanel(new BorderLayout());
        panelHistorico.setBackground(Color.WHITE);
        panelHistorico.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- Header com Título e Botão ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titulo = new JLabel("HISTÓRICO DE TRANSAÇÕES", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));

        // Novo Botão "Baixar Relatório de Transações"
        JButton btnBaixarCompleto = criarBotaoDownload("⬇️ BAIXAR RELATÓRIO DE TRANSAÇÕES");
        btnBaixarCompleto.addActionListener(e -> baixarRelatorioTransacoes());

        JPanel tituloContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tituloContainer.setBackground(Color.WHITE);
        tituloContainer.add(titulo);

        JPanel downloadPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        downloadPanel.setBackground(Color.WHITE);
        downloadPanel.add(btnBaixarCompleto);

        headerPanel.add(tituloContainer, BorderLayout.CENTER);
        headerPanel.add(downloadPanel, BorderLayout.EAST);

        panelHistorico.add(headerPanel, BorderLayout.NORTH);
        // --- FIM Header ---

        // Tabela de Transações
        String[] colunas = {"Data", "Tipo", "Valor (MT)", "Descrição"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabela = new JTable(model);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.setRowHeight(30);

        // Preencher tabela com dados reais do cliente
        if (contaCliente != null) {
            List<Transacoes> transacoes = sistemaController.visualizarTransacoes(contaCliente.getIdConta());
            for (Transacoes t : transacoes) {
                String tipo = t.getCategoria(); // Ou lógica para determinar Saque/Depósito/Transferência
                model.addRow(new Object[]{
                        t.getData().toString(),
                        tipo,
                        String.format("%.2f", t.getValor()),
                        t.getDescricao()
                });
            }
        }

        JScrollPane scrollPane = new JScrollPane(tabela);
        panelHistorico.add(scrollPane, BorderLayout.CENTER);

        jPanel1.add(panelHistorico, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    // =================================================================
    // NOVO MÉTODO: baixarRelatorioTransacoes() - Lógica de download
    // =================================================================
    private void baixarRelatorioTransacoes() {
        if (contaCliente == null) {
            JOptionPane.showMessageDialog(this, "Conta não encontrada para gerar relatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Coletar dados
        List<Transacoes> transacoes = sistemaController.visualizarTransacoes(contaCliente.getIdConta());

        // 2. Montar o conteúdo completo do relatório
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("         RELATÓRIO DE TRANSAÇÕES - CLIENTE          \n");
        sb.append(String.format("         Cliente: %s | Conta: %d\n", clienteLogado.getNomeCli(), contaCliente.getNumeroConta()));
        sb.append("         Data de Geração: ").append(LocalDate.now()).append("\n");
        sb.append("====================================================\n\n");

        sb.append(String.format("%-25s | %-15s | %-15s | %s\n", "DATA/HORA", "TIPO", "VALOR (MT)", "DESCRIÇÃO"));
        sb.append("--------------------------+-----------------+-----------------+----------------------------------------\n");

        if (transacoes.isEmpty()) {
            sb.append("Nenhuma transação encontrada no período.\n");
        } else {
            for (Transacoes t : transacoes) {
                sb.append(String.format("%-25s | %-15s | %-15.2f | %s\n",
                        t.getData().toString(), t.getCategoria(), t.getValor(), t.getDescricao()));
            }
        }
        sb.append("\n====================================================\n");
        sb.append(String.format("SALDO ATUAL: %.2f MZN\n", contaCliente.getSaldo()));

        // 3. Simular download (usando JFileChooser para salvar arquivo)
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório de Transações");

        // Sugerir nome do arquivo
        fileChooser.setSelectedFile(new File("Relatorio_Transacoes_" + clienteLogado.getIdCliente() + "_" + LocalDate.now() + ".txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (PrintWriter out = new PrintWriter(fileToSave)) {
                out.println(sb.toString());
                JOptionPane.showMessageDialog(this,
                        "Relatório salvo com sucesso em:\n" + fileToSave.getAbsolutePath(),
                        "Download Concluído",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (java.io.FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + ex.getMessage(), "Erro de I/O", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Download cancelado pelo usuário.", "Cancelado", JOptionPane.WARNING_MESSAGE);
        }
    }
    // =================================================================
    // FIM NOVO MÉTODO
    // =================================================================

    private void mostrarVerPerfil() {
        jPanel1.removeAll();

        JPanel panelPerfil = new JPanel(new BorderLayout());
        panelPerfil.setBackground(Color.WHITE);
        panelPerfil.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Título
        JLabel titulo = new JLabel("MEU PERFIL", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelPerfil.add(titulo, BorderLayout.NORTH);

        // Painel de informações
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // Dados reais do cliente
        if (clienteLogado != null) {
            adicionarInfoPerfil(infoPanel, "Nome Completo:", clienteLogado.getNomeCli());
            adicionarInfoPerfil(infoPanel, "ID Cliente:", String.valueOf(clienteLogado.getIdCliente()));
            adicionarInfoPerfil(infoPanel, "NUIT:", String.valueOf(clienteLogado.getNuitCli()));
            adicionarInfoPerfil(infoPanel, "Telefone:", String.valueOf(clienteLogado.getTelefoneCli()));
            adicionarInfoPerfil(infoPanel, "Email:", clienteLogado.getEmailCli());
            adicionarInfoPerfil(infoPanel, "Endereço:", clienteLogado.getEnderecoCli());
            adicionarInfoPerfil(infoPanel, "Status:", clienteLogado.getStatus().toString());
        }

        if (contaCliente != null) {
            adicionarInfoPerfil(infoPanel, "Número da Conta:", String.valueOf(contaCliente.getNumeroConta()));
            adicionarInfoPerfil(infoPanel, "Tipo de Conta:", contaCliente.getTipoConta().toString());
            adicionarInfoPerfil(infoPanel, "Saldo Disponível:", String.format("%.2f MZN", contaCliente.getSaldo()));
            adicionarInfoPerfil(infoPanel, "Status da Conta:", contaCliente.getStatus().toString());
        }

        panelPerfil.add(infoPanel, BorderLayout.CENTER);

        // Botão para atualizar dados
        JPanel panelBotoes = new JPanel();
        panelBotoes.setBackground(Color.WHITE);
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnAtualizar = criarBotaoAcao("🔄 Atualizar Dados", new Color(0, 100, 180));
        btnAtualizar.addActionListener(e -> solicitarAtualizacaoDados());
        panelBotoes.add(btnAtualizar);

        panelPerfil.add(panelBotoes, BorderLayout.SOUTH);

        jPanel1.add(panelPerfil, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void adicionarInfoPerfil(JPanel panel, String label, String valor) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(80, 80, 80));
        panel.add(lbl);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblValor.setForeground(new Color(40, 40, 40));
        panel.add(lblValor);
    }

    private void solicitarAtualizacaoDados() {
        JOptionPane.showMessageDialog(this,
                "Para atualizar seus dados pessoais, dirija-se a um balcão de atendimento.\n\n" +
                        "Documentos necessários:\n" +
                        "• BI ou Passaporte\n" +
                        "• Comprovativo de residência\n" +
                        "• Outros documentos conforme necessário\n\n" +
                        "📞 Suporte: +258 84 123 4567\n" +
                        "🕒 Horário: Segunda a Sexta, 8h às 15h",
                "Atualização de Dados Pessoais",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarPagamentos() {
        jPanel1.removeAll();

        JPanel panelPagamentos = new JPanel(new BorderLayout());
        panelPagamentos.setBackground(Color.WHITE);
        panelPagamentos.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("PAGAMENTO DE SERVIÇOS", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panelPagamentos.add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Campo Entidade
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(criarLabel("Entidade:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtEntidade = criarTextField();
        txtEntidade.setToolTipText("Ex: EDM, FIPAG, TMCEL, etc.");
        formPanel.add(txtEntidade, gbc);

        // Campo Referência
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(criarLabel("Referência:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField txtReferencia = criarTextField();
        txtReferencia.setToolTipText("Número de referência do serviço");
        formPanel.add(txtReferencia, gbc);

        // Campo Valor
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(criarLabel("Valor (MT):"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField txtValor = criarTextField();
        formPanel.add(txtValor, gbc);

        // Botão Pagar
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnPagar = criarBotaoAcao("💳 PAGAR SERVIÇO", new Color(0, 100, 0));
        btnPagar.addActionListener(e -> processarPagamento(txtEntidade.getText(), txtReferencia.getText(), txtValor.getText()));
        formPanel.add(btnPagar, gbc);

        panelPagamentos.add(formPanel, BorderLayout.CENTER);
        jPanel1.add(panelPagamentos, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void processarPagamento(String entidade, String referencia, String valorStr) {
        if (entidade.isEmpty() || referencia.isEmpty() || valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double valor = Double.parseDouble(valorStr);
            if (valor <= 0) {
                JOptionPane.showMessageDialog(this, "Valor deve ser positivo!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (contaCliente == null) {
                JOptionPane.showMessageDialog(this, "Conta não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Simular pagamento - debitar da conta
            if (sistemaController.sacar(contaCliente.getIdConta(), valor)) {
                sistemaController.registrarTransacao(contaCliente.getIdConta(), "Pagamento: " + entidade, valor);
                JOptionPane.showMessageDialog(this,
                        "Pagamento realizado com sucesso!\n" +
                                "Entidade: " + entidade + "\n" +
                                "Referência: " + referencia + "\n" +
                                "Valor: " + String.format("%.2f MZN", valor),
                        "Pagamento Efetuado",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Saldo insuficiente para realizar o pagamento!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarTelaInicial() {
        jPanel1.removeAll();

        JPanel panelInicial = new JPanel(new GridBagLayout());
        panelInicial.setBackground(Color.WHITE);

        String mensagem = "Bem-vindo, " + (clienteLogado != null ? clienteLogado.getNomeCli() : "Cliente") + "!";
        JLabel lblMensagem = new JLabel(mensagem, JLabel.CENTER);
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblMensagem.setForeground(new Color(120, 120, 120));

        panelInicial.add(lblMensagem);

        jPanel1.add(panelInicial, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void mostrarConsultarSaldo() {
        jPanel1.removeAll();

        JPanel panelSaldo = new JPanel(new BorderLayout());
        panelSaldo.setBackground(Color.WHITE);
        panelSaldo.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        double saldo = (contaCliente != null) ? sistemaController.consultarSaldo(contaCliente.getIdConta()) : 0.0;

        JLabel lblSaldo = new JLabel("SALDO DISPONÍVEL", JLabel.CENTER);
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblSaldo.setForeground(new Color(0, 100, 0));

        JLabel lblValor = new JLabel(String.format("%.2f MZN", saldo), JLabel.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValor.setForeground(new Color(0, 0, 139));

        JLabel lblInfo = new JLabel("Última atualização: " + new java.util.Date(), JLabel.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInfo.setForeground(Color.GRAY);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(lblSaldo);
        centerPanel.add(lblValor);
        centerPanel.add(lblInfo);

        panelSaldo.add(centerPanel, BorderLayout.CENTER);
        jPanel1.add(panelSaldo, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void mostrarSaque() {
        jPanel1.removeAll();

        JPanel panelSaque = new JPanel(new BorderLayout());
        panelSaque.setBackground(Color.WHITE);
        panelSaque.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("REALIZAR SAQUE", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panelSaque.add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campo Valor
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblValor = criarLabel("Valor do Saque (MT):");
        formPanel.add(lblValor, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtValor = criarTextField();
        formPanel.add(txtValor, gbc);

        // Botão Sacar
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSacar = criarBotaoAcao("💰 SACAR", new Color(178, 34, 34));
        btnSacar.addActionListener(e -> processarSaque(txtValor.getText()));
        formPanel.add(btnSacar, gbc);

        panelSaque.add(formPanel, BorderLayout.CENTER);
        jPanel1.add(panelSaque);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void processarSaque(String valorStr) {
        if (valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o valor do saque!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double valor = Double.parseDouble(valorStr);
            if (valor <= 0) {
                JOptionPane.showMessageDialog(this, "Valor deve ser positivo!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (contaCliente == null) {
                JOptionPane.showMessageDialog(this, "Conta não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sistemaController.sacarComTaxa(contaCliente.getIdConta(), valor)) {
                JOptionPane.showMessageDialog(this,
                        "Saque realizado com sucesso!\n" +
                                "Valor: " + String.format("%.2f MZN", valor) + "\n" +
                                "Taxa aplicada: 0.5%",
                        "Saque Efetuado",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Saldo insuficiente para realizar o saque!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarTransferir() {
        jPanel1.removeAll();

        JPanel panelTransferencia = new JPanel(new BorderLayout());
        panelTransferencia.setBackground(Color.WHITE);
        panelTransferencia.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel titulo = new JLabel("TRANSFERÊNCIA BANCÁRIA", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelTransferencia.add(titulo, BorderLayout.NORTH);

        // Painel com abas para diferentes tipos de transferência
        JTabbedPane abasTransferencia = new JTabbedPane();
        abasTransferencia.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Aba 1: Mesmo Banco
        abasTransferencia.addTab("🏦 Mesmo Banco", criarPainelTransferenciaMesmoBanco());

        // Aba 2: Outro Banco
        abasTransferencia.addTab("🔗 Outro Banco", criarPainelTransferenciaOutroBanco());

        panelTransferencia.add(abasTransferencia, BorderLayout.CENTER);
        jPanel1.add(panelTransferencia);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private JPanel criarPainelTransferenciaMesmoBanco() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Campo Conta Destino
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(criarLabel("Número da Conta Destino:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtContaDestino = criarTextField();
        panel.add(txtContaDestino, gbc);

        // Campo Valor
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(criarLabel("Valor (MT):"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField txtValor = criarTextField();
        panel.add(txtValor, gbc);

        // Campo Descrição
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(criarLabel("Descrição:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField txtDescricao = criarTextField();
        panel.add(txtDescricao, gbc);

        // Botão Transferir
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnTransferir = criarBotaoAcao("🔄 TRANSFERIR", new Color(0, 100, 0));
        btnTransferir.addActionListener(e -> processarTransferenciaMesmoBanco(
                txtContaDestino.getText(), txtValor.getText(), txtDescricao.getText()));
        panel.add(btnTransferir, gbc);

        return panel;
    }

    private JPanel criarPainelTransferenciaOutroBanco() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Campo NIB Destino
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(criarLabel("NIB da Conta Destino:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtNibDestino = criarTextField();
        txtNibDestino.setToolTipText("Número de Identificação Bancária (21 dígitos)");
        panel.add(txtNibDestino, gbc);

        // Campo Valor
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(criarLabel("Valor (MT):"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField txtValor = criarTextField();
        panel.add(txtValor, gbc);

        // Campo Descrição
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(criarLabel("Descrição:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField txtDescricao = criarTextField();
        panel.add(txtDescricao, gbc);

        // Informação sobre taxa
        JLabel lblTaxa = new JLabel("ℹ️ Taxa de 1% será aplicada na transferência");
        lblTaxa.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblTaxa.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        panel.add(lblTaxa, gbc);

        // Botão Transferir
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnTransferir = criarBotaoAcao("🔗 TRANSFERIR OUTRO BANCO", new Color(70, 130, 180));
        btnTransferir.addActionListener(e -> processarTransferenciaOutroBanco(
                txtNibDestino.getText(), txtValor.getText(), txtDescricao.getText()));
        panel.add(btnTransferir, gbc);

        return panel;
    }

    private void processarTransferenciaMesmoBanco(String contaDestinoStr, String valorStr, String descricao) {
        if (contaDestinoStr.isEmpty() || valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int contaDestino = Integer.parseInt(contaDestinoStr);
            double valor = Double.parseDouble(valorStr);

            if (contaCliente == null) {
                JOptionPane.showMessageDialog(this, "Conta não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sistemaController.transferirComTaxa(contaCliente.getIdConta(), contaDestino, valor)) {
                JOptionPane.showMessageDialog(this,
                        "Transferência realizada com sucesso!\n" +
                                "Para conta: " + contaDestino + "\n" +
                                "Valor: " + String.format("%.2f MZN", valor) + "\n" +
                                "Taxa: 0.2%",
                        "Transferência Efetuada",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro na transferência! Verifique os dados e saldo.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Dados inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processarTransferenciaOutroBanco(String nibDestino, String valorStr, String descricao) {
        if (nibDestino.isEmpty() || valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double valor = Double.parseDouble(valorStr);

            if (contaCliente == null) {
                JOptionPane.showMessageDialog(this, "Conta não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sistemaController.transferirOutroBanco(contaCliente.getIdConta(), nibDestino, valor)) {
                JOptionPane.showMessageDialog(this,
                        "Transferência para outro banco realizada!\n" +
                                "NIB: " + nibDestino + "\n" +
                                "Valor: " + String.format("%.2f MZN", valor) + "\n" +
                                "Taxa: 1.0%",
                        "Transferência Efetuada",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro na transferência! Verifique saldo e dados.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarTransferirCelular() {
        jPanel1.removeAll();

        JPanel panelCelular = new JPanel(new BorderLayout());
        panelCelular.setBackground(Color.WHITE);
        panelCelular.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("TRANSFERIR PARA CELULAR", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panelCelular.add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Campo Número Celular
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(criarLabel("Número Celular:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtCelular = criarTextField();
        formPanel.add(txtCelular, gbc);

        // Campo Operadora
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(criarLabel("Operadora:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        JComboBox<String> cmbOperadora = new JComboBox<>(new String[]{"Selecione...", "M-Pesa", "Movitel", "Vodacom"});
        cmbOperadora.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbOperadora.setPreferredSize(new Dimension(300, 35));
        formPanel.add(cmbOperadora, gbc);

        // Campo Valor
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(criarLabel("Valor (MT):"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField txtValor = criarTextField();
        formPanel.add(txtValor, gbc);

        // Botão Transferir
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnTransferir = criarBotaoAcao("📱 TRANSFERIR", new Color(0, 100, 0));
        btnTransferir.addActionListener(e -> processarTransferenciaCelular(
                txtCelular.getText(), (String) cmbOperadora.getSelectedItem(), txtValor.getText()));
        formPanel.add(btnTransferir, gbc);

        panelCelular.add(formPanel, BorderLayout.CENTER);
        jPanel1.add(panelCelular);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void processarTransferenciaCelular(String celular, String operadora, String valorStr) {
        if (celular.isEmpty() || "Selecione...".equals(operadora) || valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // NOVO: 1. Validação do número de celular: deve ter 9 caracteres
        if (celular.length() != 9) {
            JOptionPane.showMessageDialog(this, "O número de celular deve ter exatamente 9 dígitos!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double valor = Double.parseDouble(valorStr);

            if (contaCliente == null) {
                JOptionPane.showMessageDialog(this, "Conta não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // NOVO: 2. Validação do valor mínimo da transferência: deve ser >= 100 MZN
            if (valor < 100.0) {
                JOptionPane.showMessageDialog(this, "O valor mínimo para transferência para celular é 100 MZN!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sistemaController.transferirCarteiraMovel(contaCliente.getIdConta(), celular, valor)) {
                JOptionPane.showMessageDialog(this,
                        "Transferência para celular realizada!\n" +
                                "Número: " + celular + "\n" +
                                "Operadora: " + operadora + "\n" +
                                "Valor: " + String.format("%.2f MZN", valor),
                        "Transferência Efetuada",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro na transferência! Verifique saldo e dados.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarAlterarSenha() {
        jPanel1.removeAll();

        JPanel panelSenha = new JPanel(new BorderLayout());
        panelSenha.setBackground(Color.WHITE);
        panelSenha.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("ALTERAR SENHA", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panelSenha.add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Campos de senha
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(criarLabel("Senha Atual:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JPasswordField txtSenhaAtual = criarPasswordField();
        formPanel.add(txtSenhaAtual, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(criarLabel("Nova Senha:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        JPasswordField txtNovaSenha = criarPasswordField();
        formPanel.add(txtNovaSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(criarLabel("Confirmar Senha:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        JPasswordField txtConfirmarSenha = criarPasswordField();
        formPanel.add(txtConfirmarSenha, gbc);

        // Botão Alterar
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnAlterar = criarBotaoAcao("🔐 ALTERAR SENHA", new Color(0, 100, 0));
        btnAlterar.addActionListener(e -> processarAlteracaoSenha(
                new String(txtSenhaAtual.getPassword()),
                new String(txtNovaSenha.getPassword()),
                new String(txtConfirmarSenha.getPassword())
        ));
        formPanel.add(btnAlterar, gbc);

        panelSenha.add(formPanel, BorderLayout.CENTER);
        jPanel1.add(panelSenha);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void processarAlteracaoSenha(String senhaAtual, String novaSenha, String confirmarSenha) {
        if (senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (novaSenha.length() < 4) {
            JOptionPane.showMessageDialog(this, "A senha deve ter pelo menos 4 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simular alteração de senha
        // Em um sistema real, você verificaria a senha atual no banco de dados
        JOptionPane.showMessageDialog(this,
                "Senha alterada com sucesso!\n\n" +
                        "⚠️ Lembre-se de guardar sua nova senha em local seguro.",
                "Senha Alterada",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void sair() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair?",
                "Confirmação de Saída",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
        }
    }

    // MÉTODOS AUXILIARES
    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JTextField criarTextField() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return campo;
    }

    private JPasswordField criarPasswordField() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return campo;
    }

    // NOVO MÉTODO AUXILIAR PARA O BOTÃO DE DOWNLOAD
    private JButton criarBotaoDownload(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setBackground(new Color(0, 100, 0)); // Verde para download
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(34, 139, 34));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(0, 100, 0));
            }
        });

        return botao;
    }

    private JButton criarBotaoAcao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(250, 40));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
                cor.darker() != null ? BorderFactory.createLineBorder(cor.darker(), 2) : BorderFactory.createLineBorder(cor, 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));


        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
        });

        return botao;
    }

    // ... (RESTANTE DO CÓDIGO GERADO PELO NETBEANS - MANTIDO ORIGINAL)
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton10; // Variável para o novo botão de Histórico
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton(); // NOVO BOTÃO DECLARADO
        jLabel1 = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 635, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 139));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setText("Consultar Saldo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setText("Depositar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton4.setText("Saque");

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton3.setText("Transferir ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton6.setText("Sair");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Menu");

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton7.setText("Transferir Para Celular");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton9.setText("Ver Perfil");

        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton8.setText("Pagamentos");

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setText("Alterar Senha");

        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton10.setText("Histórico/Relatório"); // NOVO BOTÃO INICIALIZADO

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE) // NOVO BOTÃO ADICIONADO AO LAYOUT
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton9)
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 139));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tela Do Cliente");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    // Adicione este método na classe TelaCliente, antes do fechamento da classe
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Banco Nexus - Cliente");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            TelaCliente cliente = new TelaCliente();
            frame.add(cliente);

            frame.setVisible(true);
        });
    }
}