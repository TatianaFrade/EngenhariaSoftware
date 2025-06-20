package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Janela final após seleção do lugar, com opções para adicionar produtos ou finalizar a compra
 */
public class JanelaOpcoesFinal extends JPanel {
    private JButton btnAdicionarProdutos;
    private JButton btnFinalizarCompra;
    private JButton btnVoltar;
    private JButton btnCancelar;

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
        this(sessao, lugar, preco, null, null);
    }

    /**
     * Construtor da janela de opções final com listeners para os botões
     * @param sessao Sessão selecionada
     * @param lugar Lugar selecionado
     * @param preco Preço total atual (incluindo lugar VIP se aplicável)
     * @param actionListenerVoltar Listener para o botão voltar
     * @param actionListenerCancelar Listener para o botão cancelar
     */
    public JanelaOpcoesFinal(Sessao sessao, Lugar lugar, double preco,
                             ActionListener actionListenerVoltar,
                             ActionListener actionListenerCancelar) {
        this.sessao = sessao;
        this.lugarSelecionado = lugar;
        this.precoTotal = preco;

        setLayout(new BorderLayout(10, 10));

        // Painel de título com resumo da compra
        configurarPainelTitulo();

        // Painel central com os botões de opções
        configurarPainelOpcoes();

        // Configurar painel de botões de navegação
        configurarPainelBotoesNavegacao(actionListenerVoltar, actionListenerCancelar);
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
        // O ActionListener será configurado na JanelaPrincipal

        // Botão para finalizar compra
        btnFinalizarCompra = new JButton("Finalizar Compra");
        btnFinalizarCompra.setFont(new Font(btnFinalizarCompra.getFont().getName(), Font.BOLD, 14));
        btnFinalizarCompra.setPreferredSize(new Dimension(300, 60));
        // O ActionListener será configurado em JanelaPrincipal

        painelCentral.add(btnAdicionarProdutos);
        painelCentral.add(btnFinalizarCompra);

        // Centralizar os botões
        JPanel painelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelWrapper.add(painelCentral);

        add(painelWrapper, BorderLayout.CENTER);
    }

    /**
     * Configura o painel com os botões de navegação (Voltar e Cancelar)
     * @param actionListenerVoltar Listener para o botão voltar
     * @param actionListenerCancelar Listener para o botão cancelar
     */
    private void configurarPainelBotoesNavegacao(ActionListener actionListenerVoltar, ActionListener actionListenerCancelar) {
        // Layout para posicionar botões (Cancelar à esquerda, Voltar à direita)
        JPanel painelBotoes = new JPanel(new BorderLayout());
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Botão de cancelar à esquerda
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> {
            // Voltar para o menu principal
            // Criar um array com as opções em português
            Object[] opcoes = {"Sim", "Não"};

            int resposta = JOptionPane.showOptionDialog(
                    this,
                    "Tem certeza de que deseja cancelar a compra?",
                    "Confirmar Cancelamento",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[1]  // "Não" como opção padrão (mais seguro)
            );

            if (resposta == JOptionPane.YES_OPTION) {
                Container topLevel = this.getParent();
                while (!(topLevel instanceof JFrame) && topLevel != null) {
                    topLevel = topLevel.getParent();
                }

                if (topLevel instanceof JanelaPrincipal) {
                    ((JanelaPrincipal) topLevel).voltarParaPainelPrincipal();
                }
            }
        });

        // Botão de voltar à direita
        btnVoltar = new JButton("Voltar");
        if (actionListenerVoltar != null) {
            btnVoltar.addActionListener(actionListenerVoltar);
        }

        // Adicionar botões aos painéis
        leftButtonPanel.add(btnCancelar);
        rightButtonPanel.add(btnVoltar);

        painelBotoes.add(leftButtonPanel, BorderLayout.WEST);
        painelBotoes.add(rightButtonPanel, BorderLayout.EAST);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    // Getters para os botões
    public JButton getBtnAdicionarProdutos() {
        return btnAdicionarProdutos;
    }

    public JButton getBtnFinalizarCompra() {
        return btnFinalizarCompra;
    }

    /**
     * Obtém uma referência ao botão voltar.
     * @return Botão voltar
     */
    public JButton getBtnVoltar() {
        return btnVoltar;
    }

    /**
     * Obtém uma referência ao botão cancelar.
     * @return Botão cancelar
     */
    public JButton getBtnCancelar() {
        return btnCancelar;
    }
}
