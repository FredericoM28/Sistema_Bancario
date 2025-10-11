package view;

import controller.SistemaController;
import model.Cliente;
import model.Conta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PainelFuncAtendimento extends JFrame {

    private JPanel menuLateral, painelTopo, painelPrincipal;
    private JLabel lblBanco, lblFuncionario;
    private JButton btnDashboard, btnGerirClientes, btnNovaConta, btnAtualizarDados, btnEncerrarConta, btnCartoes, btnSuporte, btnSair;
    private SistemaController controller;
    private String nomeFuncionario, cargoFuncionario;

    // Componentes do painel Clientes
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;
    private JTextField txtPesquisar;
    private JComboBox<String> comboTipoPesquisa;
    private JButton btnPesquisar, btnEditarCliente, btnEliminarCliente, btnAtualizarTabela;

    public PainelFuncAtendimento(SistemaController controller, String nomeFuncionario, String cargoFuncionario) {
        this.controller = controller;
        this.nomeFuncionario = nomeFuncionario;
        this.cargoFuncionario = cargoFuncionario;
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Painel do Funcionário - Atendimento");
        setSize(1150, 740);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        // Painel lateral
        menuLateral = new JPanel();
        menuLateral.setBackground(new Color(25, 45, 90));
        menuLateral.setPreferredSize(new Dimension(220, 0));
        menuLateral.setLayout(new GridLayout(11, 1, 0, 6));

        // Informações do funcionário
        JPanel painelInfo = new JPanel(new GridLayout(3, 1));
        painelInfo.setBackground(new Color(20, 40, 80));
        lblFuncionario = new JLabel("<html><center>" + nomeFuncionario + "<br><small>" + cargoFuncionario + "</small></center></html>", SwingConstants.CENTER);
        lblFuncionario.setForeground(Color.WHITE);
        lblFuncionario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        painelInfo.add(new JLabel(""));
        painelInfo.add(lblFuncionario);
        painelInfo.add(new JLabel(""));
        menuLateral.add(painelInfo);

        // Botões laterais (mantendo o visual)
        btnDashboard = criarBotao("Dashboard");
        btnGerirClientes = criarBotao("Gerir Clientes");
        btnNovaConta = criarBotao("Nova Conta");
        btnAtualizarDados = criarBotao("Atualizar Dados");
        btnEncerrarConta = criarBotao("Encerrar Conta");
        btnCartoes = criarBotao("Cartões");
        btnSuporte = criarBotao("Suporte");
        btnSair = criarBotao("Sair");

        menuLateral.add(btnDashboard);
        menuLateral.add(btnGerirClientes);
        menuLateral.add(btnNovaConta);
        menuLateral.add(btnAtualizarDados);
        menuLateral.add(btnEncerrarConta);
        menuLateral.add(btnCartoes);
        menuLateral.add(btnSuporte);
        menuLateral.add(btnSair);

        // Painel topo
        painelTopo = new JPanel();
        painelTopo.setBackground(new Color(20, 70, 140));
        painelTopo.setPreferredSize(new Dimension(0, 60));
        painelTopo.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        lblBanco = new JLabel("Banco Nexus");
        lblBanco.setForeground(Color.WHITE);
        lblBanco.setFont(new Font("Segoe UI", Font.BOLD, 20));
        painelTopo.add(lblBanco);

        // Painel principal
        painelPrincipal = new JPanel(new CardLayout());
        painelPrincipal.setBackground(Color.WHITE);

        // Cards: dashboard e gerir clientes + os demais painéis já existentes
        painelPrincipal.add(criarPainelDashboard(), "dashboard");
        painelPrincipal.add(criarPainelGerirClientes(), "gerirClientes");
        painelPrincipal.add(new PainelNovaConta(controller), "novaConta");
        painelPrincipal.add(new PainelAtualizarDados(controller), "atualizarDados");
        painelPrincipal.add(new PainelEncerrarConta(controller), "encerrarConta");
        painelPrincipal.add(new PainelCartoes(controller), "cartoes");
        painelPrincipal.add(new PainelSuporte(controller), "suporte");

        // Ações dos botões (troca de cards)
        CardLayout cl = (CardLayout) painelPrincipal.getLayout();
        btnDashboard.addActionListener(e -> cl.show(painelPrincipal, "dashboard"));
        btnGerirClientes.addActionListener(e -> {
            atualizarTabela(); // sempre atualizar antes de mostrar
            cl.show(painelPrincipal, "gerirClientes");
        });
        btnNovaConta.addActionListener(e -> cl.show(painelPrincipal, "novaConta"));
        btnAtualizarDados.addActionListener(e -> cl.show(painelPrincipal, "atualizarDados"));
        btnEncerrarConta.addActionListener(e -> cl.show(painelPrincipal, "encerrarConta"));
        btnCartoes.addActionListener(e -> cl.show(painelPrincipal, "cartoes"));
        btnSuporte.addActionListener(e -> cl.show(painelPrincipal, "suporte"));
        btnSair.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Sair", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) dispose();
        });

        add(menuLateral, BorderLayout.WEST);
        add(painelTopo, BorderLayout.NORTH);
        add(painelPrincipal, BorderLayout.CENTER);
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botao.setFocusPainted(false);
        botao.setBackground(new Color(30, 60, 120));
        botao.setBorderPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { botao.setBackground(new Color(40, 80, 160)); }
            public void mouseExited(MouseEvent e) { botao.setBackground(new Color(30, 60, 120)); }
        });
        return botao;
    }

    private JPanel criarPainelDashboard() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Bem-vindo ao Painel de Atendimento", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(new Color(20, 70, 140));
        painel.add(lbl, BorderLayout.CENTER);
        return painel;
    }

    /**
     * Painel Gerir Clientes: tabela, pesquisa e botões de ação.
     */
    private JPanel criarPainelGerirClientes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Topo: título + pesquisa
        JPanel topo = new JPanel(new BorderLayout(8, 8));
        topo.setBackground(Color.WHITE);
        JLabel titulo = new JLabel("Gerir Clientes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(20, 70, 140));
        topo.add(titulo, BorderLayout.WEST);

        // painel de pesquisa
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelPesquisa.setBackground(Color.WHITE);
        comboTipoPesquisa = new JComboBox<>(new String[] {"ID", "Nome", "Nº Conta"});
        comboTipoPesquisa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPesquisar = new JTextField(20);
        txtPesquisar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setBackground(new Color(40, 90, 160));
        btnPesquisar.setForeground(Color.WHITE);
        btnPesquisar.setFocusPainted(false);

        painelPesquisa.add(comboTipoPesquisa);
        painelPesquisa.add(txtPesquisar);
        painelPesquisa.add(btnPesquisar);
        topo.add(painelPesquisa, BorderLayout.EAST);

        painel.add(topo, BorderLayout.NORTH);

        // Tabela com scroll
        String[] colunas = {"ID", "Nome", "NUIT", "Endereço", "Telefone", "Email", "Idade", "Documento", "Status", "Nº Conta"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            // células não-editáveis diretamente
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaClientes = new JTable(modeloTabela);
        tabelaClientes.setRowHeight(26);
        tabelaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaClientes.getTableHeader().setBackground(new Color(30, 70, 130));
        tabelaClientes.getTableHeader().setForeground(Color.WHITE);
        tabelaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(tabelaClientes);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        painel.add(scroll, BorderLayout.CENTER);

        // Rodapé: botões de ação
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        rodape.setBackground(Color.WHITE);

        btnEditarCliente = new JButton("Editar");
        btnEliminarCliente = new JButton("Eliminar");
        btnAtualizarTabela = new JButton("Actualizar");

        Dimension smallBtn = new Dimension(100, 32);
        btnEditarCliente.setPreferredSize(smallBtn);
        btnEliminarCliente.setPreferredSize(smallBtn);
        btnAtualizarTabela.setPreferredSize(smallBtn);

        btnEditarCliente.setBackground(new Color(20, 120, 20));
        btnEditarCliente.setForeground(Color.WHITE);
        btnEditarCliente.setFocusPainted(false);

        btnEliminarCliente.setBackground(new Color(180, 40, 40));
        btnEliminarCliente.setForeground(Color.WHITE);
        btnEliminarCliente.setFocusPainted(false);

        btnAtualizarTabela.setBackground(new Color(20, 70, 140));
        btnAtualizarTabela.setForeground(Color.WHITE);
        btnAtualizarTabela.setFocusPainted(false);

        rodape.add(btnAtualizarTabela);
        rodape.add(btnEditarCliente);
        rodape.add(btnEliminarCliente);

        painel.add(rodape, BorderLayout.SOUTH);

        // Ações
        btnPesquisar.addActionListener(e -> pesquisarEPreencher());
        txtPesquisar.addActionListener(e -> pesquisarEPreencher());
        btnAtualizarTabela.addActionListener(e -> atualizarTabela());

        btnEliminarCliente.addActionListener(e -> {
            int idx = tabelaClientes.getSelectedRow();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente da tabela.");
                return;
            }
            int id = Integer.parseInt(modeloTabela.getValueAt(idx, 0).toString());
            int op = JOptionPane.showConfirmDialog(this, "Eliminar cliente ID " + id + " ?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                boolean ok = controller.eliminarCliente(id); // usa método existente
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado.");
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao eliminar cliente.");
                }
            }
        });

        btnEditarCliente.addActionListener(e -> {
            int idx = tabelaClientes.getSelectedRow();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para editar.");
                return;
            }
            // Lê dados atuais
            int id = Integer.parseInt(modeloTabela.getValueAt(idx, 0).toString());
            String nome = modeloTabela.getValueAt(idx, 1).toString();
            String email = modeloTabela.getValueAt(idx, 5).toString();
            String telefone = modeloTabela.getValueAt(idx, 4).toString();

            // Mostra diálogo simples para editar nome/email/telefone
            JTextField fNome = new JTextField(nome);
            JTextField fEmail = new JTextField(email);
            JTextField fTel = new JTextField(telefone);
            Object[] msg = {
                    "Nome:", fNome,
                    "Email:", fEmail,
                    "Telefone:", fTel
            };
            int op = JOptionPane.showConfirmDialog(this, msg, "Editar Cliente ID " + id, JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION) {
                try {
                    boolean ok = controller.atualizarDadosCliente(id, fNome.getText(), fEmail.getText(), Integer.parseInt(fTel.getText()));
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Cliente atualizado.");
                        atualizarTabela();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro: não foi possível atualizar.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Telefone deve ser numérico.");
                }
            }
        });

        return painel;
    }

    /**
     * Atualiza tabela com todos os clientes do controller
     */
    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        List<Cliente> clientes = controller.listarClientes();
        // Para cada cliente, tentamos obter NºConta via controller.listarContas()
        List<Conta> contas = controller.listarContas();

        for (Cliente c : clientes) {
            // Abaixo uso getters que são prováveis na tua classe Cliente.
            // Se os nomes forem diferentes, adapta (ex: getNome() -> getNomeCli()).
            String id = String.valueOf(c.getIdCliente());                // assumed getter
            String nome = safeString(c.getNomeCli());                   // assumed getter
            String nuit = safeString(String.valueOf(c.getNuitCli()));   // assumed getter
            String endereco = safeString(c.getEnderecoCli());           // assumed getter
            String telefone = safeString(String.valueOf(c.getTelefoneCli())); // assumed getter
            String email = safeString(c.getEmailCli());                 // assumed getter
            String idade = safeString(String.valueOf(c.getIdadeCli())); // assumed getter (LocalDate -> toString)
            String documento = safeString(c.getDocumentoCli());         // assumed getter
            String status = safeString(String.valueOf(c.getStatus()));  // assumed getter

            // procura nº conta (assume Conta tem getNumeroConta() e getClienteId() ou getCliente())
            String numeroConta = "";
            for (Conta ct : contas) {
                try {
                    // ct.getClienteId() pode retornar Cliente ou int dependendo da tua implementação
                    Object clienteRef = ct.getClienteId(); // conforme comentário no teu controller
                    int cid = -1;
                    if (clienteRef instanceof Cliente) {
                        cid = ((Cliente) clienteRef).getIdCliente();
                    } else if (clienteRef instanceof Integer) {
                        cid = (Integer) clienteRef;
                    }
                    if (cid == c.getIdCliente()) {
                        numeroConta = String.valueOf(ct.getNumeroConta()); // assumed getter
                        break;
                    }
                } catch (Exception ignore) {}
            }

            modeloTabela.addRow(new Object[]{id, nome, nuit, endereco, telefone, email, idade, documento, status, numeroConta});
        }
    }

    /**
     * Pesquisa conforme comboTipoPesquisa e preenche tabela com resultados.
     */
    private void pesquisarEPreencher() {
        String tipo = (String) comboTipoPesquisa.getSelectedItem();
        String termo = txtPesquisar.getText().trim();
        if (termo.isEmpty()) {
            atualizarTabela();
            return;
        }

        modeloTabela.setRowCount(0);
        List<Cliente> clientes = controller.listarClientes();
        List<Conta> contas = controller.listarContas();

        for (Cliente c : clientes) {
            boolean matches = false;
            if ("ID".equals(tipo)) {
                try {
                    int idBusca = Integer.parseInt(termo);
                    matches = c.getIdCliente() == idBusca;
                } catch (NumberFormatException ex) {
                    matches = false;
                }
            } else if ("Nome".equals(tipo)) {
                matches = c.getNomeCli().toLowerCase().contains(termo.toLowerCase());
            } else if ("Nº Conta".equals(tipo)) {
                try {
                    int numBusca = Integer.parseInt(termo);
                    for (Conta ct : contas) {
                        if (String.valueOf(ct.getNumeroConta()).equals(String.valueOf(numBusca))) {
                            Object clienteRef = ct.getClienteId();
                            int cid = (clienteRef instanceof Cliente) ? ((Cliente) clienteRef).getIdCliente() : (Integer) clienteRef;
                            if (cid == c.getIdCliente()) { matches = true; break; }
                        }
                    }
                } catch (NumberFormatException ex) {
                    matches = false;
                }
            }

            if (matches) {
                // preencher linha (mesmo código do atualizarTabela)
                String id = String.valueOf(c.getIdCliente());
                String nome = safeString(c.getNomeCli());
                String nuit = safeString(String.valueOf(c.getNuitCli()));
                String endereco = safeString(c.getEnderecoCli());
                String telefone = safeString(String.valueOf(c.getTelefoneCli()));
                String email = safeString(c.getEmailCli());
                String idade = safeString(String.valueOf(c.getIdadeCli()));
                String documento = safeString(c.getDocumentoCli());
                String status = safeString(String.valueOf(c.getStatus()));
                String numeroConta = "";
                for (Conta ct : contas) {
                    try {
                        Object clienteRef = ct.getClienteId();
                        int cid = (clienteRef instanceof Cliente) ? ((Cliente) clienteRef).getIdCliente() : (Integer) clienteRef;
                        if (cid == c.getIdCliente()) {
                            numeroConta = String.valueOf(ct.getNumeroConta());
                            break;
                        }
                    } catch (Exception ignore) {}
                }

                modeloTabela.addRow(new Object[]{id, nome, nuit, endereco, telefone, email, idade, documento, status, numeroConta});
            }
        }
    }

    private String safeString(String s) {
        return s == null ? "" : s;
    }

    // ----------------- Painéis internos reutilizáveis (mantive tus classes originais) -----------------

    /**
 * Painel para criar nova conta + tabela com contas existentes.
 */
