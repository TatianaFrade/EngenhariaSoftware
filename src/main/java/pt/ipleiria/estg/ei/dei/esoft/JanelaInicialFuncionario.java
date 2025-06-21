package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JanelaInicialFuncionario extends JFrame {
    private JPanel painelInicialFuncionario;
    private JButton btnPOS;
    private JButton btnStock;
    private JButton btnGerirFilmes;
    private JButton btnGerirSessoes;
    private JButton btnGerirMenus;
    private JButton btnGerirSalas;
    private JButton btnCriarRelatorioCinema;
    private JButton btnCriarRelatorioProdutos;
    private JButton loginButton;
    private List<Item> itens;
    private List<Encomenda> encomendas;
    private List<Filme> filmes;
    private List<Sessao> sessoes;
    private RepositorioFilmes repositorioFilmes;
    private JFrame menuPrincipal;
    public JanelaInicialFuncionario(String title, JFrame menuPrincipal) {
        super(title);
        repositorioFilmes = new RepositorioFilmes();
        this.menuPrincipal = menuPrincipal != null ? menuPrincipal : new JanelaPrincipal(title);

        inicializarDados();
        criarPainelInicialFuncionario();
        setContentPane(painelInicialFuncionario);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
    }    /**
     * Inicializa os dados da aplicação, garantindo persistência dos lugares ocupados.
     *
     * Fluxo de persistência:
     * 1. Tenta carregar dados salvos (filmes, salas, sessões e compras)
     * 2. Se existirem sessões salvas, as utiliza mantendo os lugares já ocupados
     * 3. Se não existirem, cria sessões com valores padrão
     * 4. Restaura lugares ocupados com base nas compras confirmadas
     * 5. Salva as sessões atualizadas para garantir persistência
     */
    private void inicializarDados() {
        try {
            itens = PersistenciaService.carregarItens();
            encomendas = PersistenciaService.carregarEncomendas();
            filmes = new ArrayList<>();
            repositorioFilmes = new RepositorioFilmes();

            List<Filme> filmesCarregados = PersistenciaService.carregarFilmes();
            List<Sala> salas = PersistenciaService.carregarSalas();
            List<Compra> compras = PersistenciaService.carregarCompras();

            if (filmesCarregados == null || filmesCarregados.isEmpty()) {
                filmesCarregados = Arrays.asList(
                        new Filme("Matrix", true, "1999-03-31", 8.7, null),
                        new Filme("O Rei Leão", false, "1994-06-24", 8.5, null),
                        new Filme("Interestelar", true, "2014-11-06", 8.6, null),
                        new Filme("Vingadores: Ultimato", false, "2019-04-26", 8.4, null),
                        new Filme("Cidade de Deus", true, "2002-08-30", 8.6, null),
                        new Filme("O Auto da Compadecida", false, "2000-09-15", 8.8, null),
                        new Filme("Pantera Negra", true, "2018-02-15", 7.3, null),
                        new Filme("La La Land", true, "2016-12-16", 8.0, null),
                        new Filme("Bacurau", false, "2019-08-23", 7.7, null)
                );
                PersistenciaService.salvarFilmes(filmesCarregados);
            }

            for (Filme f : filmesCarregados) {
                repositorioFilmes.adicionar(f);
            }

            filmes = repositorioFilmes.getFilmes();

            List<Sala> salasList;
            if (salas == null || salas.isEmpty()) {
                salasList = new ArrayList<>();
                salasList.add(new Sala("Sala 1", "sim", 8, 10));
                salasList.add(new Sala("Sala 2", "sim", 8, 10));
                salasList.add(new Sala("Sala 3", "nao", 8, 10));
                PersistenciaService.salvarSalas(salasList);
            } else {
                salasList = salas;
            }

            List<Sessao> sessoesExistentes = PersistenciaService.carregarSessoes();

            if (sessoesExistentes != null && !sessoesExistentes.isEmpty()) {
                sessoes = sessoesExistentes;
            } else {
                sessoes = new ArrayList<>();
                sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(1).withHour(14).withMinute(30), salasList.get(0), 7.50));
                sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(1).withHour(18).withMinute(0), salasList.get(0), 9.00));
                sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(2).withHour(16).withMinute(45), salasList.get(2), 7.50));

                sessoes.add(new Sessao(filmes.get(1), LocalDateTime.now().plusDays(1).withHour(15).withMinute(0), salasList.get(1), 7.50));
                sessoes.add(new Sessao(filmes.get(1), LocalDateTime.now().plusDays(2).withHour(14).withMinute(0), salasList.get(1), 7.00));

                sessoes.add(new Sessao(filmes.get(2), LocalDateTime.now().plusDays(1).withHour(20).withMinute(30), salasList.get(2), 9.00));
                sessoes.add(new Sessao(filmes.get(2), LocalDateTime.now().plusDays(3).withHour(19).withMinute(15), salasList.get(0), 9.00));

                sessoes.add(new Sessao(filmes.get(3), LocalDateTime.now().plusDays(2).withHour(15).withMinute(0), salasList.get(0), 8.00));
                sessoes.add(new Sessao(filmes.get(3), LocalDateTime.now().plusDays(2).withHour(21).withMinute(30), salasList.get(2), 9.50));

                sessoes.add(new Sessao(filmes.get(5), LocalDateTime.now().plusDays(1).withHour(16).withMinute(0), salasList.get(1), 7.50));
                sessoes.add(new Sessao(filmes.get(5), LocalDateTime.now().plusDays(3).withHour(18).withMinute(30), salasList.get(1), 8.50));

                sessoes.add(new Sessao(filmes.get(7), LocalDateTime.now().plusDays(4).withHour(17).withMinute(45), salasList.get(1), 8.00));

                PersistenciaService.salvarSessoes(sessoes);
            }

        } catch (Exception e) {
            inicializarDadosPadrao();
        }
    }

    private void criarPainelInicialFuncionario() {
        painelInicialFuncionario = new JPanel();
        painelInicialFuncionario.setLayout(new BorderLayout());

        // Painel superior com título e login
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        // Título para o cinema com estilo neutro
        JLabel titulo = new JLabel("Cinema e Bar");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        // Botão de login com estilo neutro
        loginButton = new JButton("Log out");
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.addActionListener(e -> abrirMenuPrincipal());
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginPanel.add(loginButton);

        topPanel.add(titulo, BorderLayout.CENTER);
        topPanel.add(loginPanel, BorderLayout.EAST);

        // Adiciona o painel superior
        painelInicialFuncionario.add(topPanel, BorderLayout.NORTH);
        // Criar botões com estilo neutro
        btnPOS = createStyledButton("POS", null);
        btnStock = createStyledButton("Stock", null);
        btnGerirFilmes = createStyledButton("Gerir Filmes", null);
        btnGerirSessoes = createStyledButton("Gerir Sessões", null);
        btnGerirMenus = createStyledButton("Gerir Menus", null);
        btnGerirSalas = createStyledButton("Gerir Salas", null);
        btnCriarRelatorioCinema = createStyledButton("Criar Relatório de vendas cinema", null);
        btnCriarRelatorioProdutos = createStyledButton("Criar Relatório de vendas produtos", null);

        // Painel central com os botões principais dispostos em grid
        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 20, 20));
        centerPanel.add(btnPOS);
        centerPanel.add(btnStock);
        centerPanel.add(btnGerirFilmes);
        centerPanel.add(btnGerirSessoes);
        centerPanel.add(btnGerirMenus);
        centerPanel.add(btnGerirSalas);
        centerPanel.add(btnCriarRelatorioCinema);
        centerPanel.add(btnCriarRelatorioProdutos);

        // Adiciona espaçamento ao redor do painel central
        JPanel paddedCenterPanel = new JPanel(new BorderLayout());
        paddedCenterPanel.add(centerPanel, BorderLayout.CENTER);
        paddedCenterPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        painelInicialFuncionario.add(paddedCenterPanel, BorderLayout.CENTER);
        // Adiciona um rodapé simples
        JPanel footerPanel = new JPanel(new BorderLayout());
        JLabel footerLabel = new JLabel("Cinema e Bar");
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(footerLabel, BorderLayout.CENTER);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        painelInicialFuncionario.add(footerPanel, BorderLayout.SOUTH);

        btnGerirFilmes.addActionListener(e -> mostrarJanelaGerirFilmes());
        btnStock.addActionListener(e -> mostrarJanelaStock());
    }

    private void abrirMenuPrincipal() {
        menuPrincipal.setVisible(true);
        dispose();
    }

    private void mostrarJanelaStock() {
        // Criar o painel de seleção de filme
        JanelaStock painelStock = new JanelaStock(itens, encomendas);

        // Adicionar listener para o botão Voltar
        painelStock.getBtnVoltar().addActionListener(e -> voltarParaPainelPrincipal());

        trocarPainel(painelStock.getPanelPrincipal());
    }

    private void mostrarJanelaGerirFilmes() {
        // Criar o painel de seleção de filme
        JanelaGerirFilmes painelFilmes = new JanelaGerirFilmes(filmes, null, null, this::mostrarJanelaAdicionarEditarEliminarFilme);

        // Adicionar listener para o botão Voltar
        painelFilmes.getBtnVoltar().addActionListener(e -> voltarParaPainelPrincipal());

        // Adicionar listener para o botão Próximo
        painelFilmes.getBtnAdicionarFilme().addActionListener(e -> {
            mostrarJanelaAdicionarEditarEliminarFilme(null);
        });

        trocarPainel(painelFilmes);
    }

    private void mostrarJanelaAdicionarEditarEliminarFilme(Filme filme) {
        JanelaAdicionarEditarEliminarFilme painel = new JanelaAdicionarEditarEliminarFilme(
                filme,
                (filmeOriginal, filmeNovo) -> salvarOuAtualizarFilme(filmeOriginal, filmeNovo),
                e -> mostrarJanelaGerirFilmes(),
                e -> eliminarFilme(filme)
        );

        trocarPainel(painel);
    }

    private void eliminarFilme(Filme filme) {
        if (filme == null) {
            JOptionPane.showMessageDialog(null, "Filme inválido para eliminar!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "Tem certeza que deseja eliminar o filme: " + filme.getNome() + "?",
                "Confirmar Eliminação",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean removed = filmes.remove(filme);
            if (removed) {
                JOptionPane.showMessageDialog(null, "Filme eliminado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                mostrarJanelaGerirFilmes(); // Atualiza a interface
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao eliminar o filme.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvarOuAtualizarFilme(Filme filmeSelecionado, Filme novoFilme) {
        if (filmeSelecionado == null) {
            repositorioFilmes.adicionar(novoFilme);
        } else {
            // Ideally update in repo, for now just update filmeSelecionado properties
            filmeSelecionado.atualizar(
                    novoFilme.getNome(),
                    novoFilme.isLegendado(),
                    novoFilme.getDataLancamento(),
                    novoFilme.getRate(),
                    novoFilme.getImagemPath()
            );
        }
        PersistenciaService.salvarFilmes(repositorioFilmes.getFilmes()); // Save changes
        mostrarJanelaGerirFilmes();
    }


    private void voltarParaPainelPrincipal() {
        trocarPainel(painelInicialFuncionario);
    }    private void trocarPainel(JPanel novoPainel) {
        setContentPane(novoPainel);

        // Usar um tamanho consistente para todas as janelas
        // Um tamanho maior é usado para acomodar todas as telas, incluindo a de pagamento
        setSize(900, 650);

        setLocationRelativeTo(null); // Centraliza novamente após redimensionar
        revalidate();
        repaint();
    }
    /**
     * Cria um botão estilizado para o menu principal
     * @param texto Texto do botão
     * @param cor Cor de fundo do botão (não utilizada para manter estilo neutro)
     * @return JButton estilizado
     */
    private JButton createStyledButton(String texto, Color cor) {
        JButton button = new JButton(texto);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        // Usar estilo padrão do sistema para os botões (neutro)
        button.setFocusPainted(true);
        button.setBorderPainted(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 100));

        // Adiciona margem interna ao texto
        button.setMargin(new Insets(10, 10, 10, 10));

        return button;
    }

    // Método para inicializar dados padrão quando não há persistência
    private void inicializarDadosPadrao() {
        // Criar filmes padrão
        filmes = Arrays.asList(
                new Filme("Matrix", true, "1999-03-31", 8.7, null),
                new Filme("O Rei Leão", false, "1994-06-24", 8.5, null),
                new Filme("Interestelar", true, "2014-11-06", 8.6, null),
                new Filme("Vingadores: Ultimato", false, "2019-04-26", 8.4, null),
                new Filme("Cidade de Deus", true, "2002-08-30", 8.6, null),
                new Filme("O Auto da Compadecida", false, "2000-09-15", 8.8, null),
                new Filme("Pantera Negra", true, "2018-02-15", 7.3, null),
                new Filme("La La Land", true, "2016-12-16", 8.0, null),
                new Filme("Bacurau", false, "2019-08-23", 7.7, null)
        );

        // Criar salas padrão
        Sala sala1 = new Sala("Sala 1", "sim", 8, 10);
        Sala sala2 = new Sala("Sala 2", "sim", 8, 10);
        Sala sala3 = new Sala("Sala 3", "nao", 8, 10);

        // Criar sessões padrão (vários horários para diferentes filmes)
        sessoes = new ArrayList<>();

        // Sessões para Matrix
        sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(1).withHour(14).withMinute(30), sala1, 7.50));
        sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(1).withHour(18).withMinute(0), sala1, 9.00));
        sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(2).withHour(16).withMinute(45), sala3, 7.50));

        // Sessões para O Rei Leão
        sessoes.add(new Sessao(filmes.get(1), LocalDateTime.now().plusDays(1).withHour(15).withMinute(0), sala2, 7.50));
        sessoes.add(new Sessao(filmes.get(1), LocalDateTime.now().plusDays(2).withHour(14).withMinute(0), sala2, 7.00));

        // Sessões para Interestelar
        sessoes.add(new Sessao(filmes.get(2), LocalDateTime.now().plusDays(1).withHour(20).withMinute(30), sala3, 9.00));
        sessoes.add(new Sessao(filmes.get(2), LocalDateTime.now().plusDays(3).withHour(19).withMinute(15), sala1, 9.00));

        // Sessões para Vingadores
        sessoes.add(new Sessao(filmes.get(3), LocalDateTime.now().plusDays(2).withHour(15).withMinute(0), sala1, 8.00));
        sessoes.add(new Sessao(filmes.get(3), LocalDateTime.now().plusDays(2).withHour(21).withMinute(30), sala3, 9.50));

        // Sessões para O Auto da Compadecida
        sessoes.add(new Sessao(filmes.get(5), LocalDateTime.now().plusDays(1).withHour(16).withMinute(0), sala2, 7.50));
        sessoes.add(new Sessao(filmes.get(5), LocalDateTime.now().plusDays(3).withHour(18).withMinute(30), sala2, 8.50));

        // Sessões para La La Land
        sessoes.add(new Sessao(filmes.get(7), LocalDateTime.now().plusDays(4).withHour(17).withMinute(45), sala2, 8.00));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JanelaInicialFuncionario("Cinema e Bar", null).setVisible(true);
        });
    }
}
