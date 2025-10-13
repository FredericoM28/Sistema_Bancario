package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.SistemaController;
import model.Cliente;
import model.Conta;
import model.Funcionario;
import model.Transacoes;
import java.util.List;
import java.util.Map;

public class TelaAdmn extends JFrame {

    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel contentPanel;
    private JLabel titleLabel;
    private JButton btnGerenciarGestores;
    private JButton btnGerenciarFuncionarios;
    private JButton btnRelatorios;
    private JButton btnSair;
    
    // Controller do backend
    private SistemaController sistemaController;

    public TelaAdmn() {
        this.sistemaController = new SistemaController(); // Inicializa o controller
        initComponents();
        setTitle("Sistema Bancário - Banco Nexus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Painel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título
        titleLabel = new JLabel("PAINEL DO ADMINISTRADOR", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0, 0, 139));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Container para menu e conteúdo
        JPanel container = new JPanel(new BorderLayout(5, 5));
        container.setBackground(Color.WHITE);

        // Menu lateral
        menuPanel = criarMenuPanel();
        container.add(menuPanel, BorderLayout.WEST);

        // Painel de conteúdo
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Mensagem inicial
        mostrarMensagemInicial();
        container.add(contentPanel, BorderLayout.CENTER);

        mainPanel.add(container, BorderLayout.CENTER);
        add(mainPanel);

        configurarListeners();
    }

