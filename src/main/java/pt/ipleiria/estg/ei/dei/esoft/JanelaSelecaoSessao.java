package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class JanelaSelecaoSessao extends JPanel {
    private JButton btnProximo;
    private JButton btnVoltar;
    private JPanel sessoesPanel;
    private Sessao sessaoSeleccionada;
    private Filme filme;

    // Getters para os botões
    public JButton getBtnProximo() {
        return btnProximo;
    }

    public JButton getBtnVoltar() {
        return btnVoltar;
    }

    public JanelaSelecaoSessao(Filme filme, List<Sessao> todasSessoes, ActionListener onVoltar, ActionListener onProximo) {
        this.filme = filme;
        setLayout(new BorderLayout());

        // Filtra apenas as sessões do filme selecionado
        List<Sessao> sessoesFiltradas = todasSessoes.stream()
                .filter(s -> s.getFilme().getNome().equals(filme.getNome()))
                .collect(Collectors.toList());

        // Título para mostrar qual filme foi selecionado
        JPanel tituloPanel = new JPanel();
        JLabel tituloLabel = new JLabel("Sessões disponíveis para: " + filme.getNome());
        tituloLabel.setFont(new Font(tituloLabel.getFont().getName(), Font.BOLD, 16));
        tituloPanel.add(tituloLabel);
        add(tituloPanel, BorderLayout.NORTH);

        sessoesPanel = new JPanel();
        // Usar GridLayout com fileiras dinâmicas e 3 colunas, com margens entre as células
        int rows = (int) Math.ceil(sessoesFiltradas.size() / 3.0);
        sessoesPanel.setLayout(new GridLayout(rows > 0 ? rows : 1, 3, 20, 20));

        for (Sessao sessao : sessoesFiltradas) {
            CartaoSessao cartao = new CartaoSessao(sessao);
            // Adiciona o evento de clique ao cartão
            cartao.addMouseListener(new SessaoMouseListener(sessao, this));
            sessoesPanel.add(cartao);
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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVoltar = new JButton("Voltar");
        btnProximo = new JButton("Próximo");
        btnVoltar.addActionListener(onVoltar);
        btnProximo.addActionListener(onProximo);

        // Desabilita o botão Próximo até que uma sessão seja selecionada
        btnProximo.setEnabled(false);
        bottomPanel.add(btnVoltar);
        bottomPanel.add(btnProximo);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Método para obter a sessão selecionada
    public Sessao getSessaoSeleccionada() {
        return sessaoSeleccionada;
    }

    // Método para definir a sessão selecionada
    public void setSessaoSeleccionada(Sessao sessao) {
        this.sessaoSeleccionada = sessao;
        // Habilita o botão Próximo quando uma sessão for selecionada
        btnProximo.setEnabled(sessao != null);
    }

    // Método para obter o filme desta seleção
    public Filme getFilme() {
        return filme;
    }
}

// Classe para o cartão de sessão
class CartaoSessao extends JPanel {
    private Sessao sessao;

    public CartaoSessao(Sessao sessao) {
        this.sessao = sessao;
        setPreferredSize(new Dimension(250, 150));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setLayout(new BorderLayout(5, 5));
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Muda o cursor para indicar que é clicável

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
        JLabel salaLabel = new JLabel("Sala: " + sessao.getSala());
        salaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(salaLabel);

        infoPanel.add(Box.createVerticalStrut(10));

        // Preço
        JLabel precoLabel = new JLabel(String.format("Preço: %.2f €", sessao.getPreco()));
        precoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(precoLabel);

        // Centraliza o painel de informações
        JPanel centeredPanel = new JPanel(new BorderLayout());
        centeredPanel.add(infoPanel, BorderLayout.CENTER);

        add(centeredPanel, BorderLayout.CENTER);
    }

    public Sessao getSessao() {
        return sessao;
    }
}

// Classe para lidar com eventos de mouse nos cartões de sessão
class SessaoMouseListener extends MouseAdapter {
    private Sessao sessao;
    private JanelaSelecaoSessao janela;

    public SessaoMouseListener(Sessao sessao, JanelaSelecaoSessao janela) {
        this.sessao = sessao;
        this.janela = janela;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Define a sessão selecionada na janela
        janela.setSessaoSeleccionada(sessao);

        // Atualiza todos os cartões para mostrar qual está selecionado
        Component[] components = janela.getComponents();
        for (Component component : components) {
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                JViewport viewport = scrollPane.getViewport();
                Component viewComp = viewport.getView();
                if (viewComp instanceof JPanel) {
                    JPanel paddingPanel = (JPanel) viewComp;
                    for (Component c : paddingPanel.getComponents()) {
                        if (c instanceof JPanel) {
                            JPanel sessoesPanel = (JPanel) c;
                            for (Component cartao : sessoesPanel.getComponents()) {
                                if (cartao instanceof CartaoSessao) {
                                    CartaoSessao cs = (CartaoSessao) cartao;
                                    if (cs.getSessao() == sessao) {
                                        cs.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 3));
                                    } else {
                                        cs.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Muda a aparência do cartão quando o mouse passa por cima (somente se não for o selecionado)
        Component component = e.getComponent();
        if (component instanceof CartaoSessao) {
            CartaoSessao cartao = (CartaoSessao) component;
            if (janela.getSessaoSeleccionada() != cartao.getSessao()) {
                cartao.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Restaura a aparência original quando o mouse sai (somente se não for o selecionado)
        Component component = e.getComponent();
        if (component instanceof CartaoSessao) {
            CartaoSessao cartao = (CartaoSessao) component;
            if (janela.getSessaoSeleccionada() != cartao.getSessao()) {
                cartao.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            }
        }
    }
}
