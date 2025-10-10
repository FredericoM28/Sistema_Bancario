/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Dell
 */
public class TelaCliente extends javax.swing.JPanel {

    /**
     * Creates new form TelaCliente
     */
    public TelaCliente() {
        initComponents();
        
        // ADICIONE ESTA LINHA - Muda o layout do jPanel1
        jPanel1.setLayout(new java.awt.BorderLayout());
    
        configurarListeners();
        mostrarTelaInicial();
        

    }
    
    private void configurarListeners() {
        jButton1.addActionListener(e -> mostrarConsultarSaldo());
        jButton2.addActionListener(e -> mostrarDepositar());
        jButton4.addActionListener(e -> mostrarSaque());
        jButton3.addActionListener(e -> mostrarTransferir());
        jButton7.addActionListener(e -> mostrarTransferirCelular());
        jButton5.addActionListener(e -> mostrarAlterarSenha());
        jButton6.addActionListener(e -> sair());
    }
    private void mostrarTelaInicial() {
        jPanel1.removeAll();
    
        JPanel panelInicial = new JPanel(new GridBagLayout());
        panelInicial.setBackground(Color.WHITE);
    
        JLabel lblMensagem = new JLabel("Bem-vindo! Selecione uma operação");
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblMensagem.setForeground(new Color(120, 120, 120));
    
        panelInicial.add(lblMensagem);
    
        // MUDE ESTA LINHA - use BorderLayout.CENTER
        jPanel1.add(panelInicial, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

  
    private void mostrarConsultarSaldo() {
        jPanel1.removeAll();
    
        // TESTE VISÍVEL - label colorido
        JLabel teste = new JLabel("🎉 CONSULTAR SALDO - FUNCIONANDO!", JLabel.CENTER);
        teste.setFont(new Font("Segoe UI", Font.BOLD, 24));
        teste.setForeground(Color.RED);
        teste.setOpaque(true);
        teste.setBackground(Color.YELLOW);
    
        jPanel1.add(teste, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
    
        System.out.println("Botão Consultar Saldo clicado!");
    }

    private void mostrarDepositar() {
        jPanel1.removeAll();
        
        JPanel panelDeposito = new JPanel(new BorderLayout());
        panelDeposito.setBackground(Color.WHITE);
        panelDeposito.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("DEPOSITAR VALOR", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panelDeposito.add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campo Valor
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblValor = criarLabel("Valor (MT):");
        formPanel.add(lblValor, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtValor = criarTextField();
        formPanel.add(txtValor, gbc);

        // Botão Depositar
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnDepositar = criarBotaoAcao("💵 DEPOSITAR", new Color(0, 100, 0));
        formPanel.add(btnDepositar, gbc);

        panelDeposito.add(formPanel, BorderLayout.CENTER);
        jPanel1.add(panelDeposito);
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
        formPanel.add(btnSacar, gbc);

        panelSaque.add(formPanel, BorderLayout.CENTER);
        jPanel1.add(panelSaque);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void mostrarTransferir() {
        jPanel1.removeAll();
        
        JPanel panelTransferencia = new JPanel(new BorderLayout());
        panelTransferencia.setBackground(Color.WHITE);
        panelTransferencia.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("TRANSFERÊNCIA BANCÁRIA", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 0, 139));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panelTransferencia.add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Campo Conta Destino
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(criarLabel("Conta Destino:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanel.add(criarTextField(), gbc);

        // Campo Valor
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(criarLabel("Valor (MT):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanel.add(criarTextField(), gbc);

        // Campo Descrição
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(criarLabel("Descrição:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        formPanel.add(criarTextField(), gbc);

        // Botão Transferir
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnTransferir = criarBotaoAcao("🔄 TRANSFERIR", new Color(0, 100, 0));
        formPanel.add(btnTransferir, gbc);

        panelTransferencia.add(formPanel, BorderLayout.CENTER);
        jPanel1.add(panelTransferencia);
        jPanel1.revalidate();
        jPanel1.repaint();
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
        formPanel.add(criarTextField(), gbc);

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
        formPanel.add(criarTextField(), gbc);

        // Botão Transferir
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnTransferir = criarBotaoAcao("📱 TRANSFERIR", new Color(0, 100, 0));
        formPanel.add(btnTransferir, gbc);

        panelCelular.add(formPanel, BorderLayout.CENTER);
        jPanel1.add(panelCelular);
        jPanel1.revalidate();
        jPanel1.repaint();
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
        adicionarCampoSenhaFormulario(formPanel, gbc, "Senha Atual:", 0);
        adicionarCampoSenhaFormulario(formPanel, gbc, "Nova Senha:", 1);
        adicionarCampoSenhaFormulario(formPanel, gbc, "Confirmar Senha:", 2);

        // Botão Alterar
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnAlterar = criarBotaoAcao("🔐 ALTERAR SENHA", new Color(0, 100, 0));
        formPanel.add(btnAlterar, gbc);

        panelSenha.add(formPanel, BorderLayout.CENTER);
        jPanel1.add(panelSenha);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void sair() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair?",
            "Confirmação de Saída",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Fechar a aplicação ou voltar para login
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

    private JButton criarBotaoAcao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(250, 40));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor.darker(), 2),
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

    private void adicionarCampoSenhaFormulario(JPanel panel, GridBagConstraints gbc, String label, int linha) {
        gbc.gridx = 0; gbc.gridy = linha;
        gbc.weightx = 0;
        panel.add(criarLabel(label), gbc);
        
        gbc.gridx = 1; gbc.gridy = linha;
        gbc.weightx = 1.0;
        panel.add(criarPasswordField(), gbc);
    }

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
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
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

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setText("Alterar Senha");

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addContainerGap(33, Short.MAX_VALUE))
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
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}


