package pt.ipleiria.estg.ei.dei.esoft;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pelo gerenciamento de usuários do sistema.
 */
public class UsuarioService {
    private static final String DIRETORIO_DADOS = "dados/";
    private static final String ARQUIVO_USUARIOS = DIRETORIO_DADOS + "usuarios.json";
    private static Gson gson = new Gson();

    /**
     * Salva um novo usuário no sistema.
     * @param usuario O usuário a ser salvo
     * @return true se o usuário foi salvo com sucesso, false caso contrário
     */
    public static boolean salvarUsuario(Usuario usuario) {
        try {
            // Verificar se o nome de usuário já existe
            if (buscarUsuarioPorNomeUsuario(usuario.getNomeUsuario()) != null) {
                return false;
            }

            // Carregar lista atual de usuários
            List<Usuario> usuarios = carregarUsuarios();
            usuarios.add(usuario);

            // Garantir que o diretório de dados existe
            File diretorio = new File(DIRETORIO_DADOS);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            // Salvar a lista atualizada
            try (Writer writer = new FileWriter(ARQUIVO_USUARIOS)) {
                gson.toJson(usuarios, writer);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carrega a lista de usuários do arquivo.
     * @return Lista de usuários
     */
    public static List<Usuario> carregarUsuarios() {
        try {
            File arquivo = new File(ARQUIVO_USUARIOS);
            if (!arquivo.exists()) {
                // Criar arquivo com usuários padrão se não existir
                criarUsuariosPadrao();
                return carregarUsuarios();
            }

            Type tipoLista = new TypeToken<List<Usuario>>(){}.getType();
            try (Reader reader = new FileReader(arquivo)) {
                List<Usuario> usuarios = gson.fromJson(reader, tipoLista);
                return usuarios != null ? usuarios : new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Cria usuários padrão para o sistema.
     */
    private static void criarUsuariosPadrao() {
        try {
            List<Usuario> usuarios = new ArrayList<>();

            // Adicionar usuário administrador padrão
            usuarios.add(new Usuario("Administrador", "admin", "admin@cinema.com", "admin", true));

            // Garantir que o diretório de dados existe
            File diretorio = new File(DIRETORIO_DADOS);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            // Salvar no arquivo
            try (Writer writer = new FileWriter(ARQUIVO_USUARIOS)) {
                gson.toJson(usuarios, writer);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar usuários padrão: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Busca um usuário pelo nome de usuário.
     * @param nomeUsuario Nome de usuário a ser buscado
     * @return Usuário encontrado ou null se não existir
     */
    public static Usuario buscarUsuarioPorNomeUsuario(String nomeUsuario) {
        List<Usuario> usuarios = carregarUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getNomeUsuario().equalsIgnoreCase(nomeUsuario)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Autentica um usuário com base no nome de usuário e senha.
     * @param nomeUsuario Nome de usuário
     * @param senha Senha do usuário
     * @return Usuário autenticado ou null se a autenticação falhar
     */
    public static Usuario autenticarUsuario(String nomeUsuario, String senha) {
        Usuario usuario = buscarUsuarioPorNomeUsuario(nomeUsuario);
        if (usuario != null && usuario.verificarSenha(senha)) {
            return usuario;
        }
        return null;
    }
    /**
     * Atualiza os dados de um usuário existente.
     * @param nomeUsuario Nome de usuário do usuário a ser atualizado (não pode ser alterado)
     * @param usuarioAtualizado Objeto com os dados atualizados do usuário
     * @return true se o usuário foi atualizado com sucesso, false caso contrário
     */
    public static boolean atualizarUsuario(String nomeUsuario, Usuario usuarioAtualizado) {
        System.out.println("UsuarioService.atualizarUsuario: Iniciando atualização para usuário " + nomeUsuario);
        System.out.println("Dados atualizados: Nome=" + usuarioAtualizado.getNome() +
                ", Email=" + usuarioAtualizado.getEmail() +
                ", Senha presente=" + (usuarioAtualizado.getSenha() != null));

        try {
            // Garantir que o diretório de dados existe
            File diretorio = new File(DIRETORIO_DADOS);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
                System.out.println("Diretório de dados criado: " + diretorio.getAbsolutePath());
            }

            List<Usuario> usuarios = carregarUsuarios();
            System.out.println("Usuários carregados: " + usuarios.size());
            boolean atualizado = false;

            for (int i = 0; i < usuarios.size(); i++) {
                Usuario usuario = usuarios.get(i);
                System.out.println("Verificando usuário #" + i + ": " + usuario.getNomeUsuario());
                if (usuario.getNomeUsuario().equals(nomeUsuario)) {
                    System.out.println("Encontrado usuário a ser atualizado: " + usuario.getNomeUsuario());
                    // Substituir o usuário na lista mantendo o nome de usuário original
                    usuarioAtualizado.setNomeUsuario(nomeUsuario);
                    usuarios.set(i, usuarioAtualizado);
                    atualizado = true;
                    break;
                }
            }

            // Se o usuário foi encontrado e atualizado, salvar a lista
            if (atualizado) {
                System.out.println("Salvando lista de usuários atualizada...");                // Garantir que o arquivo exista e tente salvá-lo
                File arquivo = new File(ARQUIVO_USUARIOS);
                if (!arquivo.exists()) {
                    arquivo.createNewFile();
                    System.out.println("Arquivo de usuários criado: " + arquivo.getAbsolutePath());
                }

                System.out.println("Salvando para o arquivo: " + arquivo.getAbsolutePath());
                try (Writer writer = new FileWriter(arquivo)) {
                    gson.toJson(usuarios, writer);
                    writer.flush(); // Garantir que os dados sejam gravados
                    System.out.println("Lista de usuários salva com sucesso.");
                    return true;
                }
            } else {
                System.out.println("ERRO: Usuário " + nomeUsuario + " não encontrado na lista.");
            }

            return false;
        } catch (IOException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Exclui um usuário do sistema.
     * @param nomeUsuario Nome de usuário do usuário a ser excluído
     * @return true se o usuário foi excluído com sucesso, false caso contrário
     */
    public static boolean excluirUsuario(String nomeUsuario) {
        System.out.println("UsuarioService.excluirUsuario: Iniciando exclusão do usuário " + nomeUsuario);

        try {
            // Garantir que o diretório de dados existe
            File diretorio = new File(DIRETORIO_DADOS);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
                System.out.println("Diretório de dados criado: " + diretorio.getAbsolutePath());
            }

            // Carregar lista atual de usuários
            List<Usuario> usuarios = carregarUsuarios();
            System.out.println("Usuários carregados: " + usuarios.size());

            // Verificar se há o usuário administrador padrão antes da exclusão
            boolean temAdmin = false;
            for (Usuario u : usuarios) {
                if (u.isAdministrador() && !u.getNomeUsuario().equals(nomeUsuario)) {
                    temAdmin = true;
                    break;
                }
            }

            // Se o único administrador está tentando excluir sua conta, impeça
            // Esse é apenas um cuidado extra para não deixar o sistema sem admin
            if (!temAdmin) {
                for (Usuario u : usuarios) {
                    if (u.getNomeUsuario().equals(nomeUsuario) && u.isAdministrador()) {
                        System.out.println("ERRO: Tentativa de excluir o único usuário administrador.");
                        return false;
                    }
                }
            }

            boolean removido = false;
            String nomeUsuarioRemovido = null;

            // Procurar e remover o usuário
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = usuarios.get(i);
                if (u.getNomeUsuario().equals(nomeUsuario)) {
                    nomeUsuarioRemovido = u.getNome(); // Guardar nome para log
                    usuarios.remove(i);
                    removido = true;
                    System.out.println("Usuário removido da lista: " + u.getNomeUsuario() + " (" + u.getNome() + ")");
                    break;
                }
            }

            // Se o usuário foi encontrado e removido, salvar a lista
            if (removido) {
                System.out.println("Salvando lista de usuários atualizada após exclusão...");

                // Garantir que o arquivo exista
                File arquivo = new File(ARQUIVO_USUARIOS);
                if (!arquivo.exists()) {
                    arquivo.createNewFile();
                    System.out.println("Arquivo de usuários criado: " + arquivo.getAbsolutePath());
                }

                // Salvar a lista sem o usuário removido
                System.out.println("Salvando para o arquivo: " + arquivo.getAbsolutePath());
                try (Writer writer = new FileWriter(arquivo)) {
                    gson.toJson(usuarios, writer);
                    writer.flush(); // Garantir que os dados sejam gravados
                    System.out.println("Lista de usuários salva com sucesso após exclusão de " + nomeUsuarioRemovido);

                    // Verificar se a exclusão funcionou
                    listarUsuariosRegistrados();

                    return true;
                }
            } else {
                System.out.println("ERRO: Usuário " + nomeUsuario + " não encontrado na lista para exclusão.");
            }

            return false;
        } catch (IOException e) {
            System.err.println("Erro ao excluir usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para debug - lista todos os usuários registrados
     */
    public static void listarUsuariosRegistrados() {
        List<Usuario> usuarios = carregarUsuarios();
        System.out.println("===== USUÁRIOS REGISTRADOS =====");
        System.out.println("Total de usuários: " + usuarios.size());

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            System.out.println("Usuário #" + i + ": " +
                    "Nome=" + u.getNome() + ", " +
                    "Usuario=" + u.getNomeUsuario() + ", " +
                    "Email=" + u.getEmail() + ", " +
                    "Admin=" + u.isAdministrador());
        }
        System.out.println("================================");
    }
    /**
     * Busca todas as compras realizadas por um usuário específico.
     * @param nomeUsuario Nome do usuário cujas compras serão buscadas
     * @return Lista de compras do usuário, ou lista vazia se não houver
     */
    public static List<Compra> buscarComprasDoUsuario(String nomeUsuario) {
        if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
            System.out.println("AVISO: Tentativa de buscar compras com nome de usuário nulo ou vazio");
            return new ArrayList<>();
        }

        List<Compra> todasCompras = PersistenciaService.carregarCompras();
        List<Compra> comprasDoUsuario = new ArrayList<>();

        System.out.println("[DEBUG] Buscando compras para o usuário: " + nomeUsuario);
        System.out.println("[DEBUG] Total de compras no sistema: " + todasCompras.size());

        // Verificamos se o ID do usuário está associado à compra
        for (Compra compra : todasCompras) {
            String idUsuarioCompra = compra.getIdUsuario();
            System.out.println("[DEBUG] Analisando compra ID=" + compra.getId() + ", IdUsuario=" + idUsuarioCompra);

            // Verificar se o ID do usuário coincide com o nome de usuário fornecido
            if (idUsuarioCompra != null &&
                    (idUsuarioCompra.equals(nomeUsuario))) {

                comprasDoUsuario.add(compra);
                System.out.println("[DEBUG]   ✓ Compra ENCONTRADA para o usuário " + nomeUsuario);
                System.out.println("[DEBUG]     Data: " + compra.getDataHora());
                System.out.println("[DEBUG]     Sessão: " + compra.getIdSessao());
                System.out.println("[DEBUG]     Valor: " + compra.getPrecoTotal() + "€");
            } else {
                System.out.println("[DEBUG]   ✗ Compra não pertence ao usuário " + nomeUsuario);
            }
        }

        comprasDoUsuario.sort((c1, c2) -> c2.getDataHora().compareTo(c1.getDataHora())); // Ordena por data decrescente (mais recente primeiro)

        System.out.println("[DEBUG] Total de compras encontradas para o usuário: " + comprasDoUsuario.size());
        return comprasDoUsuario;
    }
}
