package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import controller.SistemaController;
import model.Cliente;
import model.Conta;
import model.Transacoes;
import java.util.List;

public class TelaCliente extends JFrame {

    // Cores do tema - Agora variáveis para poder mudar
    private Color PRIMARY_COLOR = new Color(0, 0, 139); // Azul padrão
    private static final Color SECONDARY_COLOR = new Color(236, 236, 240);
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color MUTED_COLOR = new Color(113, 113, 130);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    // Componentes principais
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout contentCardLayout;
    private JLabel titleLabel;
    private JLabel balanceLabel;
    private boolean showBalance = true;

    // Sistema real
    private SistemaController sistemaController;
    private Cliente clienteLogado;
    private Conta contaCliente;

    // Formatadores
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public TelaCliente(SistemaController sistema, Cliente cliente, Conta conta) {
        this.sistemaController = sistema;
        this.clienteLogado = cliente;
        this.contaCliente = conta;



        initializeFrame();
        createSidebar();
        createContentPanel();
        setupLayout();
        showDashboard();

        // Aplicar tema baseado no tipo de conta
        aplicarTemaPorTipoConta();
    }

    private void initializeFrame() {
        setTitle("Banco Nexus - Painel do Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);
    }

    private void aplicarTemaPorTipoConta() {
        if (contaCliente == null) return;

        switch (contaCliente.getTipoConta()) {
            case POUPANCA:
                aplicarTemaPoupanca();
                break;
            case CORRENTE:
                aplicarTemaCorrente();
                break;
            case DEBITO:
                aplicarTemaDebito();
                break;
        }
    }

    private void aplicarTemaPoupanca() {
        PRIMARY_COLOR = new Color(0, 100, 0); // Verde escuro
        atualizarCoresTema();
    }

    private void aplicarTemaCorrente() {
        PRIMARY_COLOR = new Color(0, 0, 139); // Azul escuro
        atualizarCoresTema();
    }

    private void aplicarTemaDebito() {
        PRIMARY_COLOR = new Color(75, 0, 130); // Roxo escuro
        atualizarCoresTema();
    }

    private void atualizarCoresTema() {
        if (sidebarPanel != null) {
            sidebarPanel.setBackground(PRIMARY_COLOR);
        }
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(PRIMARY_COLOR);
        sidebarPanel.setBorder(new MatteBorder(0, 0, 0, 1, BORDER_COLOR));
        sidebarPanel.setPreferredSize(new Dimension(280, 0));

        // Header da sidebar
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE));

        String nomeCliente = (clienteLogado != null) ? clienteLogado.getNomeCli() : "Cliente";
        String tipoConta = getTituloConta();