public class PainelNovaConta extends JPanel {
    private SistemaController controller;
    private JTextField txtNome, txtNuit, txtEndereco, txtTelefone, txtEmail, txtDocumento;
    private JComboBox<Conta.TipoConta> comboTipoConta;
    private DefaultTableModel modeloContas;
    private JTable tabelaContas;

    public PainelNovaConta(SistemaController controller) {
        this.controller = controller;
        initComponents();
        preencherTabelaContas();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top - formulário
        JPanel topo = new JPanel(new GridBagLayout());
        topo.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Abrir Nova Conta");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(20,70,140));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        topo.add(titulo, gbc);
        gbc.gridwidth = 1;

        txtNome = new JTextField(18);
        txtNuit = new JTextField(12);
        txtEndereco = new JTextField(18);
        txtTelefone = new JTextField(12);
        txtEmail = new JTextField(18);
        txtDocumento = new JTextField(12);
        comboTipoConta = new JComboBox<>(Conta.TipoConta.values());

        int linha = 1;
        gbc.gridx = 0; gbc.gridy = linha; topo.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; topo.add(txtNome, gbc); linha++;

        gbc.gridx = 0; gbc.gridy = linha; topo.add(new JLabel("NUIT:"), gbc);
        gbc.gridx = 1; topo.add(txtNuit, gbc); linha++;

        gbc.gridx = 0; gbc.gridy = linha; topo.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; topo.add(txtEndereco, gbc); linha++;

        gbc.gridx = 0; gbc.gridy = linha; topo.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; topo.add(txtTelefone, gbc); linha++;

        gbc.gridx = 0; gbc.gridy = linha; topo.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; topo.add(txtEmail, gbc); linha++;

        gbc.gridx = 0; gbc.gridy = linha; topo.add(new JLabel("Documento (BI):"), gbc);
        gbc.gridx = 1; topo.add(txtDocumento, gbc); linha++;

        gbc.gridx = 0; gbc.gridy = linha; topo.add(new JLabel("Tipo de Conta:"), gbc);
        gbc.gridx = 1; topo.add(comboTipoConta, gbc); linha++;

        JButton btnAbrir = new JButton("Abrir Conta");
        btnAbrir.setBackground(new Color(20,70,140));
        btnAbrir.setForeground(Color.WHITE);
        btnAbrir.setPreferredSize(new Dimension(160, 34));

        gbc.gridx = 1; gbc.gridy = linha; gbc.anchor = GridBagConstraints.CENTER;
        topo.add(btnAbrir, gbc);

        add(topo, BorderLayout.NORTH);

        // Centro - tabela de contas
        String[] col = {"ID Conta","Nº Conta","Tipo","Saldo","Status","Cliente"};
        modeloContas = new DefaultTableModel(col,0) {
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        tabelaContas = new JTable(modeloContas);
        tabelaContas.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(tabelaContas);
        scroll.setPreferredSize(new Dimension(800, 300));
        add(scroll, BorderLayout.CENTER);

        // Rodapé - actions
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(Color.WHITE);
        JButton btnListar = new JButton("Actualizar Lista");
        JButton btnVerHistorico = new JButton("Ver Histórico Conta (selecionada)");
        btnListar.setBackground(new Color(0,120,200)); btnListar.setForeground(Color.WHITE);
        btnVerHistorico.setBackground(new Color(0,120,200)); btnVerHistorico.setForeground(Color.WHITE);
        rodape.add(btnVerHistorico);
        rodape.add(btnListar);
        add(rodape, BorderLayout.SOUTH);

        // Ações
        btnAbrir.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                int nuit = Integer.parseInt(txtNuit.getText().trim());
                String endereco = txtEndereco.getText().trim();
                int telefone = Integer.parseInt(txtTelefone.getText().trim());
                String email = txtEmail.getText().trim();
                String documento = txtDocumento.getText().trim();
                Conta.TipoConta tipo = (Conta.TipoConta) comboTipoConta.getSelectedItem();

                Conta c = controller.abrirContaCliente(nome, nuit, endereco, telefone, email, documento, tipo);
                if (c != null) {
                    JOptionPane.showMessageDialog(this, "Conta criada! ID: " + c.getIdConta() + " Nº: " + c.getNumeroConta());
                    limparFormulario();
                    preencherTabelaContas();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao criar conta.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Campos numéricos inválidos (NUIT/Telefone).");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnListar.addActionListener(e -> preencherTabelaContas());

        btnVerHistorico.addActionListener(e -> {
            int sel = tabelaContas.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this, "Selecione uma conta."); return; }
            int idConta = Integer.parseInt(modeloContas.getValueAt(sel,0).toString());
            List<?> historico = controller.consultarHistorico(idConta);
            JOptionPane.showMessageDialog(this, "Historico: " + historico.size() + " registos (ver console).");
            // podes abrir um diálogo mais rico aqui. Em app leve mostro apenas contagem.
        });
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtNuit.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtDocumento.setText("");
        comboTipoConta.setSelectedIndex(0);
    }

