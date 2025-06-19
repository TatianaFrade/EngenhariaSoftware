package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;

/**
 * Janela final após seleção do lugar, com opções para adicionar produtos ou finalizar a compra
 */
public class JanelaOpcoesFinal extends JPanel {
    private JButton btnAdicionarProdutos;
    private JButton btnFinalizarCompra;
    private Sessao sessao;
    private Lugar lugarSelecionado;
    private double precoTotal;

    /**
     * Construtor da janela de opções final
     * @param sessao Sessão selecionada
     * @param lugar Lugar selecionado
     * @param preco Preço total atual (incluindo lugar VIP se aplicável)
     */
    public JanelaOpcoesFinal(Sessao sessao, Lugar lugar, double preco) {
        this.sessao = sessao;
        this.lugarSelecionado = lugar;
        this.precoTotal = preco;

        setLayout(new BorderLayout(10, 10));

        // Painel de título com resumo da compra
        configurarPainelTitulo();

        // Painel central com os botões de opções
        configurarPainelOpcoes();
    }

    private void configurarPainelTitulo() {
        JPanel painelTitulo = new JPanel();
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel labelTitulo = new JLabel("Resumo da Compra");
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.BOLD, 18));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelTitulo);

        painelTitulo.add(Box.createVerticalStrut(20));

        // Informações do filme
        JLabel labelFilme = new JLabel("Filme: " + sessao.getFilme().getNome());
        labelFilme.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelFilme);

        // Informações da sessão
        JLabel labelSessao = new JLabel("Sessão: " + sessao.getDataHoraFormatada() +
                " - Sala " + sessao.getNomeSala());
        labelSessao.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelSessao);

        // Informações do lugar
        JLabel labelLugar = new JLabel("Lugar: " + lugarSelecionado.getIdentificacao());
        labelLugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelLugar);

        // Informações do preço
        JLabel labelPreco = new JLabel(String.format("Preço: %.2f €", precoTotal));
        labelPreco.setFont(new Font(labelPreco.getFont().getName(), Font.BOLD, 14));
        labelPreco.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelPreco);

        add(painelTitulo, BorderLayout.NORTH);
    }

    private void configurarPainelOpcoes() {
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new GridLayout(2, 1, 10, 30));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        // Botão para adicionar produtos do bar
        btnAdicionarProdutos = new JButton("Adicionar Produtos do Bar");
        btnAdicionarProdutos.setFont(new Font(btnAdicionarProdutos.getFont().getName(), Font.BOLD, 14));
        btnAdicionarProdutos.setPreferredSize(new Dimension(300, 60));
        btnAdicionarProdutos.addActionListener(e -> {
            // Por enquanto não faz nada
            JOptionPane.showMessageDialog(this,
                    "Funcionalidade de adicionar produtos do bar será implementada em uma versão futura.",
                    "Em desenvolvimento",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Botão para finalizar compra
        btnFinalizarCompra = new JButton("Finalizar Compra");
        btnFinalizarCompra.setFont(new Font(btnFinalizarCompra.getFont().getName(), Font.BOLD, 14));
        btnFinalizarCompra.setPreferredSize(new Dimension(300, 60));
        btnFinalizarCompra.addActionListener(e -> {
            // Por enquanto não faz nada
            JOptionPane.showMessageDialog(this,
                    "Sua compra foi finalizada com sucesso!\nAgradecemos a preferência!",
                    "Compra Finalizada",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        painelCentral.add(btnAdicionarProdutos);
        painelCentral.add(btnFinalizarCompra);

        // Centralizar os botões
        JPanel painelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelWrapper.add(painelCentral);

        add(painelWrapper, BorderLayout.CENTER);
    }

    // Getters para os botões
    public JButton getBtnAdicionarProdutos() {
        return btnAdicionarProdutos;
    }

    public JButton getBtnFinalizarCompra() {
        return btnFinalizarCompra;
    }
}
