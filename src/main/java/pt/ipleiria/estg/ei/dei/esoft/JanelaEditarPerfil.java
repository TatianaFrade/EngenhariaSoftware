package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Janela para edição de perfil de usuário, permitindo atualizar dados e excluir conta.
 */
public class JanelaEditarPerfil extends JPanel {
    private JTextField campoNome;
    private JTextField campoUsuario;
    private JTextField campoEmail;
    private JPasswordField campoSenhaAtual;
    private JPasswordField campoNovaSenha;
    private JPasswordField campoConfirmaSenha;

    private JButton btnSalvar;
    private JButton btnExcluirConta;
    private JButton btnVoltar;

    private Usuario usuario;

    /**
     * Construtor da janela de edição de perfil
     *
     * @param usuario Usuário atual logado
     * @param onVoltar Listener para o botão Voltar
     * @param onSalvar Listener para o botão Salvar alterações
     * @param onExcluir Listener para o botão Excluir conta
     */
    public JanelaEditarPerfil(Usuario usuario, ActionListener onVoltar,
                              ActionListener onSalvar, ActionListener onExcluir) {
        this.usuario = usuario;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Configurar o painel de título
        configurarPainelTitulo();

        // Configurar o painel de formulário
        configurarPainelFormulario();

        // Configurar botões
        configurarBotoes(onVoltar, onSalvar, onExcluir);

        // Preencher campos com dados do usuário
        preencherCampos();
    }

    private void configurarPainelTitulo() {
        JPanel painelTitulo = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Editar Perfil");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelTitulo.add(titulo, BorderLayout.CENTER);
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(painelTitulo, BorderLayout.NORTH);
    }

    private void configurarPainelFormulario() {
        JPanel painelFormulario = new JPanel(new GridLayout(0, 2, 10, 10));

        // Campos de informações pessoais
        painelFormulario.add(new JLabel("Nome:"));
        campoNome = new JTextField(20);
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("Nome de Usuário:"));
        campoUsuario = new JTextField(20);
        // O nome de usuário não pode ser alterado após o registro
        campoUsuario.setEditable(false);
        campoUsuario.setEnabled(false);
        painelFormulario.add(campoUsuario);

        painelFormulario.add(new JLabel("Email:"));
        campoEmail = new JTextField(20);
        painelFormulario.add(campoEmail);

        // Separador para alterar senha
        JLabel labelSenha = new JLabel("Alterar Senha (opcional)");
        labelSenha.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel painelSeparador = new JPanel(new BorderLayout());
        painelSeparador.add(labelSenha, BorderLayout.WEST);
        painelSeparador.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Adicionando o separador com grid span 2
        JPanel painelSeparadorCompleto = new JPanel(new BorderLayout());
        painelSeparadorCompleto.add(painelSeparador, BorderLayout.CENTER);
        painelFormulario.add(painelSeparadorCompleto);
        painelFormulario.add(new JLabel()); // Célula vazia para manter o grid

        // Campos para alteração de senha
        painelFormulario.add(new JLabel("Senha Atual:"));
        campoSenhaAtual = new JPasswordField(20);
        painelFormulario.add(campoSenhaAtual);

        painelFormulario.add(new JLabel("Nova Senha:"));
        campoNovaSenha = new JPasswordField(20);
        painelFormulario.add(campoNovaSenha);

        painelFormulario.add(new JLabel("Confirmar Nova Senha:"));
        campoConfirmaSenha = new JPasswordField(20);
        painelFormulario.add(campoConfirmaSenha);

