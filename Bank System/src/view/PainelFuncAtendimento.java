package view;

import controller.SistemaController;
import model.Conta;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PainelFuncAtendimento extends JFrame {
    
    private JPanel menuLateral, painelTopo, painelPrincipal;
    private JLabel lblBanco, lblFuncionario;
    private JButton btnDashboard, btnNovaConta, btnAtualizarDados, btnEncerrarConta, btnCartoes, btnSuporte, btnSair;
    private SistemaController controller;
    private String nomeFuncionario, cargoFuncionario;

    public PainelFuncAtendimento(SistemaController controller, String nomeFuncionario, String cargoFuncionario) {
        this.controller = controller;
        this.nomeFuncionario = nomeFuncionario;
        this.cargoFuncionario = cargoFuncionario;
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Painel do Funcionário - Atendimento");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        // Painel lateral
        menuLateral = new JPanel();
        menuLateral.setBackground(new Color(25, 45, 90));
        menuLateral.setPreferredSize(new Dimension(220, 0));
        menuLateral.setLayout(new GridLayout(10, 1, 0, 5));

        // Informações do funcionário
        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new GridLayout(3, 1));
        painelInfo.setBackground(new Color(20, 40, 80));

        lblFuncionario = new JLabel("<html><center>" + nomeFuncionario + "<br><small>" + cargoFuncionario + "</small></center></html>", SwingConstants.CENTER);
        lblFuncionario.setForeground(Color.WHITE);
        lblFuncionario.setFont(new Font("Segoe UI", Font.BOLD, 13));

        painelInfo.add(new JLabel(""));
        painelInfo.add(lblFuncionario);
        painelInfo.add(new JLabel(""));

        menuLateral.add(painelInfo);

        // Botões
        btnDashboard = criarBotao("Dashboard");
        btnNovaConta = criarBotao("Nova Conta");
        btnAtualizarDados = criarBotao("Atualizar Dados");
        btnEncerrarConta = criarBotao("Encerrar Conta");
        btnCartoes = criarBotao("Cartões");
        btnSuporte = criarBotao("Suporte");
        btnSair = criarBotao("Sair");

        menuLateral.add(btnDashboard);
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

        // Painel principal (conteúdo dinâmico)
        painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);

        // Inicia com o Dashboard
        mostrarPainel(new PainelDashboard());

        // Ações dos botões
        btnDashboard.addActionListener(e -> mostrarPainel(new PainelDashboard()));
        btnNovaConta.addActionListener(e -> mostrarPainel(new PainelNovaConta(controller)));
        btnAtualizarDados.addActionListener(e -> mostrarPainel(new PainelAtualizarDados(controller)));
        btnEncerrarConta.addActionListener(e -> mostrarPainel(new PainelEncerrarConta(controller)));
        btnCartoes.addActionListener(e -> mostrarPainel(new PainelCartoes(controller)));
        btnSuporte.addActionListener(e -> mostrarPainel(new PainelSuporte(controller)));
        btnSair.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Sair", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                dispose();
            }
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
        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(new Color(40, 80, 160));
            }
            public void mouseExited(MouseEvent e) {
                botao.setBackground(new Color(30, 60, 120));
            }
        });
        return botao;
    }

    private void mostrarPainel(JPanel novoPainel) {
        painelPrincipal.removeAll();
        painelPrincipal.add(novoPainel, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    // Painéis internos (exemplos)
    class PainelDashboard extends JPanel {
        public PainelDashboard() {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            JLabel lbl = new JLabel("Bem-vindo ao Painel de Atendimento", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            add(lbl, BorderLayout.CENTER);
        }
    }

    class PainelNovaConta extends JPanel {
        public PainelNovaConta(SistemaController controller) {
            setLayout(new GridLayout(8, 2, 10, 10));
            setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
            setBackground(Color.WHITE);

            JLabel lblNome = new JLabel("Nome do Cliente:");
            JTextField txtNome = new JTextField();
            JLabel lblNuit = new JLabel("NUIT:");
            JTextField txtNuit = new JTextField();
            JLabel lblEndereco = new JLabel("Endereço:");
            JTextField txtEndereco = new JTextField();
            JLabel lblTelefone = new JLabel("Telefone:");
            JTextField txtTelefone = new JTextField();
            JLabel lblEmail = new JLabel("Email:");
            JTextField txtEmail = new JTextField();
            JLabel lblDocumento = new JLabel("Documento (BI):");
            JTextField txtDocumento = new JTextField();
            JLabel lblTipoConta = new JLabel("Tipo de Conta:");
            JComboBox<Conta.TipoConta> comboTipoConta = new JComboBox<>(Conta.TipoConta.values());
            
            JButton btnCriar = new JButton("Abrir Conta");

            add(lblNome); add(txtNome);
            add(lblNuit); add(txtNuit);
            add(lblEndereco); add(txtEndereco);
            add(lblTelefone); add(txtTelefone);
            add(lblEmail); add(txtEmail);
            add(lblDocumento); add(txtDocumento);
            add(lblTipoConta); add(comboTipoConta);
            add(new JLabel("")); add(btnCriar);

            btnCriar.addActionListener(e -> {
                try {
                    String nome = txtNome.getText();
                    int nuit = Integer.parseInt(txtNuit.getText());
                    String endereco = txtEndereco.getText();
                    int telefone = Integer.parseInt(txtTelefone.getText());
                    String email = txtEmail.getText();
                    String documento = txtDocumento.getText();
                    Conta.TipoConta tipoConta = (Conta.TipoConta) comboTipoConta.getSelectedItem();
                    
                    Conta novaConta = controller.abrirContaCliente(nome, nuit, endereco, telefone, email, documento, tipoConta);
                    if (novaConta != null) {
                        JOptionPane.showMessageDialog(this, 
                            "Conta criada com sucesso!\n" +
                            "ID da Conta: " + novaConta.getIdConta() + "\n" +
                            "Número da Conta: " + novaConta.getNumeroConta());
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao criar conta.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            });
        }
    }

    class PainelAtualizarDados extends JPanel {
        public PainelAtualizarDados(SistemaController controller) {
            setLayout(new GridLayout(6, 2, 10, 10));
            setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
            setBackground(Color.WHITE);

            JLabel lblID = new JLabel("ID do Cliente:");
            JTextField txtID = new JTextField();
            JLabel lblNovoNome = new JLabel("Novo Nome:");
            JTextField txtNovoNome = new JTextField();
            JLabel lblNovoEmail = new JLabel("Novo Email:");
            JTextField txtNovoEmail = new JTextField();
            JLabel lblNovoTelefone = new JLabel("Novo Telefone:");
            JTextField txtNovoTelefone = new JTextField();
            JButton btnAtualizar = new JButton("Atualizar");

            add(lblID); add(txtID);
            add(lblNovoNome); add(txtNovoNome);
            add(lblNovoEmail); add(txtNovoEmail);
            add(lblNovoTelefone); add(txtNovoTelefone);
            add(new JLabel("")); add(btnAtualizar);

            btnAtualizar.addActionListener(e -> {
                try {
                    int id = Integer.parseInt(txtID.getText());
                    String nome = txtNovoNome.getText();
                    String email = txtNovoEmail.getText();
                    int telefone = Integer.parseInt(txtNovoTelefone.getText());
                    
                    boolean ok = controller.atualizarDadosCliente(id, nome, email, telefone);
                    JOptionPane.showMessageDialog(this, ok ? "Dados atualizados!" : "Erro na atualização. Cliente não encontrado.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            });
        }
    }

    class PainelEncerrarConta extends JPanel {
        public PainelEncerrarConta(SistemaController controller) {
            setLayout(new GridLayout(3, 2, 10, 10));
            setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
            setBackground(Color.WHITE);

            JLabel lblID = new JLabel("ID da Conta:");
            JTextField txtID = new JTextField();
            JButton btnEncerrar = new JButton("Encerrar Conta");

            add(lblID); add(txtID);
            add(new JLabel("")); add(btnEncerrar);

            btnEncerrar.addActionListener(e -> {
                try {
                    int id = Integer.parseInt(txtID.getText());
                    boolean ok = controller.encerrarContaCliente(id);
                    JOptionPane.showMessageDialog(this, ok ? "Conta encerrada." : "Erro ao encerrar conta. Conta não encontrada.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            });
        }
    }

    class PainelCartoes extends JPanel {
        public PainelCartoes(SistemaController controller) {
            setLayout(new GridLayout(4, 2, 10, 10));
            setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
            setBackground(Color.WHITE);

            JLabel lblID = new JLabel("ID da Conta:");
            JTextField txtID = new JTextField();
            JButton btnReemitir = new JButton("Reemitir Cartão");

            add(lblID); add(txtID);
            add(new JLabel("")); add(btnReemitir);

            btnReemitir.addActionListener(e -> {
                try {
                    int id = Integer.parseInt(txtID.getText());
                    String resultado = controller.reemitirCartao(id);
                    JOptionPane.showMessageDialog(this, resultado);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            });
        }
    }

    class PainelSuporte extends JPanel {
        public PainelSuporte(SistemaController controller) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            JTextArea area = new JTextArea("Área de suporte e resolução de irregularidades.\n\nUse esta secção para registar reclamações ou problemas dos clientes.");
            area.setEditable(false);
            area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            add(new JScrollPane(area), BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PainelFuncAtendimento(new SistemaController(), "Frederico", "Atendimento").setVisible(true));
    }
}