    private void preencherTabelaContas() {
        modeloContas.setRowCount(0);
        List<Conta> contas = controller.listarContas();
        for (Conta c : contas) {
            String clienteNome = tryGetClienteNome(c);
            modeloContas.addRow(new Object[]{
                    c.getIdConta(),
                    c.getNumeroConta(),
                    c.getTipoConta(),
                    String.format("%.2f", c.getSaldo()),
                    c.getStatus(),
                    clienteNome
            });
        }
    }

    // tenta recuperar nome do cliente pois Conta tem getters variados entre implementações
    private String tryGetClienteNome(Conta c) {
        try {
            // se Conta tem getCliente() retornando Cliente
            Cliente cli = null;
            try {
                Object possible = c.getClienteId();
                if (possible instanceof Cliente) cli = (Cliente) possible;
                else {
                    // se getClienteId devolve int, procurar
                    int cid = Integer.parseInt(String.valueOf(possible));
                    cli = controller.buscarClientePorId(cid);
                }
            } catch (Exception ex) {
                // tenta método getCliente (algumas versões)
                try {
                    java.lang.reflect.Method m = c.getClass().getMethod("getCliente");
                    Object ret = m.invoke(c);
                    if (ret instanceof Cliente) cli = (Cliente) ret;
                } catch (Exception ignored){}
            }
            return (cli != null) ? cli.getNomeCli() : "";
        } catch (Exception ex) {
            return "";
        }
    }
}

    public class PainelEncerrarConta extends JPanel {
    private SistemaController controller;
    private DefaultTableModel modeloContas;
    private JTable tabelaContas;
    private JTextField txtIdEncerrar;

    public PainelEncerrarConta(SistemaController controller) {
        this.controller = controller;
        initComponents();
        preencherTabela();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        JLabel titulo = new JLabel("Encerrar Conta");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(20,70,140));
        add(titulo, BorderLayout.NORTH);

        // tabela
        String[] cols = {"ID Conta","Nº Conta","Tipo","Saldo","Status"};
        modeloContas = new DefaultTableModel(cols,0) { @Override public boolean isCellEditable(int r,int c){return false;} };
        tabelaContas = new JTable(modeloContas);
        tabelaContas.setRowHeight(24);
        add(new JScrollPane(tabelaContas), BorderLayout.CENTER);

        // rodape com actions
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(Color.WHITE);

        JButton btnListar = new JButton("Actualizar Lista");
        JButton btnEncerrarSel = new JButton("Encerrar Conta Selecionada");
        JButton btnEncerrarById = new JButton("Encerrar por ID");

        txtIdEncerrar = new JTextField(8);

        btnListar.setBackground(new Color(0,120,200)); btnListar.setForeground(Color.WHITE);
        btnEncerrarSel.setBackground(new Color(180,40,40)); btnEncerrarSel.setForeground(Color.WHITE);
        btnEncerrarById.setBackground(new Color(180,40,40)); btnEncerrarById.setForeground(Color.WHITE);

        rodape.add(new JLabel("ID:"));
        rodape.add(txtIdEncerrar);
        rodape.add(btnEncerrarById);
        rodape.add(btnEncerrarSel);
        rodape.add(btnListar);

        add(rodape, BorderLayout.SOUTH);

        // ações
        btnListar.addActionListener(e -> preencherTabela());

        btnEncerrarSel.addActionListener(e -> {
            int sel = tabelaContas.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this, "Selecione uma conta."); return; }
            int idConta = Integer.parseInt(modeloContas.getValueAt(sel,0).toString());
            int op = JOptionPane.showConfirmDialog(this, "Encerrar conta ID " + idConta + " ?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                boolean ok = controller.encerrarContaCliente(idConta);
                JOptionPane.showMessageDialog(this, ok ? "Conta encerrada." : "Falha ao encerrar conta.");
                preencherTabela();
            }
        });

        btnEncerrarById.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtIdEncerrar.getText().trim());
                int op = JOptionPane.showConfirmDialog(this, "Encerrar conta ID " + id + " ?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (op == JOptionPane.YES_OPTION) {
                    boolean ok = controller.encerrarContaCliente(id);
                    JOptionPane.showMessageDialog(this, ok ? "Conta encerrada." : "Falha ao encerrar conta.");
                    preencherTabela();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        });
    }

    private void preencherTabela() {
        modeloContas.setRowCount(0);
        List<Conta> contas = controller.listarContas();
        for (Conta c : contas) {
            modeloContas.addRow(new Object[]{
                    c.getIdConta(),
                    c.getNumeroConta(),
                    c.getTipoConta(),
                    String.format("%.2f", c.getSaldo()),
                    c.getStatus()
            });
        }
    }
}

    public class PainelCartoes extends JPanel {
    private SistemaController controller;
    private DefaultTableModel modeloContas;
    private JTable tabelaContas;
    private JTextField txtIdCartao;

    public PainelCartoes(SistemaController controller) {
        this.controller = controller;
        initComponents();
        preencherTabela();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8,8));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        JLabel titulo = new JLabel("Cartões - Reemissão");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(20,70,140));
        add(titulo, BorderLayout.NORTH);

        String[] cols = {"ID Conta","Nº Conta","Cliente","Saldo","Status"};
        modeloContas = new DefaultTableModel(cols,0) { @Override public boolean isCellEditable(int r,int c){return false;} };
        tabelaContas = new JTable(modeloContas);
        tabelaContas.setRowHeight(24);
        add(new JScrollPane(tabelaContas), BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(Color.WHITE);
        txtIdCartao = new JTextField(8);
        JButton btnReemitir = new JButton("Reemitir Cartão (ID)");
        JButton btnReemitirSel = new JButton("Reemitir Cartão (Selecionada)");
        JButton btnList = new JButton("Actualizar Lista");

        btnReemitir.setBackground(new Color(20,70,140)); btnReemitir.setForeground(Color.WHITE);
        btnReemitirSel.setBackground(new Color(20,70,140)); btnReemitirSel.setForeground(Color.WHITE);
        btnList.setBackground(new Color(0,120,200)); btnList.setForeground(Color.WHITE);

        rodape.add(new JLabel("ID:"));
        rodape.add(txtIdCartao);
        rodape.add(btnReemitir);
        rodape.add(btnReemitirSel);
        rodape.add(btnList);
        add(rodape, BorderLayout.SOUTH);

        btnList.addActionListener(e -> preencherTabela());

        btnReemitir.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtIdCartao.getText().trim());
                String r = controller.reemitirCartao(id);
                JOptionPane.showMessageDialog(this, r);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        });

        btnReemitirSel.addActionListener(e -> {
            int sel = tabelaContas.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this, "Selecione uma conta."); return; }
            int id = Integer.parseInt(modeloContas.getValueAt(sel,0).toString());
            String r = controller.reemitirCartao(id);
            JOptionPane.showMessageDialog(this, r);
        });
    }

    private void preencherTabela() {
        modeloContas.setRowCount(0);
        List<Conta> contas = controller.listarContas();
        for (Conta c : contas) {
            String cliente = "";
            try {
                Object clienteRef = c.getClienteId();
                if (clienteRef instanceof Integer) {
                    int cid = (Integer) clienteRef;
                    // tentar obter nome via controller
                    cliente = controller.buscarClientePorId(cid) != null ? controller.buscarClientePorId(cid).getNomeCli() : "";
                } else if (clienteRef instanceof model.Cliente) {
                    cliente = ((model.Cliente) clienteRef).getNomeCli();
                }
            } catch (Exception ignore) {}
            modeloContas.addRow(new Object[]{
                    c.getIdConta(),
                    c.getNumeroConta(),
                    cliente,
                    String.format("%.2f", c.getSaldo()),
                    c.getStatus()
            });
        }
    }
}

