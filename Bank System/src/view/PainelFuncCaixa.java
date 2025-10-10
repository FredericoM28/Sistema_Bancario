package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import controller.SistemaController;

public class PainelFuncCaixa extends JFrame {

    private JPanel menuLateral, areaPrincipal, header;
    private CardLayout cardLayout;
    private SistemaController sistema;

    // Campos de depósito e saque
    private JTextField txtIdContaDeposito, txtValorDeposito;
    private JTextField txtIdContaSaque, txtNumContaSaque, txtValorSaque;

    public PainelFuncCaixa(SistemaController sistema, String funcionario, String cargo, String banco) {
        this.sistema = sistema;

        setTitle("Sistema Bancário - " + banco);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ============ CABEÇALHO ============
        header = new JPanel();
        header.setBackground(new Color(0, 51, 102));
        header.setPreferredSize(new Dimension(1000, 50));
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        JLabel lblBanco = new JLabel(banco.toUpperCase());
        lblBanco.setForeground(Color.WHITE);
        lblBanco.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(lblBanco);
        add(header, BorderLayout.NORTH);

        // ============ MENU LATERAL ============
        menuLateral = new JPanel();
        menuLateral.setBackground(new Color(0, 102, 204));
        menuLateral.setPreferredSize(new Dimension(220, 0));
        menuLateral.setLayout(new BorderLayout());

        JPanel infoFuncionario = new JPanel(new GridLayout(3, 1));
        infoFuncionario.setBackground(new Color(0, 102, 204));
        infoFuncionario.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel lblFuncionario = new JLabel("Funcionário: " + funcionario);
        lblFuncionario.setForeground(Color.WHITE);
        lblFuncionario.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblCargo = new JLabel("Cargo: " + cargo);
        lblCargo.setForeground(Color.WHITE);
        lblCargo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        infoFuncionario.add(new JLabel("🧑", SwingConstants.CENTER));
        infoFuncionario.add(lblFuncionario);
        infoFuncionario.add(lblCargo);
        menuLateral.add(infoFuncionario, BorderLayout.NORTH);

        // Botões do menu
        JPanel botoesMenu = new JPanel(new GridLayout(6, 1, 5, 5));
        botoesMenu.setBackground(new Color(0, 102, 204));
        botoesMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnDashboard = criarBotaoMenu("🏠 Dashboard");
        JButton btnDepositos = criarBotaoMenu("💰 Depósitos");
        JButton btnSaques = criarBotaoMenu("💸 Saques");
        JButton btnRelatorios = criarBotaoMenu("📊 Relatórios");
        JButton btnSair = criarBotaoMenu("🚪 Sair");

        botoesMenu.add(btnDashboard);
        botoesMenu.add(btnDepositos);
        botoesMenu.add(btnSaques);
        botoesMenu.add(btnRelatorios);
        botoesMenu.add(btnSair);
        menuLateral.add(botoesMenu, BorderLayout.CENTER);
        add(menuLateral, BorderLayout.WEST);

        // ============ ÁREA PRINCIPAL ============
        cardLayout = new CardLayout();
        areaPrincipal = new JPanel(cardLayout);
        areaPrincipal.setBackground(new Color(230, 240, 250));

        // Painéis
        areaPrincipal.add(criarPainelDashboard(), "Dashboard");
        areaPrincipal.add(criarPainelDepositos(), "Depositos");
        areaPrincipal.add(criarPainelSaques(), "Saques");
        areaPrincipal.add(criarPainelRelatorios(), "Relatorios");
        add(areaPrincipal, BorderLayout.CENTER);

        // Eventos dos botões
        btnDashboard.addActionListener(e -> cardLayout.show(areaPrincipal, "Dashboard"));
        btnDepositos.addActionListener(e -> cardLayout.show(areaPrincipal, "Depositos"));
        btnSaques.addActionListener(e -> cardLayout.show(areaPrincipal, "Saques"));
        btnRelatorios.addActionListener(e -> cardLayout.show(areaPrincipal, "Relatorios"));
        btnSair.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        cardLayout.show(areaPrincipal, "Dashboard");
    }