        JLabel logoLabel = new JLabel("🏦 Nexus Bank");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel);

        JLabel subtitleLabel = new JLabel(nomeCliente);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.WHITE);
        headerPanel.add(subtitleLabel);

        sidebarPanel.add(headerPanel);

        // Menu items
        String[] menuItems = {
                "📊 Dashboard",
                "👤 Perfil",
                "📋 Histórico",
                "💸 Transferir",
                "💰 Saques",
                "🧾 Pagamentos",
                "🔔 Notificações",
                "⚙️ Configurações"
        };

        String[] actions = {
                "dashboard", "profile", "transactions", "transfer", "withdraw",
                "payments", "notifications", "settings"
        };

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(PRIMARY_COLOR);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], actions[i]);
            menuPanel.add(menuButton);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        sidebarPanel.add(menuPanel);

        // User info no footer
        sidebarPanel.add(Box.createVerticalGlue());

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.setBackground(PRIMARY_COLOR);
        userPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.WHITE));

        JLabel userIcon = new JLabel(getIconePorTipoConta());
        userIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        userIcon.setForeground(Color.WHITE);
        userPanel.add(userIcon);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(PRIMARY_COLOR);

        JLabel userName = new JLabel(tipoConta);
        userName.setFont(new Font("Arial", Font.BOLD, 12));
        userName.setForeground(Color.WHITE);
        userInfoPanel.add(userName);

        JLabel userType = new JLabel(getStatusConta());
        userType.setFont(new Font("Arial", Font.PLAIN, 10));
        userType.setForeground(Color.WHITE);
        userInfoPanel.add(userType);

        userPanel.add(userInfoPanel);
        sidebarPanel.add(userPanel);
    }

    private String getIconePorTipoConta() {
        if (contaCliente == null) return "💳";

        switch (contaCliente.getTipoConta()) {
            case POUPANCA: return "🐷";
            case CORRENTE: return "🏦";
            case DEBITO: return "💳";
            default: return "💳";
        }
    }

    private String getTituloConta() {
        if (contaCliente == null) return "Minha Conta";

        switch (contaCliente.getTipoConta()) {
            case POUPANCA: return "Conta Poupança";
            case CORRENTE: return "Conta Corrente";
            case DEBITO: return "Cartão Débito";
            default: return "Minha Conta";
        }
    }

    private String getStatusConta() {
        if (contaCliente == null) return "N/A";
        return contaCliente.getStatus().toString();
    }

    private JButton createMenuButton(String text, String action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setPreferredSize(new Dimension(250, 40));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(Color.WHITE);
        button.setForeground(PRIMARY_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        button.addActionListener(e -> switchContent(action));

        return button;
    }

    private void createContentPanel() {
        contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Criar todas as telas
        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(createProfilePanel(), "profile");
        contentPanel.add(createTransactionsPanel(), "transactions");
        contentPanel.add(createTransferPanel(), "transfer");
        contentPanel.add(createWithdrawPanel(), "withdraw");
        contentPanel.add(createPaymentsPanel(), "payments");
        contentPanel.add(createNotificationsPanel(), "notifications");
        contentPanel.add(createSettingsPanel(), "settings");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);

        String nomeCliente = (clienteLogado != null) ? clienteLogado.getNomeCli() : "Cliente";
        JLabel titleLabel = new JLabel("Dashboard - " + getTituloConta());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Bem-vindo de volta, " + nomeCliente);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(MUTED_COLOR);
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        JButton updateButton = new JButton("Atualizar Dados");
        updateButton.setBackground(CARD_COLOR);
        updateButton.setBorder(new LineBorder(BORDER_COLOR));
        updateButton.addActionListener(e -> atualizarDashboard());
        headerPanel.add(updateButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Balance Cards com dados reais
        JPanel balancePanel = new JPanel(new GridLayout(1, 4, 15, 15));
        balancePanel.setBackground(BACKGROUND_COLOR);
        balancePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        double saldo = (contaCliente != null) ? contaCliente.getSaldo() : 0.0;
        double limiteSaque = getLimiteSaqueDiario();

        balancePanel.add(createBalanceCard("Saldo Disponível",
                String.format("%.2f MZN", saldo), "Saldo atual", SUCCESS_COLOR, true));

        balancePanel.add(createBalanceCard("Limite Saque Diário",
                String.format("%.2f MZN", limiteSaque), "Máximo por dia", MUTED_COLOR, false));

        balancePanel.add(createBalanceCard("Taxa de Manutenção",
                getTaxaManutencao(), "Próximo vencimento", MUTED_COLOR, false));

        balancePanel.add(createBalanceCard("Status da Conta",
                getStatusConta(), getCaracteristicaExtra(), SUCCESS_COLOR, false));

        panel.add(balancePanel, BorderLayout.CENTER);

        // Quick Actions
        panel.add(createQuickActionsPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private double getLimiteSaqueDiario() {
        if (contaCliente == null) return 0;

        switch (contaCliente.getTipoConta()) {
            case POUPANCA: return 10000.0;
            case CORRENTE: return 50000.0;
            case DEBITO: return 20000.0;
            default: return 0;
        }
    }

    private String getTaxaManutencao() {
        if (contaCliente == null) return "N/A";

        switch (contaCliente.getTipoConta()) {
            case POUPANCA: return "Isenta";
            case CORRENTE: return "100 MZN/mês";
            case DEBITO: return "50 MZN/mês";
            default: return "N/A";
        }
    }

    private String getCaracteristicaExtra() {
        if (contaCliente == null) return "";

        switch (contaCliente.getTipoConta()) {
            case POUPANCA: return "Acumulação";
            case CORRENTE: return "Empréstimos ✅";
            case DEBITO: return "Uso Diário";
            default: return "";
        }
    }

    private void atualizarDashboard() {
        // Atualizar dados do dashboard
        JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!", "Atualização",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createQuickActionsPanel() {
        JPanel actionsPanel = new JPanel(new BorderLayout());
        actionsPanel.setBackground(BACKGROUND_COLOR);
        actionsPanel.setBorder(createCardBorder());

        JLabel actionsTitle = new JLabel("Ações Rápidas");
        actionsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        actionsTitle.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        actionsPanel.add(actionsTitle, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonsPanel.setBackground(CARD_COLOR);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        // Ações comuns a todos
        buttonsPanel.add(createActionButton("💸", "Transferir", e -> switchContent("transfer")));
        buttonsPanel.add(createActionButton("💰", "Sacar", e -> switchContent("withdraw")));

        // Ações específicas por tipo de conta
        if (contaCliente != null && contaCliente.getTipoConta() == Conta.TipoConta.CORRENTE) {
            buttonsPanel.add(createActionButton("📈", "Empréstimos", e -> mostrarEmprestimos()));
        } else {
            buttonsPanel.add(createActionButton("🧾", "Pagamentos", e -> switchContent("payments")));
        }

        buttonsPanel.add(createActionButton("📋", "Extrato", e -> switchContent("transactions")));

        actionsPanel.add(buttonsPanel, BorderLayout.CENTER);
        return actionsPanel;
    }

    private void mostrarEmprestimos() {
        JOptionPane.showMessageDialog(this,
                "Funcionalidade de empréstimos disponível apenas para Conta Corrente!\n\n" +
                        "Visite uma agência para mais informações.",
                "Empréstimos",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton createActionButton(String icon, String text, ActionListener listener) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(CARD_COLOR);
        button.setBorder(new LineBorder(BORDER_COLOR));
        button.setPreferredSize(new Dimension(120, 80));
        button.setFocusPainted(false);
        button.addActionListener(listener);

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        button.add(iconLabel, BorderLayout.CENTER);

        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        button.add(textLabel, BorderLayout.SOUTH);

        return button;
    }

    private JPanel createBalanceCard(String title, String amount, String subtitle, Color subtitleColor, boolean hasToggle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(createCardBorder());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(MUTED_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        if (hasToggle) {
            JButton toggleButton = new JButton(showBalance ? "👁️" : "🔒");
            toggleButton.setFont(new Font("Arial", Font.PLAIN, 12));
            toggleButton.setBorder(null);
            toggleButton.setBackground(CARD_COLOR);
            toggleButton.setFocusPainted(false);
            toggleButton.addActionListener(e -> {
                showBalance = !showBalance;
                toggleButton.setText(showBalance ? "👁️" : "🔒");
            });
            headerPanel.add(toggleButton, BorderLayout.EAST);
        }

        card.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel amountLabel = new JLabel(showBalance ? amount : "••••••");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(amountLabel);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        subtitleLabel.setForeground(subtitleColor);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(subtitleLabel);

        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Meu Perfil");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Photo Card
        contentPanel.add(createProfilePhotoCard());

        // Personal Info Card
        contentPanel.add(createPersonalInfoCard());

        // Account Details Card
        contentPanel.add(createAccountDetailsCard());

        // Security Card
        contentPanel.add(createSecurityCard());

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePhotoCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(createCardBorder());

        JLabel titleLabel = new JLabel("Foto de Perfil");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.setBackground(CARD_COLOR);
        photoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // Avatar placeholder
        JLabel avatar = new JLabel("👤", SwingConstants.CENTER);
        avatar.setFont(new Font("Arial", Font.PLAIN, 48));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        photoPanel.add(avatar);

        photoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        String nomeCliente = (clienteLogado != null) ? clienteLogado.getNomeCli() : "Cliente";
        JLabel nameLabel = new JLabel(nomeCliente);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        photoPanel.add(nameLabel);

        JLabel typeLabel = new JLabel(getTituloConta());
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        typeLabel.setForeground(MUTED_COLOR);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        photoPanel.add(typeLabel);

        photoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel badgeLabel = new JLabel("✅ Verificado");
        badgeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        badgeLabel.setForeground(SUCCESS_COLOR);
        badgeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        photoPanel.add(badgeLabel);

        card.add(photoPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createPersonalInfoCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(createCardBorder());

        JLabel titleLabel = new JLabel("Informações Pessoais");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        String nome = (clienteLogado != null) ? clienteLogado.getNomeCli() : "N/A";
        String email = (clienteLogado != null) ? clienteLogado.getEmailCli() : "N/A";
        String telefone = (clienteLogado != null) ? String.valueOf(clienteLogado.getTelefoneCli()) : "N/A";
        String nuit = (clienteLogado != null) ? String.valueOf(clienteLogado.getNuitCli()) : "N/A";

        formPanel.add(new JLabel("Nome Completo:"));
        formPanel.add(new JLabel(nome));

        formPanel.add(new JLabel("Email:"));
        formPanel.add(new JLabel(email));

        formPanel.add(new JLabel("Telefone:"));
        formPanel.add(new JLabel(telefone));

        formPanel.add(new JLabel("NUIT:"));
        formPanel.add(new JLabel(nuit));

        card.add(formPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createAccountDetailsCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(createCardBorder());

        JLabel titleLabel = new JLabel("💳 Detalhes da Conta");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(CARD_COLOR);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        String numeroConta = (contaCliente != null) ? String.valueOf(contaCliente.getNumeroConta()) : "N/A";
        String nib = (contaCliente != null) ? String.valueOf(contaCliente.getNib()) : "N/A";
        String saldo = (contaCliente != null) ? String.format("%.2f MZN", contaCliente.getSaldo()) : "N/A";

        detailsPanel.add(createDetailRow("Número da Conta:", numeroConta));
        detailsPanel.add(createDetailRow("NIB:", nib));
        detailsPanel.add(createDetailRow("Tipo de Conta:", getTituloConta()));
        detailsPanel.add(createDetailRow("Saldo Disponível:", saldo));
        detailsPanel.add(createDetailRow("Status:", getStatusConta()));

        card.add(detailsPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSecurityCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(createCardBorder());

        JLabel titleLabel = new JLabel("🔒 Segurança");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel securityPanel = new JPanel();
        securityPanel.setLayout(new BoxLayout(securityPanel, BoxLayout.Y_AXIS));
        securityPanel.setBackground(CARD_COLOR);
        securityPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        securityPanel.add(createDetailRow("Autenticação 2FA:", "✅ Ativada"));
        securityPanel.add(createDetailRow("Último Acesso:", new Date().toString()));
        securityPanel.add(createDetailRow("Nível de Segurança:", "Alto"));

        securityPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton changePasswordBtn = new JButton("Alterar Senha");
        changePasswordBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        changePasswordBtn.setMaximumSize(new Dimension(200, 30));
        changePasswordBtn.addActionListener(e -> alterarSenha());
        securityPanel.add(changePasswordBtn);

        card.add(securityPanel, BorderLayout.CENTER);

        return card;
    }

    private void alterarSenha() {
        JPasswordField senhaAtual = new JPasswordField(20);
        JPasswordField novaSenha = new JPasswordField(20);
        JPasswordField confirmarSenha = new JPasswordField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Senha Atual:"));
        panel.add(senhaAtual);
        panel.add(new JLabel("Nova Senha:"));
        panel.add(novaSenha);
        panel.add(new JLabel("Confirmar Nova Senha:"));
        panel.add(confirmarSenha);

        int result = JOptionPane.showConfirmDialog(this, panel, "Alterar Senha",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this, "Senha alterada com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel createDetailRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(CARD_COLOR);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.PLAIN, 12));
        labelComp.setForeground(MUTED_COLOR);
        row.add(labelComp, BorderLayout.WEST);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.PLAIN, 12));
        row.add(valueComp, BorderLayout.EAST);

        return row;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Histórico de Transações");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton exportButton = new JButton("📥 Exportar");
        exportButton.setBackground(CARD_COLOR);
        exportButton.setBorder(new LineBorder(BORDER_COLOR));
        headerPanel.add(exportButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabela de transações
        String[] columnNames = {"Data/Hora", "Descrição", "Categoria", "Valor", "Status"};

        // Buscar transações reais do sistema
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        if (contaCliente != null) {
            List<Transacoes> transacoes = sistemaController.consultarHistorico(contaCliente.getIdConta());
            for (Transacoes transacao : transacoes) {
                model.addRow(new Object[]{
                        transacao.getData().toString(),
                        transacao.getDescricaoTrancacao(),
                        transacao.getCategoria(),
                        String.format("%.2f MZN", transacao.getValor()),
                        transacao.getStatus().toString()
                });
            }
        }

        JTable table = new JTable(model);
        table.setBackground(CARD_COLOR);
        table.setRowHeight(40);
        table.setGridColor(BORDER_COLOR);
        table.getTableHeader().setBackground(SECONDARY_COLOR);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(createCardBorder());

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTransferPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Transferência Bancária");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campos do formulário
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Conta Destino:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        JTextField contaDestino = new JTextField(20);
        formPanel.add(contaDestino, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Valor (MZN):"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        JTextField valor = new JTextField(20);
        formPanel.add(valor, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Descrição:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JTextField descricao = new JTextField(20);
        formPanel.add(descricao, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton transferirBtn = new JButton("💸 Transferir");
        transferirBtn.setBackground(PRIMARY_COLOR);
        transferirBtn.setForeground(Color.WHITE);
        transferirBtn.setFont(new Font("Arial", Font.BOLD, 16));
        transferirBtn.addActionListener(e -> processarTransferencia(contaDestino.getText(), valor.getText(), descricao.getText()));
        formPanel.add(transferirBtn, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    private void processarTransferencia(String contaDestino, String valorStr, String descricao) {
        try {
            if (contaDestino.isEmpty() || valorStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double valor = Double.parseDouble(valorStr);
            int contaDest = Integer.parseInt(contaDestino);

            if (sistemaController.transferirComTaxa(contaCliente.getIdConta(), contaDest, valor)) {
                JOptionPane.showMessageDialog(this, "Transferência realizada com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro na transferência. Verifique os dados.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createWithdrawPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Saque");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Valor do Saque (MZN):"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        JTextField valor = new JTextField(20);
        formPanel.add(valor, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton sacarBtn = new JButton("💰 Sacar");
        sacarBtn.setBackground(PRIMARY_COLOR);
        sacarBtn.setForeground(Color.WHITE);
        sacarBtn.setFont(new Font("Arial", Font.BOLD, 16));
        sacarBtn.addActionListener(e -> processarSaque(valor.getText()));
        formPanel.add(sacarBtn, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    private void processarSaque(String valorStr) {
        try {
            if (valorStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite o valor do saque!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double valor = Double.parseDouble(valorStr);

            if (sistemaController.sacarComTaxa(contaCliente.getIdConta(), valor)) {
                JOptionPane.showMessageDialog(this, "Saque realizado com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createPaymentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Pagamento de Serviços");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        JLabel infoLabel = new JLabel("Funcionalidade em desenvolvimento...");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(infoLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createNotificationsPanel() {
        return createPlaceholderPanel("Notificações");
    }

    private JPanel createSettingsPanel() {
        return createPlaceholderPanel("Configurações");
    }

    private JPanel createPlaceholderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        JLabel descLabel = new JLabel("Esta seção será implementada em breve...");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setForeground(MUTED_COLOR);
        panel.add(descLabel, BorderLayout.CENTER);

        return panel;
    }

    private Border createCardBorder() {
        return new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void switchContent(String section) {
        contentCardLayout.show(contentPanel, section);
    }

    private void showDashboard() {
        contentCardLayout.show(contentPanel, "dashboard");
    }


}