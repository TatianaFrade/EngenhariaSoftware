package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class JanelaStock extends JFrame {
    private JPanel panelStock;
    private JPanel panelItems;
    private JPanel panelDireita;
    private JPanel panelDetalhes;
    private JPanel panelDetalhesProduto;
    private JButton buttonVoltar;
    private JButton buttonEncomendar;
    private JButton buttonGerirEncomendas;
    private JButton buttonAdicionarItem;

    JScrollPane scrollPane;

    private final List<ItemCartao> itemCartoes;
    private Item itemSelecionado;

    public JanelaStock(List<Item> itens, List<Encomenda> encomendas) {
        setTitle("Stock");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panelStock);
        pack();
        setLocationRelativeTo(null);

        itemCartoes = new ArrayList<ItemCartao>();

        buttonEncomendar.addActionListener(e -> {
            if (itemSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um item primeiro.");
                return;
            }

            JanelaNovaEncomenda janelaEncomenda = new JanelaNovaEncomenda(this, itemSelecionado);
            janelaEncomenda.setVisible(true);

            Encomenda encomenda = janelaEncomenda.getEncomendaCriada();
            if (encomenda != null) {
                System.out.println("Encomenda criada: " + encomenda.getCodigo());
                encomendas.add(encomenda);
                PersistenciaService.salvarEncomendas(encomendas);
            }
        });

        buttonGerirEncomendas.addActionListener(e -> {
            JanelaGerirEncomendas janela = new JanelaGerirEncomendas(this, encomendas, itens);
            janela.setVisible(true);

            // Após fechar a janela, salva as alterações caso alguma tenha acontecido
            PersistenciaService.salvarEncomendas(encomendas);
            PersistenciaService.salvarItens(itens);
            atualizarPainelItems(itens);
        });

        buttonAdicionarItem.addActionListener(e -> {
            Item novoItem = null;
            new JanelaAdicionarEditarItem(JanelaStock.this, novoItem, itens);
            atualizarPainelItems(itens);
        });

        atualizarPainelItems(itens);
    }

    public void atualizarPainelItems(List<Item> itens) {
        panelItems.removeAll();
        itemCartoes.clear();
        int rows = (int) Math.ceil(itens.size() / 3.0);
        panelItems.setLayout(new GridLayout(rows, 3, 20, 20));

        for (Item item : itens) {
            ItemCartao itemCartao = new ItemCartao(item);

            JPopupMenu menuContexto = new JPopupMenu();

            JMenuItem removerDoStock = new JMenuItem("Remover do Stock");
            removerDoStock.addActionListener(e -> {
                menuContexto.setVisible(false);
                new JanelaRemoverItem(JanelaStock.this, item, itens);
                atualizarPainelItems(itens);
            });

            JMenuItem editarItem = new JMenuItem("Editar Item");
            editarItem.addActionListener(e -> {
                menuContexto.setVisible(false);
                new JanelaAdicionarEditarItem(JanelaStock.this, item, itens);
                atualizarPainelItems(itens);
            });

            menuContexto.add(editarItem);
            menuContexto.add(removerDoStock);

            // Adiciona o evento de clique ao cartão
            itemCartao.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    setItemSelecionado(item);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        mostrarMenu(e);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (itemSelecionado != item) {
                        itemCartao.mouseEntrou();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (itemSelecionado != item) {
                        itemCartao.mouseSaiu();
                    }
                }

                private void mostrarMenu(MouseEvent e) {
                    // Fecha qualquer menu anterior
                    menuContexto.setVisible(false);
                    menuContexto.show(e.getComponent(), e.getX(), e.getY());
                }
            });
            panelItems.add(itemCartao);
            itemCartoes.add(itemCartao);
        }

        panelItems.revalidate();
        panelItems.repaint();

        if (scrollPane == null) {
            JPanel paddingPanel = new JPanel(new BorderLayout());
            paddingPanel.setBackground(new Color(183, 183, 183));
            paddingPanel.add(panelItems, BorderLayout.CENTER);
            paddingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            scrollPane = new JScrollPane(paddingPanel);
            scrollPane.setBackground(new Color(183, 183, 183));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            add(scrollPane, BorderLayout.CENTER);
        } else {
            scrollPane.getViewport().revalidate();
            scrollPane.getViewport().repaint();
        }

        if (itemSelecionado != null) setItemSelecionado(itemSelecionado);
    }

    public Item getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(Item item) {
        this.itemSelecionado = item;
        atualizarSelecao();

        // Mostrar informações ao lado direito
        mostrarDetalhesItem(item);
    }

    private void atualizarSelecao() {
        // Atualiza todos os cartões para refletir a seleção
        for (ItemCartao itemCartao : itemCartoes) {
            Item itemDoCartao = (Item) itemCartao.getClientProperty("item");
            boolean selecionado = itemDoCartao == itemSelecionado;

            itemCartao.atualizaBorda(selecionado);
        }
    }

    private void mostrarDetalhesItem(Item item) {
        panelDetalhesProduto.removeAll(); // Limpa o conteúdo anterior

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(new Color(183, 183, 183));
        conteudo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nome = new JLabel("Nome: " + item.getNome());
        nome.setFont(new Font("Arial", Font.BOLD, 16));
        nome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel categoria = new JLabel("Categoria: " + item.getCategoria());
        categoria.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel preco = new JLabel("Preço: €" + item.getPreco());
        preco.setAlignmentX(Component.LEFT_ALIGNMENT);

        String textoDescricao = !item.getDescricao().isEmpty() ? item.getDescricao() : "(sem descricao)";

        JTextArea descricao = new JTextArea("Descrição: " + textoDescricao);
        descricao.setWrapStyleWord(true);
        descricao.setLineWrap(true);
        descricao.setEditable(false);
        descricao.setOpaque(false);
        descricao.setFocusable(false);
        descricao.setBorder(null);
        descricao.setAlignmentX(Component.LEFT_ALIGNMENT);
        descricao.setMaximumSize(new Dimension(300, 80)); // Limita altura

        conteudo.add(nome);
        conteudo.add(Box.createVerticalStrut(5)); // Espaçamento
        conteudo.add(categoria);
        conteudo.add(Box.createVerticalStrut(5));
        conteudo.add(preco);
        conteudo.add(Box.createVerticalStrut(5));
        conteudo.add(descricao);

        // Se for combo, mostrar dependências
        if (item.getCategoria().equals("Combo")) {
            if (item.getDependencias() != null && !item.getDependencias().isEmpty()) {
                conteudo.add(Box.createVerticalStrut(10));
                conteudo.add(new JLabel("Inclui:"));
                for (Dependencia dep : item.getDependencias()) {
                    JLabel depLabel = new JLabel("- " + dep.getItem().getNome() + " (Qtd: " + dep.getQuantidade() + ")");
                    depLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    conteudo.add(depLabel);
                }
            }
        } else {
            JLabel quantidade = new JLabel("Quantidade: " + item.getQuantidade());
            quantidade.setAlignmentX(Component.LEFT_ALIGNMENT);
            conteudo.add(Box.createVerticalStrut(5));
            conteudo.add(quantidade);
        }

        panelDetalhesProduto.add(conteudo, BorderLayout.CENTER);
        panelDetalhesProduto.revalidate();
        panelDetalhesProduto.repaint();
    }

    public JButton getBtnVoltar() {
        return buttonVoltar;
    }

    public JPanel getPanelPrincipal() {
        return panelStock;
    }
}