package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Janela de login que permite aos usuários autenticarem-se ou criar uma nova conta.
 */
public class JanelaLogin extends JPanel {
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton btnLogin;
    private JButton btnCriarConta;
    private JButton btnVoltar;

    public JanelaLogin(ActionListener onVoltar, ActionListener onLogin, ActionListener onCriarConta) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Configurar o painel de título
        configurarPainelTitulo();

        // Configurar o painel de formulário
        configurarPainelFormulario();

        // Configurar botões
        configurarBotoes(onVoltar, onLogin, onCriarConta);
    }

    private void configurarPainelTitulo() {
        JPanel painelTitulo = new JPanel();
        painelTitulo.setLayout(new BorderLayout());

        JLabel tituloLabel = new JLabel("Login de Usuário");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtituloLabel = new JLabel("Entre com suas credenciais ou crie uma nova conta");
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

        // Campo de usuário
        JLabel usuarioLabel = new JLabel("Nome de Usuário:");
        campoUsuario = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFormulario.add(usuarioLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        painelFormulario.add(campoUsuario, gbc);

        // Campo de senha
        JLabel senhaLabel = new JLabel("Senha:");
        campoSenha = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelFormulario.add(senhaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        painelFormulario.add(campoSenha, gbc);

        // Centralizar o formulário
        JPanel painelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelCentral.add(painelFormulario);

        add(painelCentral, BorderLayout.CENTER);
    }

    private void configurarBotoes(ActionListener onVoltar, ActionListener onLogin, ActionListener onCriarConta) {
        // Botão de login
        btnLogin = new JButton("Entrar");
        btnLogin.setPreferredSize(new Dimension(100, 30));
        if (onLogin != null) {
            btnLogin.addActionListener(onLogin);
        } else {
            btnLogin.addActionListener(e -> {
                String usuario = campoUsuario.getText();
                char[] senha = campoSenha.getPassword();

                if (autenticarUsuario(usuario, new String(senha))) {
                    JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                // Limpar a senha por segurança
                for (int i = 0; i < senha.length; i++) {
                    senha[i] = 0;
                }
            });
        }

        // Botão de criar conta
        btnCriarConta = new JButton("Criar Nova Conta");
        btnCriarConta.setPreferredSize(new Dimension(150, 30));
        if (onCriarConta != null) {
            btnCriarConta.addActionListener(onCriarConta);
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
        painelBotoes.add(btnLogin);
        painelBotoes.add(btnCriarConta);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    /**
     * Método temporário para autenticar usuários.
     * Em uma implementação real, isso seria substituído por um sistema de autenticação adequado.
     */
    private boolean autenticarUsuario(String usuario, String senha) {
        // Implementação simples para demonstração
        // Em uma aplicação real, isso consultaria um banco de dados ou um serviço de autenticação
        return usuario.equals("admin") && senha.equals("admin");
    }

    // Getters para os campos e botões
    public String getUsuario() {
        return campoUsuario.getText();
    }

    public char[] getSenha() {
        return campoSenha.getPassword();
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnCriarConta() {
        return btnCriarConta;
    }

    public JButton getBtnVoltar() {
        return btnVoltar;
    }
}
