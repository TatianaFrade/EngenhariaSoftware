package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

public class JanelaGerirSalas extends JPanel {
    private JButton btnAdicionarSala;
    private JButton btnVoltar;
    private JPanel salasPanel;

    // Constantes para a aparência dos cartões
    private static final Color BORDA_NORMAL = Color.GRAY;
    private static final Color BORDA_HOVER = new Color(100, 100, 100);
    private static final Color BORDA_SELECIONADO = new Color(0, 120, 215);
    private static final int LARGURA_BORDA_NORMAL = 1;
    private static final int LARGURA_BORDA_HOVER = 2;
    private static final int LARGURA_BORDA_SELECIONADO = 3;

    public JButton getBtnAdicionarSala() {
        return btnAdicionarSala;
    }

    public JButton getBtnVoltar() {
        return btnVoltar;
    }

    public JanelaGerirSalas(List<Sala> listaSalas, ActionListener onVoltar, ActionListener onProximo, Consumer<Sala> onAdicionarEditarSala) {
        setLayout(new BorderLayout());

        configurarPainelSalas(listaSalas, onAdicionarEditarSala);

        configurarBotoes(onVoltar, onProximo);
    }

    private void configurarPainelSalas(List<Sala> salas, Consumer<Sala> onAdicionarEditarSala) {
        salasPanel = new JPanel();

        // Usar GridLayout com fileiras dinâmicas e 3 colunas
        int rows = (int) Math.ceil(salas.size() / 3.0);
        salasPanel.setLayout(new GridLayout(rows, 3, 20, 20));

        for (Sala sala : salas) {
            JPanel cartao = criarCartaoSala(sala);

            // Adiciona o evento de clique ao cartão
            cartao.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onAdicionarEditarSala.accept(sala);
                }
            });

            salasPanel.add(cartao);
        }

        // Configurar painel com padding e scroll
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.add(salasPanel, BorderLayout.CENTER);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(paddingPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll mais rápido
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel criarCartaoSala(Sala sala) {
        JPanel cartao = new JPanel();
        cartao.putClientProperty("sala", sala);

        cartao.setPreferredSize(new Dimension(250, 180));
        cartao.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
        cartao.setLayout(new BorderLayout(5, 5));
        cartao.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Muda o cursor para indicar que é clicável

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel nomeLabel = new JLabel("Nome: " + sala.getNome());
        nomeLabel.setFont(new Font(nomeLabel.getFont().getName(), Font.BOLD, 14));
        infoPanel.add(nomeLabel);

        JLabel acessibilidadeLabel = new JLabel("Acessibilidade: " + sala.getAcessibilidade());
        infoPanel.add(acessibilidadeLabel);
        infoPanel.add(new JLabel("Total Lugares: " + sala.getTotalLugares()));

        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cartao.add(infoPanel, BorderLayout.CENTER);

        return cartao;
    }

    private void configurarBotoes(ActionListener onVoltar, ActionListener onProximo) {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVoltar = new JButton("Voltar");
        btnAdicionarSala = new JButton("Adicionar Sala");

        // Adicionar listeners
        btnVoltar.addActionListener(onVoltar != null ? onVoltar : e -> {});
        btnAdicionarSala.addActionListener(onProximo != null ? onProximo : e -> {});

        bottomPanel.add(btnVoltar);
        bottomPanel.add(btnAdicionarSala);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