    // ===================== DASHBOARD =====================
    private JPanel criarPainelDashboard() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(230, 240, 250));

        JLabel lblTitulo = new JLabel("📊 Dashboard do Caixa", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 51, 102));

        JPanel dados = new JPanel(new GridLayout(3, 1, 10, 10));
        dados.setBackground(new Color(230, 240, 250));
        dados.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JLabel lblSaldo = new JLabel("💵 Saldo total: " + sistema.calcularSaldoTotal() + " MZN", SwingConstants.CENTER);
        JLabel lblDepositos = new JLabel("💰 Total de Depósitos: " + sistema.getTotalDepositos(), SwingConstants.CENTER);
        JLabel lblSaques = new JLabel("💸 Total de Saques: " + sistema.getTotalSaques(), SwingConstants.CENTER);

        lblSaldo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDepositos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSaques.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        dados.add(lblSaldo);
        dados.add(lblDepositos);
        dados.add(lblSaques);

        painel.add(lblTitulo, BorderLayout.NORTH);
        painel.add(dados, BorderLayout.CENTER);
        return painel;
    }

    // ===================== DEPÓSITOS =====================
    private JPanel criarPainelDepositos() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(230, 240, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("💰 Registrar Depósito");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 51, 102));

        txtIdContaDeposito = new JTextField(15);
        txtValorDeposito = new JTextField(15);
        JButton btnDepositar = new JButton("Confirmar Depósito");

        estilizarBotaoAcao(btnDepositar);

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(lblTitulo, gbc);
        gbc.gridy++;
        painel.add(new JLabel("ID da Conta:"), gbc);
        gbc.gridy++;
        painel.add(txtIdContaDeposito, gbc);
        gbc.gridy++;
        painel.add(new JLabel("Valor (MZN):"), gbc);
        gbc.gridy++;
        painel.add(txtValorDeposito, gbc);
        gbc.gridy++;
        painel.add(btnDepositar, gbc);

        btnDepositar.addActionListener(e -> {
            try {
                int idConta = Integer.parseInt(txtIdContaDeposito.getText());
                double valor = Double.parseDouble(txtValorDeposito.getText());
                boolean ok = sistema.registrarDeposito(idConta, valor);
                if (ok)
                    JOptionPane.showMessageDialog(this, "Depósito realizado com sucesso!");
                else
                    JOptionPane.showMessageDialog(this, "Erro: conta não encontrada ou valor inválido.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos!");
            }
        });

        return painel;
    }

    // ===================== SAQUES =====================
    private JPanel criarPainelSaques() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(230, 240, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("💸 Registrar Saque");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 51, 102));

        txtIdContaSaque = new JTextField(15);
        txtNumContaSaque = new JTextField(15);
        txtValorSaque = new JTextField(15);
        JButton btnSacar = new JButton("Confirmar Saque");

        estilizarBotaoAcao(btnSacar);

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(lblTitulo, gbc);
        gbc.gridy++;
        painel.add(new JLabel("ID da Conta:"), gbc);
        gbc.gridy++;
        painel.add(txtIdContaSaque, gbc);
        gbc.gridy++;
        painel.add(new JLabel("Número da Conta:"), gbc);
        gbc.gridy++;
        painel.add(txtNumContaSaque, gbc);
        gbc.gridy++;
        painel.add(new JLabel("Valor (MZN):"), gbc);
        gbc.gridy++;
        painel.add(txtValorSaque, gbc);
        gbc.gridy++;
        painel.add(btnSacar, gbc);

        btnSacar.addActionListener(e -> {
            try {
                int idConta = Integer.parseInt(txtIdContaSaque.getText());
                int numeroConta = Integer.parseInt(txtNumContaSaque.getText());
                double valor = Double.parseDouble(txtValorSaque.getText());
                boolean ok = sistema.registrarLevantamento(idConta, numeroConta, valor);
                if (ok)
                    JOptionPane.showMessageDialog(this, "Levantamento realizado com sucesso!");
                else
                    JOptionPane.showMessageDialog(this, "Erro: saldo insuficiente ou conta inválida.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos!");
            }
        });

        return painel;
    }

    // ===================== RELATÓRIOS =====================
    private JPanel criarPainelRelatorios() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(230, 240, 250));

        JLabel lblTitulo = new JLabel("📊 Relatórios do Caixa");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 51, 102));

        JButton btnGerar = new JButton("Gerar Relatório");
        estilizarBotaoAcao(btnGerar);

        btnGerar.addActionListener(e -> {
            String relatorio = sistema.gerarRelatorioCaixa();
            JOptionPane.showMessageDialog(this, relatorio, "Relatório", JOptionPane.INFORMATION_MESSAGE);
        });

        painel.add(lblTitulo);
        painel.add(btnGerar);
        return painel;
    }

    // ========== ESTILOS AUXILIARES ==========
    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(0, 76, 153));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }

    private void estilizarBotaoAcao(JButton btn) {
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 35));
    }

    // ========== MAIN DE TESTE ==========
    public static void main(String[] args) {
        SistemaController sistema = new SistemaController();
        SwingUtilities.invokeLater(() ->
                new PainelFuncCaixa(sistema, "Frederico Madabula", "Caixa", "Banco Nexus").setVisible(true)
        );
    }
}
