package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Janela para seleção de itens do bar
 */
public class JanelaSelecaoItensBar extends JPanel {
    private JButton btnProximo;
    private JButton btnVoltar;
    private JButton btnCancelar;
    private JPanel itensPanel;
    private List<Item> itensSelecionados;
    private List<JPanel> cartoes;
    private JLabel labelTotalValor;
    private double precoTotalItens;
    private double precoTotalCompra; // Preço total da compra (bilhete + itens)

    // Constantes para a aparência dos cartões
    private static final Color BORDA_NORMAL = Color.GRAY;
    private static final Color BORDA_HOVER = new Color(100, 100, 100);
    private static final Color BORDA_SELECIONADO = new Color(0, 120, 215);
    private static final int LARGURA_BORDA_NORMAL = 1;
    private static final int LARGURA_BORDA_HOVER = 2;
    private static final int LARGURA_BORDA_SELECIONADO = 3;

    /**
     * Construtor da janela de seleção de itens do bar
     *
     * @param sessao Sessão selecionada
     * @param lugar Lugar selecionado
     * @param precoInicial Preço total da compra do bilhete
     * @param onVoltar Listener para o botão voltar
     * @param onProximo Listener para o botão próximo
     */    public JanelaSelecaoItensBar(Sessao sessao, Lugar lugar, double precoInicial,
                                        ActionListener onVoltar, ActionListener onProximo, ActionListener onCancelar) {
        setLayout(new BorderLayout(10, 10));
        cartoes = new ArrayList<>();
        itensSelecionados = new ArrayList<>();
        this.precoTotalCompra = precoInicial;
        this.precoTotalItens = 0.0;

        // Configurar o painel de título
        configurarPainelTitulo(sessao, lugar);
        // Carregar itens do arquivo JSON e configurar o painel
        List<Item> itensDisponiveis = PersistenciaService.carregarItens();
        configurarPainelItens(itensDisponiveis.toArray(new Item[0]));

        // Configuração do painel com valor total
        configurarPainelValorTotal();
        // Configuração dos botões de navegação
        configurarBotoes(onVoltar, onProximo, onCancelar);
    }
    private void configurarPainelTitulo(Sessao sessao, Lugar lugar) {
        JPanel painelTitulo = new JPanel();
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Título diferente dependendo se é compra direta ou adicional ao bilhete
        String titulo = (sessao == null) ? "Compra de Itens do Bar" : "Adicionar Itens do Bar";
        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.BOLD, 18));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelTitulo);

        JLabel labelSubtitulo = new JLabel("Selecione os itens que deseja " +
                ((sessao == null) ? "comprar" : "adicionar à sua compra"));
        labelSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelSubtitulo);

        painelTitulo.add(Box.createVerticalStrut(10));

        // Informações do filme e lugar apenas se estiverem disponíveis
        if (sessao != null && lugar != null) {
            JLabel labelDetalhes = new JLabel(String.format(
                    "Filme: %s | Sessão: %s | Lugar: %s",
                    sessao.getFilme().getNome(),
                    sessao.getDataHoraFormatada(),
                    lugar.getIdentificacao()
            ));
            labelDetalhes.setAlignmentX(Component.CENTER_ALIGNMENT);
            painelTitulo.add(labelDetalhes);
        }

        // Instrução para seleção múltipla
        JLabel labelInstrucao = new JLabel("Clique nos itens para selecioná-los. Você pode selecionar múltiplos itens.");
        labelInstrucao.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelInstrucao.setFont(new Font(labelInstrucao.getFont().getName(), Font.ITALIC, 12));
        labelInstrucao.setForeground(new Color(100, 100, 100));

        painelTitulo.add(Box.createVerticalStrut(5));
        painelTitulo.add(labelInstrucao);

        add(painelTitulo, BorderLayout.NORTH);
    }

    private void configurarPainelItens(Item[] itens) {
        itensPanel = new JPanel();

        // Usar GridLayout com fileiras dinâmicas e 3 colunas
        int rows = (int) Math.ceil(itens.length / 3.0);
        itensPanel.setLayout(new GridLayout(rows, 3, 15, 15));

        // Adicionar cartões para cada item
        for (Item item : itens) {
            if (item.isDisponivel()) {
                if (item.getQuantidade() > 0 || item.getCategoria().equals("Combo")) {
                    JPanel cartao = criarCartaoItem(item);
                    itensPanel.add(cartao);
                    cartoes.add(cartao);
                }
            }
        }

        // Configurar painel com padding e scroll
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.add(itensPanel, BorderLayout.CENTER);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(paddingPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll mais rápido
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Cria um painel que representa visualmente um item como um cartão
     */
    private JPanel criarCartaoItem(Item item) {
        JPanel cartao = new JPanel();
        // Armazenar o item associado como uma propriedade do cartão
        cartao.putClientProperty("item", item);

        cartao.setPreferredSize(new Dimension(220, 140));
        cartao.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
        cartao.setLayout(new BorderLayout(5, 5));
        cartao.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Muda o cursor para indicar que é clicável

        // Ícone que representa a categoria do item
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(50, 50));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Definir um ícone baseado na categoria
        String icone = "🍿"; // Padrão para comidas
        if (item.getCategoria().equals("Bebida")) {
            icone = "🥤";
        } else if (item.getCategoria().equals("Combo")) {
            icone = "🍔";
        }

        imgLabel.setText(icone);
        imgLabel.setFont(new Font("Dialog", Font.PLAIN, 30));

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.add(imgLabel);
        cartao.add(iconPanel, BorderLayout.WEST);

        // Área de informações do item
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel nomeLabel = new JLabel(item.getNome());
        nomeLabel.setFont(new Font(nomeLabel.getFont().getName(), Font.BOLD, 14));
        nomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nomeLabel);

        infoPanel.add(Box.createVerticalStrut(3));

        JLabel descLabel = new JLabel("<html><div style='width:120px'>" + item.getDescricao() + "</div></html>");
        descLabel.setFont(new Font(descLabel.getFont().getName(), Font.PLAIN, 11));
        descLabel.setForeground(new Color(80, 80, 80));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descLabel);

        infoPanel.add(Box.createVerticalStrut(5));

        JLabel categoriaLabel = new JLabel(item.getCategoria());
        categoriaLabel.setFont(new Font(categoriaLabel.getFont().getName(), Font.ITALIC, 11));
        categoriaLabel.setForeground(new Color(100, 100, 100));
        categoriaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(categoriaLabel);

        infoPanel.add(Box.createVerticalStrut(8));

        JLabel precoLabel = new JLabel(String.format("%.2f €", item.getPreco()));
        precoLabel.setFont(new Font(precoLabel.getFont().getName(), Font.BOLD, 14));
        precoLabel.setForeground(new Color(0, 100, 0));
        precoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(precoLabel);

        cartao.add(infoPanel, BorderLayout.CENTER);

        // Adicionar evento de clique para seleção do item
        cartao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                alternarSelecaoItem(item, cartao);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!itensSelecionados.contains(item)) {
                    cartao.setBorder(BorderFactory.createLineBorder(BORDA_HOVER, LARGURA_BORDA_HOVER));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!itensSelecionados.contains(item)) {
                    cartao.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
                }
            }
        });

        return cartao;
    }

    private void alternarSelecaoItem(Item item, JPanel cartao) {
        if (itensSelecionados.contains(item)) {
            // Remover item da seleção
            itensSelecionados.remove(item);
            precoTotalItens -= item.getPreco();
            cartao.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
        } else {
            // Adicionar item à seleção
            itensSelecionados.add(item);
            precoTotalItens += item.getPreco();
            cartao.setBorder(BorderFactory.createLineBorder(BORDA_SELECIONADO, LARGURA_BORDA_SELECIONADO));
        }

        // Atualizar o valor total
        atualizarValorTotal();
    }

    private void configurarPainelValorTotal() {
        JPanel painelValor = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelValor.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 30));

        JPanel subPainel = new JPanel(new GridLayout(2, 2, 10, 5));

        // Valor dos itens selecionados
        JLabel labelItensTitulo = new JLabel("Itens do bar:");
        labelItensTitulo.setFont(new Font(labelItensTitulo.getFont().getName(), Font.BOLD, 14));
        labelItensTitulo.setHorizontalAlignment(SwingConstants.RIGHT);

        DecimalFormat df = new DecimalFormat("0.00 €");
        JLabel labelItensValor = new JLabel(df.format(precoTotalItens));
        labelItensValor.setForeground(new Color(0, 100, 0));

        // Valor total da compra (bilhete + itens)
        JLabel labelTotalTitulo = new JLabel("TOTAL:");
        labelTotalTitulo.setFont(new Font(labelTotalTitulo.getFont().getName(), Font.BOLD, 16));
        labelTotalTitulo.setHorizontalAlignment(SwingConstants.RIGHT);

        labelTotalValor = new JLabel(df.format(precoTotalCompra + precoTotalItens));
        labelTotalValor.setFont(new Font(labelTotalValor.getFont().getName(), Font.BOLD, 16));

        subPainel.add(labelItensTitulo);
        subPainel.add(labelItensValor);
        subPainel.add(labelTotalTitulo);
        subPainel.add(labelTotalValor);

        painelValor.add(subPainel);
        add(painelValor, BorderLayout.SOUTH);
    }

    private void atualizarValorTotal() {
        DecimalFormat df = new DecimalFormat("0.00 €");
        labelTotalValor.setText(df.format(precoTotalCompra + precoTotalItens));
    }
    private void configurarBotoes(ActionListener onVoltar, ActionListener onProximo, ActionListener onCancelar) {
        // Botão Cancelar à esquerda
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(onCancelar != null ? onCancelar : e -> {});

        // Botões de navegação à direita
        btnVoltar = new JButton("Voltar");
        btnProximo = new JButton("Próximo");

        // Adicionar listeners
        btnVoltar.addActionListener(onVoltar != null ? onVoltar : e -> {});
        btnProximo.addActionListener(onProximo != null ? onProximo : e -> {});

        // Layout para posicionar botões (Cancelar à esquerda, Voltar e Próximo à direita)
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtonPanel.add(btnCancelar);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(btnVoltar);
        rightButtonPanel.add(btnProximo);

        // Painel para organizar os dois grupos de botões
        JPanel navigationPanel = new JPanel(new BorderLayout());
        navigationPanel.add(leftButtonPanel, BorderLayout.WEST);
        navigationPanel.add(rightButtonPanel, BorderLayout.EAST);

        add(navigationPanel, BorderLayout.SOUTH);
    }

    // Getters
    public JButton getBtnProximo() {
        return btnProximo;
    }
    public JButton getBtnVoltar() {
        return btnVoltar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public List<Item> getItensSelecionados() {
        return itensSelecionados;
    }

    public double getPrecoTotalItens() {
        return precoTotalItens;
    }

    public double getPrecoTotal() {
        return precoTotalCompra + precoTotalItens;
    }
}
