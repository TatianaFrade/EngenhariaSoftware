package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Janela para criação de uma nova conta de usuário.
 */
public class JanelaCriarConta extends JPanel {
    private JTextField campoNome;
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JTextField campoEmail;
    private JButton btnCriar;
    private JButton btnVoltar;

    public JanelaCriarConta(ActionListener onVoltar, ActionListener onCriarConta) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Configurar o painel de título
        configurarPainelTitulo();

        // Configurar o painel de formulário
        configurarPainelFormulario();

        // Configurar botões
        configurarBotoes(onVoltar, onCriarConta);
    }

    private void configurarPainelTitulo() {
        JPanel painelTitulo = new JPanel();
        painelTitulo.setLayout(new BorderLayout());

        JLabel tituloLabel = new JLabel("Criar Nova Conta");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtituloLabel = new JLabel("Preencha os campos abaixo para criar sua conta");
        subtituloLabel.setHorizontalAlignment(SwingConstants.CENTER);

        painelTitulo.add(tituloLabel, BorderLayout.CENTER);
        painelTitulo.add(subtituloLabel, BorderLayout.SOUTH);
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(painelTitulo, BorderLayout.NORTH);
    }

    private void configurarPainelFormulario() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campo de nome completo
        JLabel nomeLabel = new JLabel("Nome Completo:");
        campoNome = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFormulario.add(nomeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        painelFormulario.add(campoNome, gbc);

        // Campo de nome de usuário
        JLabel usuarioLabel = new JLabel("Nome de Usuário:");
        campoUsuario = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelFormulario.add(usuarioLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        painelFormulario.add(campoUsuario, gbc);

        // Campo de email
        JLabel emailLabel = new JLabel("Email:");
        campoEmail = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelFormulario.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        painelFormulario.add(campoEmail, gbc);

        // Campo de senha
        JLabel senhaLabel = new JLabel("Senha:");
        campoSenha = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 3;
        painelFormulario.add(senhaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        painelFormulario.add(campoSenha, gbc);

        // Campo de confirmar senha
        JLabel confirmarSenhaLabel = new JLabel("Confirmar Senha:");
        campoConfirmarSenha = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 4;
        painelFormulario.add(confirmarSenhaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        painelFormulario.add(campoConfirmarSenha, gbc);

        // Centralizar o formulário
        JPanel painelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelCentral.add(painelFormulario);

        add(painelCentral, BorderLayout.CENTER);
    }

    private void configurarBotoes(ActionListener onVoltar, ActionListener onCriarConta) {
        // Botão de criar conta
        btnCriar = new JButton("Criar Conta");
        btnCriar.setPreferredSize(new Dimension(120, 30));
        if (onCriarConta != null) {
            btnCriar.addActionListener(onCriarConta);
        } else {
            btnCriar.addActionListener(e -> {
                if (validarFormulario()) {
                    JOptionPane.showMessageDialog(this, "Conta criada com sucesso!");
                }
            });
        }

        // Botão voltar
        btnVoltar = new JButton("Voltar");
        btnVoltar.setPreferredSize(new Dimension(100, 30));
        if (onVoltar != null) {
            btnVoltar.addActionListener(onVoltar);
        }

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnCriar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    /**
     * Valida o formulário de criação de conta.
     * @return true se o formulário estiver válido, false caso contrário.
     */
    public boolean validarFormulario() {
        if (campoNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe seu nome completo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (campoUsuario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe um nome de usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (campoEmail.getText().trim().isEmpty() || !campoEmail.getText().contains("@")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe um email válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        char[] senha = campoSenha.getPassword();
        if (senha.length == 0) {
            JOptionPane.showMessageDialog(this, "Por favor, informe uma senha.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        char[] confirmSenha = campoConfirmarSenha.getPassword();
        if (!String.valueOf(senha).equals(String.valueOf(confirmSenha))) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Limpar as senhas por segurança
        for (int i = 0; i < senha.length; i++) {
            senha[i] = 0;
        }
        for (int i = 0; i < confirmSenha.length; i++) {
            confirmSenha[i] = 0;
        }

        return true;
    }

    // Getters para os campos e botões
    public String getNome() {
        return campoNome.getText();
    }

    public String getUsuario() {
        return campoUsuario.getText();
    }

    public String getEmail() {
        return campoEmail.getText();
    }

    public char[] getSenha() {
        return campoSenha.getPassword();
    }

    public JButton getBtnCriar() {
        return btnCriar;
    }

    public JButton getBtnVoltar() {
        return btnVoltar;
    }
}
