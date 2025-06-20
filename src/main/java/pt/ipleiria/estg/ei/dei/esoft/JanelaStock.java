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
    private JButton buttonGerirEncomendas;
    private JButton buttonAdicionarProduto;
    private JButton buttonRemoverDoStock;
    private JButton buttonEncomendar;

    private List<ItemCartao> itemCartoes;
    private Item itemSelecionado;

    public JanelaStock(List<Item> items) {
        setTitle("Stock");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panelStock);
        pack();
        setLocationRelativeTo(null);

        itemCartoes = new ArrayList<ItemCartao>();

        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));
        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));
        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));
        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));
        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));
        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));
        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));
        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));
        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));
        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));
        items.add(new Item("objeto", "é um objeto", 29.99, "coisas", true));

        atualizarPainelItems(items);
    }

    public void atualizarPainelItems(List<Item> items) {
        int rows = (int) Math.ceil(items.size() / 3.0);
        panelItems.setLayout(new GridLayout(rows, 3, 20, 20));

        for (Item item : items) {
            ItemCartao itemCartao = new ItemCartao(item);

            // Adiciona o evento de clique ao cartão
            itemCartao.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setItemSelecionado(item);
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
            });
            panelItems.add(itemCartao);
            itemCartoes.add(itemCartao);
        }

        JScrollPane scrollPane = new JScrollPane(panelItems);
        //scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll mais rápido
        add(scrollPane, BorderLayout.CENTER);
    }

    public Item getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(Item item) {
        this.itemSelecionado = item;
        //btnProximo.setEnabled(filme != null);
        atualizarSelecao();
    }

    private void atualizarSelecao() {
        // Atualiza todos os cartões para refletir a seleção
        for (ItemCartao itemCartao : itemCartoes) {
            Item itemDoCartao = (Item) itemCartao.getClientProperty("item");
            boolean selecionado = itemDoCartao == itemSelecionado;

            itemCartao.atualizaBorda(selecionado);
        }
    }

    public static void main(String[] args) {
        new JanelaStock(new ArrayList<Item>()).setVisible(true);
    }
}