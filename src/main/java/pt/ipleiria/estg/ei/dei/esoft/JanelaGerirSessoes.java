package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

public class JanelaGerirSessoes extends JPanel {
    private JButton btnAdicionarSessao;
    private JButton btnVoltar;
    private JPanel sessoesPanel;

    // Constantes para a aparência dos cartões
    private static final Color BORDA_NORMAL = Color.GRAY;
    private static final Color BORDA_HOVER = new Color(100, 100, 100);
    private static final Color BORDA_SELECIONADO = new Color(0, 120, 215);
    private static final int LARGURA_BORDA_NORMAL = 1;
    private static final int LARGURA_BORDA_HOVER = 2;
    private static final int LARGURA_BORDA_SELECIONADO = 3;

    public JButton getBtnAdicionarSessao() {
        return btnAdicionarSessao;
    }

    public JButton getBtnVoltar() {
        return btnVoltar;
    }

    public JanelaGerirSessoes(List<Sessao> listaSessoes, ActionListener onVoltar, ActionListener onProximo, Consumer<Sessao> onAdicionarEditarSessao) {
        setLayout(new BorderLayout());

        configurarPainelSessoes(listaSessoes, onAdicionarEditarSessao);

        configurarBotoes(onVoltar, onProximo);
    }

    private void configurarPainelSessoes(List<Sessao> sessoes, Consumer<Sessao> onAdicionarEditarSessao) {
        sessoesPanel = new JPanel();

        // Usar GridLayout com fileiras dinâmicas e 3 colunas
        int rows = (int) Math.ceil(sessoes.size() / 3.0);
        sessoesPanel.setLayout(new GridLayout(rows, 3, 20, 20));

        for (Sessao sessao : sessoes) {
            JPanel cartao = criarCartaoSessao(sessao);

            // Adiciona o evento de clique ao cartão
            cartao.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onAdicionarEditarSessao.accept(sessao);
                }
            });

            sessoesPanel.add(cartao);
        }

        // Configurar painel com padding e scroll
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.add(sessoesPanel, BorderLayout.CENTER);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(paddingPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll mais rápido
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel criarCartaoSessao(Sessao sessao) {
        JPanel cartao = new JPanel();
        cartao.putClientProperty("sessao", sessao);

        cartao.setPreferredSize(new Dimension(250, 180));
        cartao.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
        cartao.setLayout(new BorderLayout(5, 5));
        cartao.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Muda o cursor para indicar que é clicável

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        int larguraMax = 200;
        String texto = String.format("<html><body style='width: %dpx'>%s</body></html>", larguraMax, sessao.toString());

        JLabel filmeLabel = new JLabel(texto);
        filmeLabel.setFont(new Font(filmeLabel.getFont().getName(), Font.BOLD, 14));
        infoPanel.add(filmeLabel);

        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cartao.add(infoPanel, BorderLayout.CENTER);

        return cartao;
    }

    private void configurarBotoes(ActionListener onVoltar, ActionListener onProximo) {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVoltar = new JButton("Voltar");
        btnAdicionarSessao = new JButton("Adicionar Sessao");

        // Adicionar listeners
        btnVoltar.addActionListener(onVoltar != null ? onVoltar : e -> {});
        btnAdicionarSessao.addActionListener(onProximo != null ? onProximo : e -> {});

        bottomPanel.add(btnVoltar);
        bottomPanel.add(btnAdicionarSessao);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
