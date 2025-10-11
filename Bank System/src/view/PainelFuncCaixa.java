package view;

import controller.SistemaController;
import model.Transacoes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelFuncCaixa extends JFrame {

    private JPanel menuLateral, areaPrincipal, header;
    private CardLayout cardLayout;
    private SistemaController sistema;

    // Campos de depósito e saque
    private JTextField txtIdContaDeposito, txtValorDeposito;
    private JTextField txtIdContaSaque, txtNumContaSaque, txtValorSaque;

    // Tabelas e modelos
    private JTable tabelaDepositos;
    private DefaultTableModel modeloDepositos;
    private JTable tabelaSaques;
    private DefaultTableModel modeloSaques;

    // Labels do dashboard (para atualização)
    private JLabel lblSaldoVal, lblTotalDepositosVal, lblTotalSaquesVal;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PainelFuncCaixa(SistemaController sistema, String funcionario, String cargo, String banco) {
        this.sistema = sistema;

        setTitle("Sistema Bancário - " + banco);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ============ CABEÇALHO ============
        header = new JPanel();
        header.setBackground(new Color(0, 51, 102));
        header.setPreferredSize(new Dimension(1000, 60));
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 18));

        JLabel lblBanco = new JLabel(banco.toUpperCase());
        lblBanco.setForeground(Color.WHITE);
        lblBanco.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(lblBanco);
        add(header, BorderLayout.NORTH);

        // ============ MENU LATERAL ============
        menuLateral = new JPanel();
        menuLateral.setBackground(new Color(0, 102, 204));
        menuLateral.setPreferredSize(new Dimension(240, 0));
        menuLateral.setLayout(new BorderLayout());

        JPanel infoFuncionario = new JPanel(new GridLayout(3, 1));
        infoFuncionario.setBackground(new Color(0, 102, 204));
        infoFuncionario.setBorder(BorderFactory.createEmptyBorder(18, 14, 18, 14));

        JLabel lblFuncionario = new JLabel("<html><span style='color:white'>Funcionário: " + funcionario + "</span></html>");
        lblFuncionario.setForeground(Color.WHITE);
        lblFuncionario.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblCargo = new JLabel("<html><span style='color:white'>Cargo: " + cargo + "</span></html>");
        lblCargo.setForeground(Color.WHITE);
        lblCargo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        infoFuncionario.add(new JLabel(" ", SwingConstants.CENTER));
        infoFuncionario.add(lblFuncionario);
        infoFuncionario.add(lblCargo);
        menuLateral.add(infoFuncionario, BorderLayout.NORTH);

        // Botões do menu
        JPanel botoesMenu = new JPanel(new GridLayout(6, 1, 8, 8));
        botoesMenu.setBackground(new Color(0, 102, 204));
        botoesMenu.setBorder(BorderFactory.createEmptyBorder(20, 14, 20, 14));

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
        areaPrincipal.setBackground(new Color(235, 242, 248));

        // Painéis
        areaPrincipal.add(criarPainelDashboard(), "Dashboard");
        areaPrincipal.add(criarPainelDepositos(), "Depositos");
        areaPrincipal.add(criarPainelSaques(), "Saques");
        areaPrincipal.add(criarPainelRelatorios(), "Relatorios");
        add(areaPrincipal, BorderLayout.CENTER);

        // Eventos dos botões
        btnDashboard.addActionListener(e -> {
            atualizarDashboard();
            cardLayout.show(areaPrincipal, "Dashboard");
        });
        btnDepositos.addActionListener(e -> {
            atualizarTabelaDepositos();
            cardLayout.show(areaPrincipal, "Depositos");
        });
        btnSaques.addActionListener(e -> {
            atualizarTabelaSaques();
            cardLayout.show(areaPrincipal, "Saques");
        });
        btnRelatorios.addActionListener(e -> {
            atualizarDashboard();
            cardLayout.show(areaPrincipal, "Relatorios");
        });
        btnSair.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) dispose();
        });

        cardLayout.show(areaPrincipal, "Dashboard");
    }

    // ===================== DASHBOARD =====================
    private JPanel criarPainelDashboard() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(235, 242, 248));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("📊 Dashboard do Caixa", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 51, 102));

        JPanel dados = new JPanel(new GridLayout(1, 3, 20, 20));
        dados.setBackground(new Color(235, 242, 248));
        dados.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel p1 = criarCartaoResumo("Saldo total", "0.00 MZN");
        JPanel p2 = criarCartaoResumo("Total Depósitos", "0.00 MZN");
        JPanel p3 = criarCartaoResumo("Total Saques", "0.00 MZN");

        lblSaldoVal = (JLabel) p1.getClientProperty("valueLabel");
        lblTotalDepositosVal = (JLabel) p2.getClientProperty("valueLabel");
        lblTotalSaquesVal = (JLabel) p3.getClientProperty("valueLabel");

        dados.add(p1);
        dados.add(p2);
        dados.add(p3);

        painel.add(lblTitulo, BorderLayout.NORTH);
        painel.add(dados, BorderLayout.CENTER);

        atualizarDashboard();
        return painel;
    }

    private JPanel criarCartaoResumo(String titulo, String valorInicial) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        JLabel t = new JLabel(titulo, SwingConstants.LEFT);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setForeground(new Color(0, 51, 102));
        JLabel v = new JLabel(valorInicial, SwingConstants.CENTER);
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));
        v.setForeground(new Color(0, 102, 204));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        card.putClientProperty("valueLabel", v);
        return card;
    }

    private void atualizarDashboard() {
        lblSaldoVal.setText(String.format("%.2f MZN", sistema.calcularSaldoTotal()));
        lblTotalDepositosVal.setText(String.format("%.2f MZN", sistema.getTotalDepositos()));
        lblTotalSaquesVal.setText(String.format("%.2f MZN", sistema.getTotalSaques()));
    }

    // ===================== DEPÓSITOS =====================
   private JPanel criarPainelDepositos() {
    JPanel painel = new JPanel(new BorderLayout(12, 12));
    painel.setBackground(new Color(235, 242, 248));
    painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

    // ========== TOPO ==========
    JPanel topo = new JPanel(new GridBagLayout());
    topo.setBackground(new Color(235, 242, 248));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel lblTitulo = new JLabel("💰 Registrar Depósito");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblTitulo.setForeground(new Color(0, 51, 102));

    txtIdContaDeposito = new JTextField(12);
    txtValorDeposito = new JTextField(12);
    JTextField txtReferencia = new JTextField(12);
    JTextField txtEntidade = new JTextField(12);

    JButton btnDepositar = new JButton("Confirmar Depósito");
    JButton btnListar = new JButton("Listar Depósitos");
    JButton btnLimpar = new JButton("Limpar Campos");

    estilizarBotaoAcao(btnDepositar);
    estilizarBotaoSecundario(btnListar);
    estilizarBotaoSecundario(btnLimpar);

    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    topo.add(lblTitulo, gbc);

    gbc.gridwidth = 1; gbc.gridy++;
    topo.add(new JLabel("ID da Conta:"), gbc);
    gbc.gridx = 1;
    topo.add(txtIdContaDeposito, gbc);

    gbc.gridx = 0; gbc.gridy++;
    topo.add(new JLabel("Valor (MZN):"), gbc);
    gbc.gridx = 1;
    topo.add(txtValorDeposito, gbc);

    gbc.gridx = 0; gbc.gridy++;
    topo.add(new JLabel("Referência:"), gbc);
    gbc.gridx = 1;
    topo.add(txtReferencia, gbc);

    gbc.gridx = 0; gbc.gridy++;
    topo.add(new JLabel("Entidade:"), gbc);
    gbc.gridx = 1;
    topo.add(txtEntidade, gbc);

    gbc.gridx = 0; gbc.gridy++;
    topo.add(btnDepositar, gbc);
    gbc.gridx = 1;
    topo.add(btnLimpar, gbc);
    gbc.gridx = 0; gbc.gridy++;
    topo.add(btnListar, gbc);

    painel.add(topo, BorderLayout.NORTH);

    // ========== TABELA ==========
    String[] col = {"ID Transação", "ID Conta", "Valor", "Data", "Referência", "Entidade", "Descrição"};
    modeloDepositos = new DefaultTableModel(col, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    tabelaDepositos = new JTable(modeloDepositos);
    tabelaDepositos.setRowHeight(24);
    tabelaDepositos.getTableHeader().setReorderingAllowed(false);
    JScrollPane scroll = new JScrollPane(tabelaDepositos);
    scroll.setPreferredSize(new Dimension(700, 240));
    painel.add(scroll, BorderLayout.CENTER);

    // ========== RODAPÉ ==========
    JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
    rodape.setBackground(new Color(235, 242, 248));
    JButton btnAtualizar = new JButton("Actualizar Tabela");
    estilizarBotaoSecundario(btnAtualizar);
    rodape.add(btnAtualizar);
    painel.add(rodape, BorderLayout.SOUTH);

    // ========== AÇÕES ==========
    btnDepositar.addActionListener(e -> {
        try {
            int idConta = Integer.parseInt(txtIdContaDeposito.getText().trim());
            double valor = Double.parseDouble(txtValorDeposito.getText().trim());
            String referencia = txtReferencia.getText().trim();
            String entidade = txtEntidade.getText().trim();

            if (valor <= 0) {
                JOptionPane.showMessageDialog(this, "O valor deve ser maior que zero.");
                return;
            }

            // Chama método atualizado no SistemaController
            Transacoes transacao = sistema.registrarTransacao(
                idConta, "Depósito", valor, referencia, entidade
            );

            if (transacao != null) {
                JOptionPane.showMessageDialog(this, "Depósito realizado com sucesso!");
                atualizarTabelaDepositos();
                atualizarDashboard();
                txtIdContaDeposito.setText("");
                txtValorDeposito.setText("");
                txtReferencia.setText("");
                txtEntidade.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Erro: conta não encontrada ou dados inválidos.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Dados inválidos! Verifica o ID da conta e o valor.");
        }
    });

    btnListar.addActionListener(e -> atualizarTabelaDepositos());
    btnLimpar.addActionListener(e -> {
        txtIdContaDeposito.setText("");
        txtValorDeposito.setText("");
        txtReferencia.setText("");
        txtEntidade.setText("");
    });
    btnAtualizar.addActionListener(e -> atualizarTabelaDepositos());

    return painel;
}


    private void atualizarTabelaDepositos() {
        modeloDepositos.setRowCount(0);
        List<Transacoes> lista = sistema.listarDepositos();
        for (Transacoes t : lista) {
            String data = t.getData() != null ? t.getData().format(dtf) : "";
            modeloDepositos.addRow(new Object[]{
                    t.getId(),          // id transacao
                    t.getConta() != null ? t.getConta().getIdConta() : t.getIdCliente(),
                    String.format("%.2f", t.getValor()),
                    data,
                    t.getCategoria()
            });
        }
    }

    // ===================== SAQUES =====================
    private JPanel criarPainelSaques() {
        JPanel painel = new JPanel(new BorderLayout(12, 12));
        painel.setBackground(new Color(235, 242, 248));
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Top - formulário e ações
        JPanel topo = new JPanel(new GridBagLayout());
        topo.setBackground(new Color(235, 242, 248));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("💸 Registrar Saque");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 51, 102));

        txtIdContaSaque = new JTextField(12);
        txtNumContaSaque = new JTextField(12);
        txtValorSaque = new JTextField(12);
        JButton btnSacar = new JButton("Confirmar Saque");
        JButton btnListar = new JButton("Listar Saques");
        JButton btnLimpar = new JButton("Limpar Campos");

        estilizarBotaoAcao(btnSacar);
        estilizarBotaoSecundario(btnListar);
        estilizarBotaoSecundario(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        topo.add(lblTitulo, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        topo.add(new JLabel("ID da Conta:"), gbc);
        gbc.gridx = 1;
        topo.add(txtIdContaSaque, gbc);
        gbc.gridx = 0; gbc.gridy++;
        topo.add(new JLabel("Número da Conta:"), gbc);
        gbc.gridx = 1;
        topo.add(txtNumContaSaque, gbc);
        gbc.gridx = 0; gbc.gridy++;
        topo.add(new JLabel("Valor (MZN):"), gbc);
        gbc.gridx = 1;
        topo.add(txtValorSaque, gbc);
        gbc.gridx = 0; gbc.gridy++;
        topo.add(btnSacar, gbc);
        gbc.gridx = 1;
        topo.add(btnLimpar, gbc);
        gbc.gridx = 0; gbc.gridy++;
        topo.add(btnListar, gbc);

        painel.add(topo, BorderLayout.NORTH);

        // Centro - tabela de saques
        String[] col = {"ID Transação", "ID Conta", "Valor", "Data", "Descrição"};
        modeloSaques = new DefaultTableModel(col, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaSaques = new JTable(modeloSaques);
        tabelaSaques.setRowHeight(24);
        tabelaSaques.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(tabelaSaques);
        scroll.setPreferredSize(new Dimension(600, 240));
        painel.add(scroll, BorderLayout.CENTER);

        // Rodapé - ações rápidas
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        rodape.setBackground(new Color(235, 242, 248));
        JButton btnAtualizar = new JButton("Actualizar Tabela");
        estilizarBotaoSecundario(btnAtualizar);
        rodape.add(btnAtualizar);
        painel.add(rodape, BorderLayout.SOUTH);

        // Ações
        btnSacar.addActionListener(e -> {
            try {
                int idConta = Integer.parseInt(txtIdContaSaque.getText().trim());
                int numeroConta = Integer.parseInt(txtNumContaSaque.getText().trim());
                double valor = Double.parseDouble(txtValorSaque.getText().trim());
                boolean ok = sistema.registrarLevantamento(idConta, numeroConta, valor);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Levantamento realizado com sucesso!");
                    atualizarTabelaSaques();
                    atualizarDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: saldo insuficiente ou conta inválida.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos! Verifica o ID da conta, nº conta e o valor.");
            }
        });

        btnListar.addActionListener(e -> atualizarTabelaSaques());
        btnLimpar.addActionListener(e -> {
            txtIdContaSaque.setText("");
            txtNumContaSaque.setText("");
            txtValorSaque.setText("");
        });
        btnAtualizar.addActionListener(e -> atualizarTabelaSaques());

        return painel;
    }

    private void atualizarTabelaSaques() {
        modeloSaques.setRowCount(0);
        List<Transacoes> lista = sistema.listarSaques();
        for (Transacoes t : lista) {
            String data = t.getData() != null ? t.getData().format(dtf) : "";
            modeloSaques.addRow(new Object[]{
                    t.getId(),
                    t.getConta() != null ? t.getConta().getIdConta() : t.getIdCliente(),
                    String.format("%.2f", t.getValor()),
                    data,
                    t.getCategoria()
            });
        }
    }

    // ===================== RELATÓRIOS =====================
    private JPanel criarPainelRelatorios() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(235, 242, 248));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("📊 Relatórios do Caixa", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 51, 102));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText(sistema.gerarRelatorioCaixa());

        JButton btnGerar = new JButton("Actualizar Relatório");
        estilizarBotaoAcao(btnGerar);
        btnGerar.addActionListener(e -> area.setText(sistema.gerarRelatorioCaixa()));

        painel.add(lblTitulo, BorderLayout.NORTH);
        painel.add(new JScrollPane(area), BorderLayout.CENTER);
        JPanel rod = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rod.setBackground(new Color(235, 242, 248));
        rod.add(btnGerar);
        painel.add(rod, BorderLayout.SOUTH);

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
        btn.setPreferredSize(new Dimension(180, 36));
    }

    private void estilizarBotaoSecundario(JButton btn) {
        btn.setBackground(new Color(220, 230, 240));
        btn.setForeground(new Color(0, 51, 102));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 32));
    }

    // ========== MAIN DE TESTE ==========
    public static void main(String[] args) {
        SistemaController sistema = new SistemaController();
        // opcional: cria contas/tansacoes para teste
        SwingUtilities.invokeLater(() ->
                new PainelFuncCaixa(sistema, "Frederico Madabula", "Caixa", "Banco Nexus").setVisible(true)
        );
    }
}