    private JPanel criarMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0, 0, 139));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        panel.setPreferredSize(new Dimension(280, 0));

        // Título do menu
        JLabel menuTitle = new JLabel("MENU", JLabel.CENTER);
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panel.add(menuTitle);

        // Botão Gerenciar Gestores
        btnGerenciarGestores = criarBotaoMenu("👨‍💼 GERENCIAR GESTORES");
        panel.add(btnGerenciarGestores);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Botão Gerenciar Funcionários
        btnGerenciarFuncionarios = criarBotaoMenu("👥 GERENCIAR FUNCIONÁRIOS");
        panel.add(btnGerenciarFuncionarios);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Botão Relatórios
        btnRelatorios = criarBotaoMenu("📊 VER RELATÓRIOS");
        panel.add(btnRelatorios);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Espaço flexível
        panel.add(Box.createVerticalGlue());

        // Botão Sair
        btnSair = criarBotaoSair("🚪 SAIR");
        panel.add(btnSair);

        return panel;
    }

    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setBackground(Color.WHITE);
        botao.setForeground(new Color(0, 0, 139));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(250, 55));
        
        // Efeitos visuais
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(240, 240, 255));
                botao.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 180), 2),
                    BorderFactory.createEmptyBorder(14, 19, 14, 19)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(Color.WHITE);
                botao.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }
        });
        
        return botao;
    }

    private JButton criarBotaoSair(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setBackground(new Color(178, 34, 34));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(180, 50));
        
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(200, 0, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(178, 34, 34));
            }
        });
        
        return botao;
    }

    private void configurarListeners() {
        btnGerenciarGestores.addActionListener(e -> mostrarFormGestores());
        btnGerenciarFuncionarios.addActionListener(e -> mostrarFormFuncionarios());
        btnRelatorios.addActionListener(e -> mostrarRelatorios());
        btnSair.addActionListener(e -> sair());
    }

    private void mostrarMensagemInicial() {
        contentPanel.removeAll();
        
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        
        JLabel lblMensagem = new JLabel("Selecione uma opção no menu");
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblMensagem.setForeground(new Color(120, 120, 120));
        
        panelCentral.add(lblMensagem);
        contentPanel.add(panelCentral, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void mostrarFormGestores() {
        contentPanel.removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Título
        JLabel titulo = new JLabel("GERENCIAR GESTORES", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(titulo, BorderLayout.NORTH);

        // Painel com abas
        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Aba 1: Listar Clientes
        abas.addTab("👥 Listar Clientes", criarPainelClientes());
        
        // Aba 2: Listar Contas
        abas.addTab("🏦 Listar Contas", criarPainelContas());
        
        // Aba 3: Transações
        abas.addTab("💳 Transações", criarPainelTransacoes());

        mainPanel.add(abas, BorderLayout.CENTER);
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel criarPainelClientes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botão atualizar
        JButton btnAtualizar = new JButton("🔄 Atualizar Lista");
        btnAtualizar.setBackground(new Color(0, 100, 0));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtualizar.addActionListener(e -> atualizarTabelaClientes());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(btnAtualizar);

        panel.add(topPanel, BorderLayout.NORTH);

        // Tabela de clientes
        String[] colunas = {"ID", "Nome", "NUIT", "Email", "Telefone", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.setRowHeight(25);
        
        JScrollPane scroll = new JScrollPane(tabela);
        panel.add(scroll, BorderLayout.CENTER);

        // Carregar dados iniciais
        atualizarTabelaClientes();

        return panel;
    }

    private void atualizarTabelaClientes() {
        // Encontrar a tabela no contentPanel
        Component[] components = contentPanel.getComponents();
        if (components.length > 0) {
            JPanel mainPanel = (JPanel) components[0];
            JTabbedPane abas = (JTabbedPane) mainPanel.getComponent(1);
            JPanel painelClientes = (JPanel) abas.getComponent(0);
            JScrollPane scroll = (JScrollPane) painelClientes.getComponent(1);
            JTable tabela = (JTable) scroll.getViewport().getView();
            DefaultTableModel model = (DefaultTableModel) tabela.getModel();
            
            // Limpar tabela
            model.setRowCount(0);
            
            // Buscar clientes do controller
            List<Cliente> clientes = sistemaController.listarClientes();
            
            // Popular tabela
            for (Cliente cliente : clientes) {
                model.addRow(new Object[]{
                    cliente.getIdCliente(),
                    cliente.getNomeCli(),
                    cliente.getNuitCli(),
                    cliente.getEmailCli(),
                    cliente.getTelefoneCli(),
                    cliente.getStatus().toString()
                });
            }
            
            JOptionPane.showMessageDialog(this, 
                "Lista atualizada! Total: " + clientes.size() + " clientes",
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel criarPainelContas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAtualizar = new JButton("🔄 Atualizar Lista");
        btnAtualizar.setBackground(new Color(0, 100, 0));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtualizar.addActionListener(e -> atualizarTabelaContas());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(btnAtualizar);

        panel.add(topPanel, BorderLayout.NORTH);

        // Tabela de contas
        String[] colunas = {"ID", "Número", "Tipo", "Cliente", "Saldo", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.setRowHeight(25);
        
        JScrollPane scroll = new JScrollPane(tabela);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void atualizarTabelaContas() {
        Component[] components = contentPanel.getComponents();
        if (components.length > 0) {
            JPanel mainPanel = (JPanel) components[0];
            JTabbedPane abas = (JTabbedPane) mainPanel.getComponent(1);
            JPanel painelContas = (JPanel) abas.getComponent(1);
            JScrollPane scroll = (JScrollPane) painelContas.getComponent(1);
            JTable tabela = (JTable) scroll.getViewport().getView();
            DefaultTableModel model = (DefaultTableModel) tabela.getModel();
            
            model.setRowCount(0);
            
            List<Conta> contas = sistemaController.listarContas();
            
            for (Conta conta : contas) {
                model.addRow(new Object[]{
                    conta.getIdConta(),
                    conta.getNumeroConta(),
                    conta.getTipoConta().toString(),
                    conta.getClienteId().getNomeCli(),
                    String.format("%.2f MZN", conta.getSaldo()),
                    conta.getStatus().toString()
                });
            }
            
            JOptionPane.showMessageDialog(this, 
                "Contas atualizadas! Total: " + contas.size() + " contas",
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel criarPainelTransacoes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnVerTransacoes = new JButton("📋 Ver Todas Transações");
        btnVerTransacoes.setBackground(new Color(70, 130, 180));
        btnVerTransacoes.setForeground(Color.WHITE);
        btnVerTransacoes.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVerTransacoes.addActionListener(e -> mostrarTodasTransacoes());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(btnVerTransacoes);

        panel.add(topPanel, BorderLayout.NORTH);

        // Área para mostrar transações
        JTextArea areaTransacoes = new JTextArea();
        areaTransacoes.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaTransacoes.setEditable(false);
        
        JScrollPane scroll = new JScrollPane(areaTransacoes);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void mostrarTodasTransacoes() {
        Component[] components = contentPanel.getComponents();
        if (components.length > 0) {
            JPanel mainPanel = (JPanel) components[0];
            JTabbedPane abas = (JTabbedPane) mainPanel.getComponent(1);
            JPanel painelTransacoes = (JPanel) abas.getComponent(2);
            JScrollPane scroll = (JScrollPane) painelTransacoes.getComponent(1);
            JTextArea areaTransacoes = (JTextArea) scroll.getViewport().getView();
            
            List<Transacoes> transacoes = sistemaController.visualizarTodasTransacoes();
            
            StringBuilder sb = new StringBuilder();
            sb.append("=== TODAS AS TRANSAÇÕES ===\n\n");
            
            if (transacoes.isEmpty()) {
                sb.append("Nenhuma transação encontrada.\n");
            } else {
                for (Transacoes t : transacoes) {
                    sb.append(String.format("ID: %d | Tipo: %s | Valor: %.2f MZN | Data: %s\n",
                        t.getId(), 
                        t.getCategoria(),
                        t.getValor(),
                        t.getData().toString()
                    ));
                }
                sb.append(String.format("\nTotal: %d transações", transacoes.size()));
            }
            
            areaTransacoes.setText(sb.toString());
        }
    }

    private void mostrarFormFuncionarios() {
        contentPanel.removeAll();
        
        JPanel mainFormPanel = new JPanel(new BorderLayout());
        mainFormPanel.setBackground(Color.WHITE);
        mainFormPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        // Título
        JLabel titulo = new JLabel("CADASTRO DE FUNCIONÁRIO", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainFormPanel.add(titulo, BorderLayout.NORTH);

        // Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 6, 10);

        // Campos
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel lblNome = criarLabelFormulario("Nome:");
        formPanel.add(lblNome, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtNome = criarCampoTexto();
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblCargo = criarLabelFormulario("Cargo:");
        formPanel.add(lblCargo, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        JComboBox<String> cmbCargo = new JComboBox<>(new String[]{"Caixa", "Atendente", "Gestor"});
        cmbCargo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCargo.setPreferredSize(new Dimension(320, 38));
        formPanel.add(cmbCargo, gbc);

        // Botão Salvar
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSalvar = criarBotaoAcao("💾 SALVAR FUNCIONÁRIO", new Color(0, 100, 0));
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String cargo = (String) cmbCargo.getSelectedItem();
            
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha o nome do funcionário!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Chamar o controller para criar funcionário
            Funcionario func = sistemaController.criarFuncionario(nome, cargo);
            
            if (func != null) {
                JOptionPane.showMessageDialog(this, 
                    "Funcionário criado com sucesso!\nID: " + func.getIdFuncionario(), 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                txtNome.setText("");
                cmbCargo.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao criar funcionário!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(btnSalvar, gbc);

        mainFormPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(mainFormPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void mostrarRelatorios() {
        contentPanel.removeAll();
        
        JPanel relatorioPanel = new JPanel(new BorderLayout());
        relatorioPanel.setBackground(Color.WHITE);
        relatorioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("RELATÓRIOS DO SISTEMA", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        relatorioPanel.add(titulo, BorderLayout.NORTH);

        // Painel de botões
        JPanel botoesPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Botão Performance Banco
        JButton btnPerformance = criarBotaoRelatorio("📈 Performance Banco", new Color(0, 100, 0));
        btnPerformance.addActionListener(e -> mostrarPerformanceBanco());
        botoesPanel.add(btnPerformance);

        // Botão Relatório Caixa
        JButton btnRelatorioCaixa = criarBotaoRelatorio("💰 Relatório Caixa", new Color(70, 130, 180));
        btnRelatorioCaixa.addActionListener(e -> mostrarRelatorioCaixa());
        botoesPanel.add(btnRelatorioCaixa);

        // Botão Configurar Taxas
        JButton btnTaxas = criarBotaoRelatorio("⚙️ Configurar Taxas", new Color(165, 42, 42));
        btnTaxas.addActionListener(e -> configurarTaxasJuros());
        botoesPanel.add(btnTaxas);

        // Botão Estatísticas
        JButton btnEstatisticas = criarBotaoRelatorio("📊 Estatísticas", new Color(128, 0, 128));
        btnEstatisticas.addActionListener(e -> mostrarEstatisticas());
        botoesPanel.add(btnEstatisticas);

        relatorioPanel.add(botoesPanel, BorderLayout.CENTER);
        contentPanel.add(relatorioPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JButton criarBotaoRelatorio(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
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

    private void mostrarPerformanceBanco() {
        Map<String, Object> performance = sistemaController.analisarPerformanceBanco();
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== PERFORMANCE DO BANCO ===\n\n");
        
        for (Map.Entry<String, Object> entry : performance.entrySet()) {
            sb.append(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
        }
        
        // Adicionar informações adicionais
        sb.append("\n=== INFORMAÇÕES ADICIONAIS ===\n");
        sb.append(String.format("Total Clientes: %d\n", sistemaController.listarClientes().size()));
        sb.append(String.format("Total Contas: %d\n", sistemaController.listarContas().size()));
        sb.append(String.format("Total Transações: %d\n", sistemaController.visualizarTodasTransacoes().size()));
        
        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setEditable(false);
        
        JOptionPane.showMessageDialog(this, new JScrollPane(area), 
            "Performance do Banco", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarRelatorioCaixa() {
        String relatorio = sistemaController.gerarRelatorioCaixa();
        
        JTextArea area = new JTextArea(relatorio);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setEditable(false);
        
        JOptionPane.showMessageDialog(this, new JScrollPane(area), 
            "Relatório de Caixa", JOptionPane.INFORMATION_MESSAGE);
    }

    private void configurarTaxasJuros() {
        String taxaStr = JOptionPane.showInputDialog(this, 
            "Digite a nova taxa de juros (ex: 0.05 para 5%):", 
            "0.05");
        
        if (taxaStr != null && !taxaStr.trim().isEmpty()) {
            try {
                double novaTaxa = Double.parseDouble(taxaStr);
                sistemaController.definirTaxasJuros(novaTaxa);
                JOptionPane.showMessageDialog(this, 
                    "Taxa de juros atualizada para: " + (novaTaxa * 100) + "%",
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Valor inválido! Use formato decimal (ex: 0.05)", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarEstatisticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTATÍSTICAS DO SISTEMA ===\n\n");
        
        List<Cliente> clientes = sistemaController.listarClientes();
        List<Conta> contas = sistemaController.listarContas();
        List<Transacoes> transacoes = sistemaController.visualizarTodasTransacoes();
        
        sb.append(String.format("📊 Clientes Cadastrados: %d\n", clientes.size()));
        sb.append(String.format("🏦 Contas Ativas: %d\n", contas.size()));
        sb.append(String.format("💳 Transações Realizadas: %d\n", transacoes.size()));
        
        // Calcular saldo total
        double saldoTotal = 0;
        for (Conta conta : contas) {
            saldoTotal += conta.getSaldo();
        }
        sb.append(String.format("💰 Saldo Total no Banco: %.2f MZN\n", saldoTotal));
        
        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setEditable(false);
        
        JOptionPane.showMessageDialog(this, new JScrollPane(area), 
            "Estatísticas do Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    // MÉTODOS AUXILIARES (mantidos da versão original)
    private JLabel criarLabelFormulario(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JTextField criarCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(320, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return campo;
    }

    private JPasswordField criarCampoSenha() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(320, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return campo;
    }

    private JButton criarBotaoAcao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(250, 45));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor.darker(), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
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

    private void sair() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair do painel administrativo?",
            "Confirmação de Saída",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            // new LoginNV().setVisible(true); // Voltar para login
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaAdmn().setVisible(true);
        });
    }
}