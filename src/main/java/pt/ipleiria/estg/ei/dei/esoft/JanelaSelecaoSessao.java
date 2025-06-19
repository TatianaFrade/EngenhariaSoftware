package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JanelaSelecaoSessao extends JPanel {
    private JButton btnProximo;
    private JButton btnVoltar;
    private JPanel sessoesPanel;
    private Sessao sessaoSeleccionada;
    private Filme filme;
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

    public JanelaSelecaoSessao(Filme filme, List<Sessao> todasSessoes, ActionListener onVoltar, ActionListener onProximo) {
        this.filme = filme;
        this.cartoes = new ArrayList<>();
        setLayout(new BorderLayout());

        // Configurar título
        configurarTitulo();

        // Configurar painel de sessões
        configurarPainelSessoes(todasSessoes);

        // Configurar botões de navegação
        configurarBotoes(onVoltar, onProximo);
    }

    private void configurarTitulo() {
        JPanel tituloPanel = new JPanel();
        JLabel tituloLabel = new JLabel("Sessões disponíveis para: " + filme.getNome());
        tituloLabel.setFont(new Font(tituloLabel.getFont().getName(), Font.BOLD, 16));
        tituloPanel.add(tituloLabel);
        add(tituloPanel, BorderLayout.NORTH);
    }

    private void configurarPainelSessoes(List<Sessao> todasSessoes) {
        // Filtra apenas as sessões do filme selecionado
        List<Sessao> sessoesFiltradas = todasSessoes.stream()
                .filter(s -> s.getFilme().getNome().equals(filme.getNome()))
                .collect(Collectors.toList());

        sessoesPanel = new JPanel();

        // Usar GridLayout com fileiras dinâmicas e 3 colunas, com margens entre as células
        int rows = (int) Math.ceil(sessoesFiltradas.size() / 3.0);
        sessoesPanel.setLayout(new GridLayout(rows > 0 ? rows : 1, 3, 20, 20));

        // Adicionar cartões para cada sessão
        for (Sessao sessao : sessoesFiltradas) {
            JPanel cartao = criarCartaoSessao(sessao);

            // Adiciona o evento de clique ao cartão
            cartao.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selecionarSessao(sessao);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (sessaoSeleccionada != sessao) {
                        cartao.setBorder(BorderFactory.createLineBorder(BORDA_HOVER, LARGURA_BORDA_HOVER));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (sessaoSeleccionada != sessao) {
                        cartao.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
                    }
                }
            });

            sessoesPanel.add(cartao);
            cartoes.add(cartao);
        }

        // Caso não tenha sessões disponíveis
        if (sessoesFiltradas.isEmpty()) {
            JLabel semSessoes = new JLabel("Não há sessões disponíveis para este filme.");
            semSessoes.setHorizontalAlignment(SwingConstants.CENTER);
            sessoesPanel.add(semSessoes);
        }

        // Adiciona o painel de sessões a um painel com padding
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.add(sessoesPanel, BorderLayout.CENTER);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Adiciona o painel com scroll
        JScrollPane scrollPane = new JScrollPane(paddingPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll mais rápido
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Cria um painel que representa visualmente uma sessão como um cartão
     */
    private JPanel criarCartaoSessao(Sessao sessao) {
        JPanel cartao = new JPanel();
        // Armazenar a sessão associada como uma propriedade do cartão
        cartao.putClientProperty("sessao", sessao);

        cartao.setPreferredSize(new Dimension(250, 150));
        cartao.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
        cartao.setLayout(new BorderLayout(5, 5));
        cartao.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Muda o cursor para indicar que é clicável

        // Painel central com as informações da sessão
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        // Adiciona espaçamento no topo
        infoPanel.add(Box.createVerticalStrut(10));

        // Data e hora da sessão
        JLabel dataHoraLabel = new JLabel("Data/Hora: " + sessao.getDataHoraFormatada());
        dataHoraLabel.setFont(new Font(dataHoraLabel.getFont().getName(), Font.BOLD, 14));
        dataHoraLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(dataHoraLabel);

        infoPanel.add(Box.createVerticalStrut(10));

        // Sala
        JLabel salaLabel = new JLabel("Sala: " + sessao.getNomeSala() +
                (sessao.getSala().getAcessibilidade().equals("sim") ? " (Acessível)" : ""));
        salaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(salaLabel);

        infoPanel.add(Box.createVerticalStrut(10));

        // Preço
        JLabel precoLabel = new JLabel(String.format("Preço: %.2f €", sessao.getPreco()));
        precoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(precoLabel);

        // Lugares disponíveis
        JLabel lugaresLabel = new JLabel(String.format("Lugares: %d disponíveis",
                sessao.getSala().getLugaresDisponiveis()));
        lugaresLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(lugaresLabel);

        // Centraliza o painel de informações
        JPanel centeredPanel = new JPanel(new BorderLayout());
        centeredPanel.add(infoPanel, BorderLayout.CENTER);

        cartao.add(centeredPanel, BorderLayout.CENTER);

        return cartao;
    }

    private void configurarBotoes(ActionListener onVoltar, ActionListener onProximo) {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVoltar = new JButton("Voltar");
        btnProximo = new JButton("Próximo");

        // Adicionar listeners
        btnVoltar.addActionListener(onVoltar != null ? onVoltar : e -> {});
        btnProximo.addActionListener(onProximo != null ? onProximo : e -> {});

        // O botão Próximo começa desabilitado até que uma sessão seja selecionada
        btnProximo.setEnabled(false);

        bottomPanel.add(btnVoltar);
        bottomPanel.add(btnProximo);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public Sessao getSessaoSeleccionada() {
        return sessaoSeleccionada;
    }

    public void setSessaoSeleccionada(Sessao sessao) {
        this.sessaoSeleccionada = sessao;
        btnProximo.setEnabled(sessao != null);
        atualizarSelecao();
    }

    private void selecionarSessao(Sessao sessao) {
        setSessaoSeleccionada(sessao);
    }

    private void atualizarSelecao() {
        // Atualiza todos os cartões para refletir a seleção
        for (JPanel cartao : cartoes) {
            Sessao sessaoDoCartao = (Sessao) cartao.getClientProperty("sessao");
            boolean selecionado = sessaoDoCartao == sessaoSeleccionada;

            cartao.setBorder(BorderFactory.createLineBorder(
                    selecionado ? BORDA_SELECIONADO : BORDA_NORMAL,
                    selecionado ? LARGURA_BORDA_SELECIONADO : LARGURA_BORDA_NORMAL
            ));
        }
    }

    public Filme getFilme() {
        return filme;
    }
}