        // Adiciona o painel de formulário em um painel com scroll
        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(null);

        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.add(scrollPane, BorderLayout.NORTH);
        add(painelCentral, BorderLayout.CENTER);
    }

    private void configurarBotoes(ActionListener onVoltar, ActionListener onSalvar, ActionListener onExcluir) {
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnVoltar = new JButton("Voltar");
        btnSalvar = new JButton("Salvar Alterações");
        btnExcluirConta = new JButton("Excluir Conta");

        // Estilizando botão de excluir em vermelho para indicar ação destrutiva
        btnExcluirConta.setBackground(new Color(220, 53, 69));
        btnExcluirConta.setForeground(Color.WHITE);
        btnExcluirConta.setFocusPainted(false);

        // Adicionando listeners
        btnVoltar.addActionListener(onVoltar);
        btnSalvar.addActionListener(onSalvar);
        btnExcluirConta.addActionListener(onExcluir);

        JPanel painelEsquerda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelEsquerda.add(btnVoltar);

        JPanel painelDireita = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelDireita.add(btnSalvar);
        painelDireita.add(btnExcluirConta);

        JPanel painelBotoesCompleto = new JPanel(new BorderLayout());
        painelBotoesCompleto.add(painelEsquerda, BorderLayout.WEST);
        painelBotoesCompleto.add(painelDireita, BorderLayout.EAST);
        painelBotoesCompleto.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        add(painelBotoesCompleto, BorderLayout.SOUTH);
    }

    private void preencherCampos() {
        if (usuario != null) {
            campoNome.setText(usuario.getNome());
            campoUsuario.setText(usuario.getUsuario());
            campoEmail.setText(usuario.getEmail());
        }
    }

    /**
     * Valida o formulário para garantir que todos os campos obrigatórios estão preenchidos
     * e que a nova senha está confirmada corretamente, caso esteja sendo alterada.
     *
     * @return true se o formulário é válido, false caso contrário
     */
    public boolean validarFormulario() {
        // Verifica se campos obrigatórios estão preenchidos
        if (campoNome.getText().trim().isEmpty() || campoEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nome e email são campos obrigatórios.",
                    "Campos Obrigatórios",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Se a senha atual foi inserida, verifica se novas senhas correspondem
        if (campoSenhaAtual.getPassword().length > 0) {
            String senhaAtual = new String(campoSenhaAtual.getPassword());
            String novaSenha = new String(campoNovaSenha.getPassword());
            String confirmaSenha = new String(campoConfirmaSenha.getPassword());

            // Verifica se a senha atual é correta
            if (!usuario.verificarSenha(senhaAtual)) {
                JOptionPane.showMessageDialog(this,
                        "A senha atual está incorreta.",
                        "Erro de Verificação",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Verifica se nova senha foi informada
            if (novaSenha.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, informe a nova senha.",
                        "Nova Senha Obrigatória",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // Verifica se as senhas novas correspondem
            if (!novaSenha.equals(confirmaSenha)) {
                JOptionPane.showMessageDialog(this,
                        "A nova senha e a confirmação não correspondem.",
                        "Senhas Não Coincidem",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true;
    }    /**
     * Retorna os dados do usuário atualizados
     * @return Objeto Usuario com os dados atualizados
     */
    public Usuario getDadosAtualizados() {
        // Primeiro preservamos a senha original
        String senhaParaUsar = usuario.getSenha();

        // Se uma nova senha foi inserida, usamos a nova senha
        if (campoNovaSenha.getPassword().length > 0) {
            senhaParaUsar = new String(campoNovaSenha.getPassword());
        }

        System.out.println("getDadosAtualizados: Criando novo objeto Usuario com dados atualizados");
        System.out.println("Nome original: " + usuario.getNome() + " -> Novo nome: " + campoNome.getText());
        System.out.println("Email original: " + usuario.getEmail() + " -> Novo email: " + campoEmail.getText());
        System.out.println("Senha alterada: " + (campoNovaSenha.getPassword().length > 0 ? "Sim" : "Não"));

        // Criamos o usuário atualizado com a senha correta (original ou nova)
        Usuario usuarioAtualizado = new Usuario(
                campoNome.getText(),
                usuario.getUsuario(), // Nome de usuário não muda
                campoEmail.getText(),
                senhaParaUsar,
                usuario.isAdministrador() // Preserva status de administrador
        );

        System.out.println("Objeto Usuario criado: " + usuarioAtualizado);
        return usuarioAtualizado;
    }
    /**
     * Retorna a senha atual digitada pelo usuário para confirmação
     */
    public char[] getSenhaAtual() {
        char[] senha = campoSenhaAtual.getPassword();
        System.out.println("Senha atual fornecida no campo: " + (senha != null ? (senha.length > 0 ? "preenchida (" + senha.length + " caracteres)" : "vazia") : "null"));
        return senha;
    }
}
