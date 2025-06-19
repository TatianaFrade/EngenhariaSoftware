package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class JanelaSelecaoFilme extends JPanel {
    private JButton btnProximo;
    private JButton btnVoltar;
    private JPanel filmesPanel;
    private Filme filmeSeleccionado;
    private List<JPanel> cartoes;

    // Constantes para a aparência dos cartões
    private static final Color BORDA_NORMAL = Color.GRAY;
    private static final Color BORDA_HOVER = new Color(100, 100, 100);
    private static final Color BORDA_SELECIONADO = new Color(0, 120, 215);
    private static final int LARGURA_BORDA_NORMAL = 1;
    private static final int LARGURA_BORDA_HOVER = 2;
    private static final int LARGURA_BORDA_SELECIONADO = 3;

    public JButton getBtnProximo() {
        return btnProximo;
    }

    public JButton getBtnVoltar() {
        return btnVoltar;
    }

    public JanelaSelecaoFilme(List<Filme> filmes, ActionListener onVoltar, ActionListener onProximo) {
        setLayout(new BorderLayout());
        cartoes = new ArrayList<>();

        // Configuração do painel de filmes com layout em grade
        configurarPainelFilmes(filmes);

        // Configuração dos botões de navegação
        configurarBotoes(onVoltar, onProximo);
    }

    private void configurarPainelFilmes(List<Filme> filmes) {
        filmesPanel = new JPanel();

        // Usar GridLayout com fileiras dinâmicas e 3 colunas
        int rows = (int) Math.ceil(filmes.size() / 3.0);
        filmesPanel.setLayout(new GridLayout(rows, 3, 20, 20));

        // Adicionar cartões para cada filme
        for (Filme filme : filmes) {
            JPanel cartao = criarCartaoFilme(filme);

            // Adiciona o evento de clique ao cartão
            cartao.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selecionarFilme(filme);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (filmeSeleccionado != filme) {
                        cartao.setBorder(BorderFactory.createLineBorder(BORDA_HOVER, LARGURA_BORDA_HOVER));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (filmeSeleccionado != filme) {
                        cartao.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
                    }
                }
            });

            filmesPanel.add(cartao);
            cartoes.add(cartao);
        }

        // Configurar painel com padding e scroll
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.add(filmesPanel, BorderLayout.CENTER);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(paddingPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll mais rápido
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Cria um painel que representa visualmente um filme como um cartão
     */
    private JPanel criarCartaoFilme(Filme filme) {
        JPanel cartao = new JPanel();
        // Armazenar o filme associado como uma propriedade do cartão
        cartao.putClientProperty("filme", filme);

        cartao.setPreferredSize(new Dimension(250, 180));
        cartao.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
        cartao.setLayout(new BorderLayout(5, 5));
        cartao.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Muda o cursor para indicar que é clicável

        // Área da imagem do filme
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(100, 140));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Exemplo: imgLabel.setIcon(new ImageIcon(filme.getImagemPath()));
        imgLabel.setText("[Imagem]");
        cartao.add(imgLabel, BorderLayout.WEST);

        // Área de informações do filme
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel nomeLabel = new JLabel("Nome: " + filme.getNome());
        nomeLabel.setFont(new Font(nomeLabel.getFont().getName(), Font.BOLD, 14));
        infoPanel.add(nomeLabel);

        JLabel tipoLabel = new JLabel("Tipo: " + (filme.isLegendado() ? "Legendado" : "Dublado"));
        infoPanel.add(tipoLabel);
        infoPanel.add(new JLabel("Lançamento: " + filme.getDataLancamento()));

        JLabel rateLabel = new JLabel("Rate: " + filme.getRate() + "/10");
        rateLabel.setForeground(filme.getRate() >= 7.0 ? new Color(0, 128, 0) : Color.BLACK);
        infoPanel.add(rateLabel);

        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cartao.add(infoPanel, BorderLayout.CENTER);

        return cartao;
    }

    private void configurarBotoes(ActionListener onVoltar, ActionListener onProximo) {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVoltar = new JButton("Voltar");
        btnProximo = new JButton("Próximo");

        // Adicionar listeners
        btnVoltar.addActionListener(onVoltar != null ? onVoltar : e -> {});
        btnProximo.addActionListener(onProximo != null ? onProximo : e -> {});

        // O botão Próximo começa desabilitado até que um filme seja selecionado
        btnProximo.setEnabled(false);

        bottomPanel.add(btnVoltar);
        bottomPanel.add(btnProximo);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public Filme getFilmeSeleccionado() {
        return filmeSeleccionado;
    }

    public void setFilmeSeleccionado(Filme filme) {
        this.filmeSeleccionado = filme;
        btnProximo.setEnabled(filme != null);
        atualizarSelecao();
    }

    private void selecionarFilme(Filme filme) {
        setFilmeSeleccionado(filme);
    }

    private void atualizarSelecao() {
        // Atualiza todos os cartões para refletir a seleção
        for (JPanel cartao : cartoes) {
            Filme filmeDoCartao = (Filme) cartao.getClientProperty("filme");
            boolean selecionado = filmeDoCartao == filmeSeleccionado;

            cartao.setBorder(BorderFactory.createLineBorder(
                    selecionado ? BORDA_SELECIONADO : BORDA_NORMAL,
                    selecionado ? LARGURA_BORDA_SELECIONADO : LARGURA_BORDA_NORMAL
            ));
        }
    }
}
