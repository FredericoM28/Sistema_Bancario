package View;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Dell
 */
public class TelaAdmin extends javax.swing.JFrame {

    /**
     * Creates new form TelaAdmin
     */
    
    public TelaAdmin() {
        initComponents();
        setTitle("Sistema Bancário - Banco Nexus");
        
        // Painel inicial - PERFEITAMENTE CENTRALIZADO
        JPanel panelInicial = new JPanel(new BorderLayout());
        panelInicial.setBackground(Color.WHITE);

        JLabel lblInicial = new JLabel("Selecione uma opção no menu", JLabel.CENTER);
        lblInicial.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblInicial.setForeground(new Color(128, 128, 128));

        panelInicial.add(lblInicial, BorderLayout.CENTER);
        jPanel3.add(panelInicial, BorderLayout.CENTER);
        
        
        // Listeners dos botões
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarFormGestores();
            }
        });
    
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarFormFuncionarios();
            }
        });
    
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarRelatorios();
            }
        });
    
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
    }
    
    // MÉTODOS PARA O PAINEL DINÂMICO - COMPLETOS E MELHORADOS

    private void mostrarFormGestores() {
        jPanel3.removeAll();
    
        // Painel principal com bordas e margens
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
    
        // Título
        JLabel titulo = new JLabel("CADASTRO DE GESTOR", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(0, 0, 120));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titulo, BorderLayout.NORTH);
        
        
        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
    
        // Campo Nome
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblNome = criarLabel("Nome:");
        formPanel.add(lblNome, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtNome = criarTextField();
        formPanel.add(txtNome, gbc);
    
        // Campo Idade
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblIdade = criarLabel("Idade:");
        formPanel.add(lblIdade, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField txtIdade = criarTextField();
        formPanel.add(txtIdade, gbc);
    
        // Campo ID
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel lblID = criarLabel("ID:");
        formPanel.add(lblID, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField txtID = criarTextField();
        formPanel.add(txtID, gbc);
    
        // Campo Usuário
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel lblUsuario = criarLabel("Usuário:");
        formPanel.add(lblUsuario, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 1.0;
        JTextField txtUsuario = criarTextField();
        formPanel.add(txtUsuario, gbc);
    
        // Campo Senha
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel lblSenha = criarLabel("Senha:");
        formPanel.add(lblSenha, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.weightx = 1.0;
        JPasswordField txtSenha = criarPasswordField();
        formPanel.add(txtSenha, gbc);
    
        // Botão Salvar
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSalvar = criarBotaoSalvar("Salvar Gestor");
        formPanel.add(btnSalvar, gbc);
    
        mainPanel.add(formPanel, BorderLayout.CENTER);
        jPanel3.add(mainPanel, BorderLayout.CENTER);
        jPanel3.revalidate();
        jPanel3.repaint();
    }

    private void mostrarFormFuncionarios() {
        jPanel3.removeAll();
    
        // Painel principal com bordas e margens
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
    
        // Título
        JLabel titulo = new JLabel("CADASTRO DE FUNCIONÁRIO", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(0, 0, 120));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titulo, BorderLayout.NORTH);
    
        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
    
        // Campo Nome
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblNome = criarLabel("Nome:");
        formPanel.add(lblNome, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtNome = criarTextField();
        formPanel.add(txtNome, gbc);
    
        // Campo Idade
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblIdade = criarLabel("Idade:");
        formPanel.add(lblIdade, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField txtIdade = criarTextField();
        formPanel.add(txtIdade, gbc);
    
        // Campo ID
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel lblID = criarLabel("ID:");
        formPanel.add(lblID, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField txtID = criarTextField();
        formPanel.add(txtID, gbc);
    
        // Campo Tipo
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel lblTipo = criarLabel("Tipo:");
        formPanel.add(lblTipo, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 1.0;
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Selecione...", "Caixa", "Atendimento"});
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTipo.setPreferredSize(new Dimension(300, 35));
        formPanel.add(cmbTipo, gbc);
    
        // Campo Usuário
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel lblUsuario = criarLabel("Usuário:");
        formPanel.add(lblUsuario, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.weightx = 1.0;
        JTextField txtUsuario = criarTextField();
        formPanel.add(txtUsuario, gbc);
    
        // Campo Senha
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.weightx = 0;
        JLabel lblSenha = criarLabel("Senha:");
        formPanel.add(lblSenha, gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.weightx = 1.0;
        JPasswordField txtSenha = criarPasswordField();
        formPanel.add(txtSenha, gbc);
    
        // Botão Salvar
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSalvar = criarBotaoSalvar("Salvar Funcionário");
        formPanel.add(btnSalvar, gbc);
    
        mainPanel.add(formPanel, BorderLayout.CENTER);
        jPanel3.add(mainPanel, BorderLayout.CENTER);
        jPanel3.revalidate();
        jPanel3.repaint();
    }

    private void mostrarRelatorios() {
        jPanel3.removeAll();
        
        JPanel relatorioPanel = new JPanel(new BorderLayout());
        relatorioPanel.setBackground(Color.WHITE);
        relatorioPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        
        JLabel lbl = new JLabel("RELATÓRIOS DO SISTEMA", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setForeground(new Color(0, 0, 120));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        relatorioPanel.add(lbl, BorderLayout.NORTH);
        
        JLabel lblStatus = new JLabel("Funcionalidade em desenvolvimento", JLabel.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblStatus.setForeground(Color.GRAY);
        relatorioPanel.add(lblStatus, BorderLayout.CENTER);
        
        jPanel3.add(relatorioPanel, BorderLayout.CENTER);
        jPanel3.revalidate();
        jPanel3.repaint();
    }
    
    // MÉTODOS AUXILIARES PARA CRIAR COMPONENTES
    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private JTextField criarTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 35));
        field.setMinimumSize(new Dimension(300, 35));
        return field;
    }

    private JPasswordField criarPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 35));
        field.setMinimumSize(new Dimension(300, 35));
        return field;
    }

    private JButton criarBotaoSalvar(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBackground(new Color(0, 120, 0));
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(200, 45));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return botao;
    }

        
 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 120));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Painel Do Administrador");

        jButton1.setBackground(new java.awt.Color(0, 0, 120));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Gerenciar Gerente");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 0, 120));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Gerenciar Funcionários");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 0, 120));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Ver Relatórios");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 0, 120));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Sair");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 571, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 466, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        // Ação para Gerenciar Gerente
        javax.swing.JOptionPane.showMessageDialog(this, "Funcionalidade de Gerenciar Gerente em desenvolvimento");
          
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        // Ação para Gerenciar Funcionários  
        javax.swing.JOptionPane.showMessageDialog(this, "Funcionalidade de Gerenciar Funcionários em desenvolvimento");

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {                                         
       // Fecha a tela atual (Painel do Administrador)
       this.dispose();
       

       // Abre novamente a tela de login (LoginNV)
       LoginNV telaLogin = new LoginNV();
       telaLogin.setVisible(true);
       telaLogin.setLocationRelativeTo(null); // Centraliza a tela 
    }
    
    
        
    
    


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
