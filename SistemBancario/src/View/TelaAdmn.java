package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaAdmn extends JFrame {

    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel contentPanel;
    private JLabel titleLabel;
    private JButton btnGerenciarGestores;
    private JButton btnGerenciarFuncionarios;
    private JButton btnRelatorios;
    private JButton btnSair;

    public TelaAdmn() {
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
        
        JPanel mainFormPanel = new JPanel(new BorderLayout());
        mainFormPanel.setBackground(Color.WHITE);
        mainFormPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // Título
        JLabel titulo = new JLabel("CADASTRO DE GESTOR", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        mainFormPanel.add(titulo, BorderLayout.NORTH);

        // Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Campos
        adicionarCampoFormulario(formPanel, gbc, "Nome:", 0);
        adicionarCampoFormulario(formPanel, gbc, "Idade:", 1);
        adicionarCampoFormulario(formPanel, gbc, "ID:", 2);
        adicionarCampoFormulario(formPanel, gbc, "Usuário:", 3);
        adicionarCampoSenhaFormulario(formPanel, gbc, "Senha:", 4);

        // Botão Salvar
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSalvar = criarBotaoAcao("💾 SALVAR GESTOR", new Color(0, 100, 0));
        formPanel.add(btnSalvar, gbc);

        mainFormPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(mainFormPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void mostrarFormFuncionarios() {
        contentPanel.removeAll();
        
        JPanel mainFormPanel = new JPanel(new BorderLayout());
        mainFormPanel.setBackground(Color.WHITE);
        mainFormPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // Menos padding

        // Título
        JLabel titulo = new JLabel("CADASTRO DE FUNCIONÁRIO", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Menos espaço
        mainFormPanel.add(titulo, BorderLayout.NORTH);

        // Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 6, 10); // Menos espaçamento

        // Campos - mais compactos
        adicionarCampoFormulario(formPanel, gbc, "Nome:", 0);
        adicionarCampoFormulario(formPanel, gbc, "Idade:", 1);
        adicionarCampoFormulario(formPanel, gbc, "ID:", 2);
        
        // Campo Tipo
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel lblTipo = criarLabelFormulario("Tipo:");
        formPanel.add(lblTipo, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 1.0;
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Selecione...", "Caixa", "Atendimento"});
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTipo.setPreferredSize(new Dimension(320, 38));
        formPanel.add(cmbTipo, gbc);
        
        adicionarCampoFormulario(formPanel, gbc, "Usuário:", 4);
        adicionarCampoSenhaFormulario(formPanel, gbc, "Senha:", 5);

        // Botão Salvar bem visível
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 5, 10); // Mais espaço acima
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSalvar = criarBotaoAcao("💾 SALVAR FUNCIONÁRIO", new Color(0, 100, 0));
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
        relatorioPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        
        JLabel titulo = new JLabel("RELATÓRIOS DO SISTEMA", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        relatorioPanel.add(titulo, BorderLayout.NORTH);
        
        JLabel status = new JLabel("📈 Funcionalidade em desenvolvimento", JLabel.CENTER);
        status.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        status.setForeground(Color.GRAY);
        relatorioPanel.add(status, BorderLayout.CENTER);
        
        contentPanel.add(relatorioPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // MÉTODOS AUXILIARES
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

    private void adicionarCampoFormulario(JPanel panel, GridBagConstraints gbc, String label, int linha) {
        gbc.gridx = 0; gbc.gridy = linha;
        gbc.weightx = 0;
        panel.add(criarLabelFormulario(label), gbc);
        
        gbc.gridx = 1; gbc.gridy = linha;
        gbc.weightx = 1.0;
        panel.add(criarCampoTexto(), gbc);
    }

    private void adicionarCampoSenhaFormulario(JPanel panel, GridBagConstraints gbc, String label, int linha) {
        gbc.gridx = 0; gbc.gridy = linha;
        gbc.weightx = 0;
        panel.add(criarLabelFormulario(label), gbc);
        
        gbc.gridx = 1; gbc.gridy = linha;
        gbc.weightx = 1.0;
        panel.add(criarCampoSenha(), gbc);
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
    /*    try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        SwingUtilities.invokeLater(() -> {
            new TelaAdmn().setVisible(true);
        });
    }
}
 