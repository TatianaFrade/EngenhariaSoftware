package pt.ipleiria.estg.ei.dei.esoft;

import java.io.Serializable;

/**
 * Classe que representa um usuário do sistema.
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String nomeUsuario;
    private String email;
    private String senha; // Em uma aplicação real, deveria ser armazenado com hash e salt
    private boolean administrador;

    public Usuario(String nome, String nomeUsuario, String email, String senha, boolean administrador) {
        this.nome = nome;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.administrador = administrador;
    }

    // Construtor para usuário comum (não administrador)
    public Usuario(String nome, String nomeUsuario, String email, String senha) {
        this(nome, nomeUsuario, email, senha, false);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Retorna a senha do usuário.
     * Nota: Em uma aplicação real de produção, não se deve retornar a senha diretamente,
     * mas para este exercício educacional é necessário para o funcionamento da funcionalidade.
     * @return A senha do usuário
     */
    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    /**
     * Verifica se a senha fornecida corresponde à senha do usuário.
     * Em uma aplicação real, deveria usar algoritmos de hash seguros.
     * @param senhaFornecida A senha a ser verificada
     * @return true se a senha estiver correta, false caso contrário
     */
    public boolean verificarSenha(String senhaFornecida) {
        return this.senha != null && this.senha.equals(senhaFornecida);
    }
    // Método para compatibilidade com nome usado em algumas classes (alias para getNomeUsuario)
    public String getUsuario() {
        String nome = getNomeUsuario();
        System.out.println("[DEBUG] getUsuario chamado, retornando: " + nome);
        return nome;
    }
    // Método para compatibilidade com nome usado em algumas classes (alias para setNomeUsuario)
    public void setUsuario(String usuario) {
        System.out.println("[DEBUG] setUsuario chamado com: " + usuario);
        setNomeUsuario(usuario);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", email='" + email + '\'' +
                ", administrador=" + administrador +
                ", senha definida=" + (senha != null) +
                '}';
    }
}