public class PainelAtualizarDados extends JPanel {
    private SistemaController controller;
    private DefaultTableModel modeloClientes;
    private JTable tabelaClientes;

    public PainelAtualizarDados(SistemaController controller) {
        this.controller = controller;
        initComponents();
        preencherTabela();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8,8));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        JLabel titulo = new JLabel("Atualizar Dados dos Clientes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(20,70,140));
        add(titulo, BorderLayout.NORTH);

        String[] cols = {"ID","Nome","NUIT","Telefone","Email"};
        modeloClientes = new DefaultTableModel(cols,0) { @Override public boolean isCellEditable(int r,int c){return false;} };
        tabelaClientes = new JTable(modeloClientes);
        tabelaClientes.setRowHeight(24);
        add(new JScrollPane(tabelaClientes), BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(Color.WHITE);
        JButton btnAtualizar = new JButton("Actualizar Lista");
        JButton btnEditar = new JButton("Editar Selecionado");
        JButton btnVer = new JButton("Ver Detalhes");

        btnAtualizar.setBackground(new Color(0,120,200)); btnAtualizar.setForeground(Color.WHITE);
        btnEditar.setBackground(new Color(20,70,140)); btnEditar.setForeground(Color.WHITE);
        btnVer.setBackground(new Color(20,70,140)); btnVer.setForeground(Color.WHITE);

        rodape.add(btnVer);
        rodape.add(btnEditar);
        rodape.add(btnAtualizar);
        add(rodape, BorderLayout.SOUTH);

        btnAtualizar.addActionListener(e -> preencherTabela());

        btnVer.addActionListener(e -> {
            int sel = tabelaClientes.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this, "Selecione um cliente."); return; }
            int id = Integer.parseInt(modeloClientes.getValueAt(sel,0).toString());
            Cliente c = controller.buscarClientePorId(id);
            if (c != null) {
                String msg = "ID: " + c.getIdCliente() + "\nNome: " + c.getNomeCli() + "\nNUIT: " + c.getNuitCli()
                        + "\nEmail: " + c.getEmailCli() + "\nTelefone: " + c.getTelefoneCli()
                        + "\nEndereço: " + c.getEnderecoCli();
                JOptionPane.showMessageDialog(this, msg, "Detalhes Cliente", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
            }
        });

        btnEditar.addActionListener(e -> {
            int sel = tabelaClientes.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this, "Selecione um cliente."); return; }
            int id = Integer.parseInt(modeloClientes.getValueAt(sel,0).toString());
            Cliente c = controller.buscarClientePorId(id);
            if (c == null) { JOptionPane.showMessageDialog(this, "Cliente não encontrado."); return; }

            JTextField fNome = new JTextField(c.getNomeCli());
            JTextField fEmail = new JTextField(c.getEmailCli());
            JTextField fTel = new JTextField(String.valueOf(c.getTelefoneCli()));
            Object[] msg = {"Nome:", fNome, "Email:", fEmail, "Telefone:", fTel};
            int op = JOptionPane.showConfirmDialog(this, msg, "Editar Cliente ID " + id, JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION) {
                try {
                    boolean ok = controller.atualizarDadosCliente(id, fNome.getText().trim(), fEmail.getText().trim(), Integer.parseInt(fTel.getText().trim()));
                    JOptionPane.showMessageDialog(this, ok ? "Atualizado com sucesso." : "Falha ao atualizar.");
                    preencherTabela();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Telefone inválido.");
                }
            }
        });
    }

    private void preencherTabela() {
        modeloClientes.setRowCount(0);
        List<Cliente> clientes = controller.listarClientes();
        for (Cliente c : clientes) {
            modeloClientes.addRow(new Object[]{
                    c.getIdCliente(),
                    c.getNomeCli(),
                    c.getNuitCli(),
                    c.getTelefoneCli(),
                    c.getEmailCli()
            });
        }
    }
}

    class PainelSuporte extends JPanel {
        public PainelSuporte(SistemaController controller) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            JTextArea area = new JTextArea("Área de suporte técnico e irregularidades.\n\nRegiste aqui reclamações ou problemas dos clientes.");
            area.setEditable(false);
            area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            add(new JScrollPane(area), BorderLayout.CENTER);
        }
    }

    // Main para testar a UI (exemplo)
    public static void main(String[] args) {
        SistemaController sc = new SistemaController();
        // Adiciona alguns dados de teste para visualização (opcional)
        sc.criarCliente("João Silva", 123456789, "Rua A, 10", 823456789, "joao@mail.com", java.time.LocalDate.of(1990,1,1), "BI12345", "senha");
        sc.criarCliente("Maria Santos", 987654321, "Av B, 20", 844556677, "maria@mail.com", java.time.LocalDate.of(1985,5,5), "BI98765", "senha");

        SwingUtilities.invokeLater(() -> {
            PainelFuncAtendimento p = new PainelFuncAtendimento(sc, "Frederico", "Atendimento");
            p.setVisible(true);
        });
    }
}
