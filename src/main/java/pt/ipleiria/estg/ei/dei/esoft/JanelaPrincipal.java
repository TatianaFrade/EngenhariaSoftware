package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JanelaPrincipal extends JFrame {
    private JPanel painelPrincipal;
    private JButton comprarBilheteButton;
    private JButton comprarProdutosButton;
    private JButton verMenusButton;
    private JButton consultarSessoesPorDiaButton;
    private JButton loginButton;    private List<Filme> filmes;
    private List<Sessao> sessoes;    public JanelaPrincipal(String title) {
        super(title);
        inicializarDados();
        criarPainelPrincipal();
        setContentPane(painelPrincipal);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Usar o mesmo tamanho padronizado para todas as janelas (900x650)
        setSize(900, 650);
        setLocationRelativeTo(null);
    }

    private void inicializarDados() {
        // Criar filmes
        filmes = Arrays.asList(
                new Filme("Matrix", true, "1999-03-31", 8.7, null),
                new Filme("O Rei Leão", false, "1994-06-24", 8.5, null),
                new Filme("Interestelar", true, "2014-11-06", 8.6, null),
                new Filme("Vingadores: Ultimato", false, "2019-04-26", 8.4, null),
                new Filme("Cidade de Deus", true, "2002-08-30", 8.6, null),
                new Filme("O Auto da Compadecida", false, "2000-09-15", 8.8, null),
                new Filme("Pantera Negra", true, "2018-02-15", 7.3, null),
                new Filme("La La Land", true, "2016-12-16", 8.0, null),
                new Filme("Bacurau", false, "2019-08-23", 7.7, null)
        );

        // Criar salas com configurações diferentes
        Sala sala1 = new Sala("Sala 1", "sim", 8, 10);
        Sala sala2 = new Sala("Sala 2", "sim", 8, 10);
        Sala sala3 = new Sala("Sala 3", "nao", 8, 10);

        // Criar sessões (vários horários para diferentes filmes)
        sessoes = new ArrayList<>();

        // Sessões para Matrix
        sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(1).withHour(14).withMinute(30), sala1, 7.50));
        sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(1).withHour(18).withMinute(0), sala1, 9.00));
        sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(2).withHour(16).withMinute(45), sala3, 7.50));

        // Sessões para O Rei Leão
        sessoes.add(new Sessao(filmes.get(1), LocalDateTime.now().plusDays(1).withHour(15).withMinute(0), sala2, 7.50));
        sessoes.add(new Sessao(filmes.get(1), LocalDateTime.now().plusDays(2).withHour(14).withMinute(0), sala2, 7.00));

        // Sessões para Interestelar
        sessoes.add(new Sessao(filmes.get(2), LocalDateTime.now().plusDays(1).withHour(20).withMinute(30), sala3, 9.00));
        sessoes.add(new Sessao(filmes.get(2), LocalDateTime.now().plusDays(3).withHour(19).withMinute(15), sala1, 9.00));

        // Sessões para Vingadores
        sessoes.add(new Sessao(filmes.get(3), LocalDateTime.now().plusDays(2).withHour(15).withMinute(0), sala1, 8.00));
        sessoes.add(new Sessao(filmes.get(3), LocalDateTime.now().plusDays(2).withHour(21).withMinute(30), sala3, 9.50));

        // Sessões para O Auto da Compadecida
        sessoes.add(new Sessao(filmes.get(5), LocalDateTime.now().plusDays(1).withHour(16).withMinute(0), sala2, 7.50));
        sessoes.add(new Sessao(filmes.get(5), LocalDateTime.now().plusDays(3).withHour(18).withMinute(30), sala2, 8.50));

        // Sessões para La La Land
        sessoes.add(new Sessao(filmes.get(7), LocalDateTime.now().plusDays(4).withHour(17).withMinute(45), sala2, 8.00));
    }    private void criarPainelPrincipal() {
        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());

        // Painel superior com título e login
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        // Título para o cinema com estilo neutro
        JLabel titulo = new JLabel("Cinema e Bar");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        // Botão de login com estilo neutro
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginPanel.add(loginButton);

        topPanel.add(titulo, BorderLayout.CENTER);
        topPanel.add(loginPanel, BorderLayout.EAST);

        // Adiciona o painel superior
        painelPrincipal.add(topPanel, BorderLayout.NORTH);
        // Criar botões com estilo neutro
        comprarBilheteButton = createStyledButton("Comprar Bilhete", null);
        comprarProdutosButton = createStyledButton("Comprar Produtos", null);
        verMenusButton = createStyledButton("Ver Menus", null);
        consultarSessoesPorDiaButton = createStyledButton("Consultar Sessões por Dia", null);

        // Painel central com os botões principais dispostos em grid
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.add(comprarBilheteButton);
        centerPanel.add(comprarProdutosButton);
        centerPanel.add(verMenusButton);
        centerPanel.add(consultarSessoesPorDiaButton);

        // Adiciona espaçamento ao redor do painel central
        JPanel paddedCenterPanel = new JPanel(new BorderLayout());
        paddedCenterPanel.add(centerPanel, BorderLayout.CENTER);
        paddedCenterPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        painelPrincipal.add(paddedCenterPanel, BorderLayout.CENTER);
        // Adiciona um rodapé simples
        JPanel footerPanel = new JPanel(new BorderLayout());
        JLabel footerLabel = new JLabel("Cinema e Bar");
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(footerLabel, BorderLayout.CENTER);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        painelPrincipal.add(footerPanel, BorderLayout.SOUTH);

        comprarBilheteButton.addActionListener(e -> mostrarJanelaSelecaoFilme());
    }

    private void mostrarJanelaSelecaoFilme() {
        // Criar o painel de seleção de filme
        JanelaSelecaoFilme painelFilmes = new JanelaSelecaoFilme(filmes, null, null);

        // Adicionar listener para o botão Voltar
        painelFilmes.getBtnVoltar().addActionListener(e -> voltarParaPainelPrincipal());

        // Adicionar listener para o botão Próximo
        painelFilmes.getBtnProximo().addActionListener(e -> {
            Filme filmeSeleccionado = painelFilmes.getFilmeSeleccionado();
            if (filmeSeleccionado != null) {
                mostrarJanelaSelecaoSessao(filmeSeleccionado);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um filme primeiro.");
            }
        });

        trocarPainel(painelFilmes);
    }

    private void mostrarJanelaSelecaoSessao(Filme filmeSeleccionado) {
        if (filmeSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um filme primeiro.");
            return;
        }

        // Criar o painel de seleção de sessão
        JanelaSelecaoSessao painelSessoes = new JanelaSelecaoSessao(filmeSeleccionado, sessoes, null, null);

        // Adicionar listener para o botão Voltar
        painelSessoes.getBtnVoltar().addActionListener(e -> mostrarJanelaSelecaoFilme());

        // Adicionar listener para o botão Próximo
        painelSessoes.getBtnProximo().addActionListener(e -> {
            Sessao sessaoSeleccionada = painelSessoes.getSessaoSeleccionada();
            if (sessaoSeleccionada != null) {
                continuarCompra(sessaoSeleccionada);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione uma sessão primeiro.");
            }
        });

        trocarPainel(painelSessoes);
    }

    private void continuarCompra(Sessao sessaoSeleccionada) {
        if (sessaoSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma sessão primeiro.");
            return;
        }

        // Mostrar a janela de seleção de lugar
        mostrarJanelaSelecaoLugar(sessaoSeleccionada);
    }    private void mostrarJanelaSelecaoLugar(Sessao sessaoSeleccionada) {
        // Criar o painel de seleção de lugar
        JanelaSelecaoLugar painelLugares = new JanelaSelecaoLugar(sessaoSeleccionada, null, null);

        // Adicionar listener para o botão Voltar
        painelLugares.getBtnVoltar().addActionListener(e -> {
            // Voltar para a seleção de sessão
            mostrarJanelaSelecaoSessao(sessaoSeleccionada.getFilme());
        });

        // Adicionar listener para o botão Próximo para ir para a tela de opções finais
        painelLugares.getBtnProximo().addActionListener(e -> {
            Lugar lugarSelecionado = painelLugares.getLugarSelecionadoObjeto();
            if (lugarSelecionado != null) {
                // Mostrar a janela de opções finais
                mostrarJanelaOpcoesFinal(sessaoSeleccionada, lugarSelecionado, painelLugares.getPrecoTotal());
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um lugar primeiro.");
            }
        });

        trocarPainel(painelLugares);
    }      private void mostrarJanelaOpcoesFinal(Sessao sessao, Lugar lugar, double precoTotal) {
        // Criar o painel de opções finais
        JanelaOpcoesFinal painelOpcoes = new JanelaOpcoesFinal(sessao, lugar, precoTotal);

        // Configurar os botões
        painelOpcoes.getBtnAdicionarProdutos().addActionListener(e -> {
            // Abrir a janela de seleção de itens do bar
            mostrarJanelaSelecaoItensBar(sessao, lugar, precoTotal);
        });

        painelOpcoes.getBtnFinalizarCompra().addActionListener(e -> {
            // Avançar para a tela de pagamento
            mostrarJanelaPagamento(sessao, lugar, precoTotal);
        });

        trocarPainel(painelOpcoes);
    }

    private void voltarParaPainelPrincipal() {
        trocarPainel(painelPrincipal);
    }    private void trocarPainel(JPanel novoPainel) {
        setContentPane(novoPainel);

        // Usar um tamanho consistente para todas as janelas
        // Um tamanho maior é usado para acomodar todas as telas, incluindo a de pagamento
        setSize(900, 650);

        setLocationRelativeTo(null); // Centraliza novamente após redimensionar
        revalidate();
        repaint();    }private void mostrarJanelaPagamento(Sessao sessao, Lugar lugar, double precoTotal) {
        // Criar o painel de pagamento
        JanelaPagamento painelPagamento = new JanelaPagamento(
                sessao,
                lugar,
                precoTotal,
                // ActionListener para o botão Voltar - retorna à tela de opções finais
                e -> mostrarJanelaOpcoesFinal(sessao, lugar, precoTotal),
                // ActionListener para o botão Próximo - finaliza o pagamento
                null // Será configurado após a criação
        );
        // Configurar o ActionListener para o botão Próximo separadamente
        painelPagamento.getBtnProximo().addActionListener(e ->
                finalizarPagamento(sessao, lugar, precoTotal, painelPagamento.getMetodoPagamentoSelecionado(), painelPagamento)
        );

        trocarPainel(painelPagamento);
    }
    /**
     * Finaliza o processo de compra e pagamento, exibindo uma mensagem de confirmação
     * e retornando ao menu principal
     */
    private void finalizarPagamento(Sessao sessao, Lugar lugar, double precoTotal, String metodoPagamento, JanelaPagamento painelPagamento) {
        // Marcar o lugar como ocupado na sala
        sessao.getSala().ocuparLugar(lugar.getFila(), lugar.getColuna());

        // Mensagem de pagamento concluído com base no método selecionado
        String mensagem;
        if (metodoPagamento.equals("Cartão de Crédito")) {
            // Coletar dados do cartão usando o painel de pagamento fornecido
            java.util.Map<String, String> dadosCartao = painelPagamento.coletarDadosCartao();

            // Se o usuário cancelou ou houve erro de validação
            if (dadosCartao == null) {
                return; // Não continua com o processo
            }

            // Formato simplificado do número do cartão para exibição (últimos 4 dígitos)
            String numeroCartao = dadosCartao.get("numeroCartao");
            String ultimos4Digitos = numeroCartao.length() > 4 ?
                    numeroCartao.substring(numeroCartao.length() - 4) :
                    numeroCartao;

            mensagem = "Pagamento realizado com sucesso via " + metodoPagamento + "!\n" +
                    "Cartão: **** **** **** " + ultimos4Digitos + "\n" +
                    "Titular: " + dadosCartao.get("nomeTitular") + "\n" +
                    "Seu bilhete para " + sessao.getFilme().getNome() + " foi emitido.\n" +
                    "Agradecemos a preferência!";
        } else {
            // Multibanco - gerar referência fictícia
            String referencia = gerarReferenciaMultibanco();
            mensagem = "Referência Multibanco gerada com sucesso!\n" +
                    "Referência: " + referencia + "\n" +
                    "Valor: " + String.format("%.2f €", precoTotal) + "\n" +
                    "Por favor, efetue o pagamento em 48 horas para validar sua compra.\n" +
                    "Agradecemos a preferência!";
        }

        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Pagamento " + (metodoPagamento.equals("Cartão de Crédito") ? "Finalizado" : "Pendente"),
                JOptionPane.INFORMATION_MESSAGE
        );

        // Voltar para o menu principal
        voltarParaPainelPrincipal();
    }

    /**
     * Gera uma referência Multibanco fictícia para demonstração
     */
    private String gerarReferenciaMultibanco() {
        // Gera uma referência no formato XXX XXX XXX
        StringBuilder ref = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ref.append((int) (Math.random() * 10));
            }
            if (i < 2) ref.append(" ");
        }
        return ref.toString();
    }    /**
     * Cria um botão estilizado para o menu principal
     * @param texto Texto do botão
     * @param cor Cor de fundo do botão (não utilizada para manter estilo neutro)
     * @return JButton estilizado
     */
    private JButton createStyledButton(String texto, Color cor) {
        JButton button = new JButton(texto);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        // Usar estilo padrão do sistema para os botões (neutro)
        button.setFocusPainted(true);
        button.setBorderPainted(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 100));

        // Adiciona margem interna ao texto
        button.setMargin(new Insets(10, 10, 10, 10));

        return button;
    }    /**
     * Mostra a janela de seleção de itens do bar
     *
     * @param sessao A sessão selecionada
     * @param lugar O lugar selecionado
     * @param precoBase O preço do bilhete (sem itens do bar)
     */
    private void mostrarJanelaSelecaoItensBar(Sessao sessao, Lugar lugar, double precoBase) {
        // Criar o painel de seleção de itens do bar
        final JanelaSelecaoItensBar painelItensBar = new JanelaSelecaoItensBar(
                sessao,
                lugar,
                precoBase,
                // ActionListener para o botão Voltar - retorna à tela de opções finais
                e -> mostrarJanelaOpcoesFinal(sessao, lugar, precoBase),
                null // O listener para Próximo será configurado abaixo
        );

        // Configurar o ActionListener para o botão Próximo separadamente
        painelItensBar.getBtnProximo().addActionListener(e -> {
            // Calcular o preço total (bilhete + itens do bar)
            double precoTotal = painelItensBar.getPrecoTotal();

            // Avançar para a tela de pagamento com o valor atualizado
            mostrarJanelaPagamentoComItens(sessao, lugar, precoTotal, painelItensBar.getItensSelecionados());
        });

        trocarPainel(painelItensBar);
    }    /**
     * Mostra a janela de pagamento incluindo itens do bar
     *
     * @param sessao A sessão selecionada
     * @param lugar O lugar selecionado
     * @param precoTotal O preço total (bilhete + itens do bar)
     * @param itensSelecionados Lista de itens selecionados do bar
     */
    private void mostrarJanelaPagamentoComItens(Sessao sessao, Lugar lugar, double precoTotal, List<Item> itensSelecionados) {
        // Calcular o valor dos itens do bar
        double valorItensBar = 0.0;
        for (Item item : itensSelecionados) {
            valorItensBar += item.getPreco();
        }

        // Criar o painel de pagamento
        JanelaPagamento painelPagamento = new JanelaPagamento(
                sessao,
                lugar,
                precoTotal,
                // ActionListener para o botão Voltar - retorna à tela de seleção de itens
                e -> mostrarJanelaSelecaoItensBar(sessao, lugar, lugar.calcularPreco(sessao.getPreco())),
                // ActionListener para o botão Próximo - finaliza o pagamento
                null // Será configurado após a criação
        );

        // Adicionar os detalhes dos itens do bar ao painel de pagamento
        painelPagamento.adicionarDetalhesItensBar(itensSelecionados, valorItensBar);

        // Configurar o ActionListener para o botão Próximo separadamente
        painelPagamento.getBtnProximo().addActionListener(e ->
                finalizarPagamentoComItens(sessao, lugar, precoTotal, painelPagamento.getMetodoPagamentoSelecionado(),
                        painelPagamento, itensSelecionados)
        );

        trocarPainel(painelPagamento);
    }

    /**
     * Finaliza o processo de compra e pagamento incluindo itens do bar
     *
     * @param sessao A sessão selecionada
     * @param lugar O lugar selecionado
     * @param precoTotal O preço total (bilhete + itens do bar)
     * @param metodoPagamento O método de pagamento selecionado
     * @param painelPagamento Referência ao painel de pagamento
     * @param itensSelecionados Lista de itens selecionados do bar
     */
    private void finalizarPagamentoComItens(Sessao sessao, Lugar lugar, double precoTotal,
                                            String metodoPagamento, JanelaPagamento painelPagamento,
                                            List<Item> itensSelecionados) {
        // Marcar o lugar como ocupado na sala
        sessao.getSala().ocuparLugar(lugar.getFila(), lugar.getColuna());

        // Calcular o valor dos itens do bar
        double valorItensBar = 0.0;
        StringBuilder itensStr = new StringBuilder();

        for (Item item : itensSelecionados) {
            valorItensBar += item.getPreco();
            itensStr.append("- ").append(item.getNome()).append(": ").append(String.format("%.2f €", item.getPreco())).append("\n");
        }

        // Mensagem de pagamento concluído com base no método selecionado
        String mensagem;
        if (metodoPagamento.equals("Cartão de Crédito")) {
            // Coletar dados do cartão usando o painel de pagamento fornecido
            java.util.Map<String, String> dadosCartao = painelPagamento.coletarDadosCartao();

            // Se o usuário cancelou ou houve erro de validação
            if (dadosCartao == null) {
                return; // Não continua com o processo
            }

            // Formato simplificado do número do cartão para exibição (últimos 4 dígitos)
            String numeroCartao = dadosCartao.get("numeroCartao");
            String ultimos4Digitos = numeroCartao.length() > 4 ?
                    numeroCartao.substring(numeroCartao.length() - 4) :
                    numeroCartao;

            mensagem = "Pagamento realizado com sucesso via " + metodoPagamento + "!\n" +
                    "Cartão: **** **** **** " + ultimos4Digitos + "\n" +
                    "Titular: " + dadosCartao.get("nomeTitular") + "\n\n" +
                    "Seu bilhete para " + sessao.getFilme().getNome() + " foi emitido.\n\n" +
                    (itensSelecionados.isEmpty() ? "" : "Itens do Bar:\n" + itensStr.toString() + "\n") +
                    "Subtotal bilhete: " + String.format("%.2f €", precoTotal - valorItensBar) + "\n" +
                    "Subtotal itens: " + String.format("%.2f €", valorItensBar) + "\n" +
                    "TOTAL: " + String.format("%.2f €", precoTotal) + "\n\n" +
                    "Agradecemos a preferência!";
        } else {
            // Multibanco - gerar referência fictícia
            String referencia = gerarReferenciaMultibanco();
            mensagem = "Referência Multibanco gerada com sucesso!\n" +
                    "Referência: " + referencia + "\n" +
                    "Valor: " + String.format("%.2f €", precoTotal) + "\n\n" +
                    (itensSelecionados.isEmpty() ? "" : "Itens do Bar:\n" + itensStr.toString() + "\n") +
                    "Subtotal bilhete: " + String.format("%.2f €", precoTotal - valorItensBar) + "\n" +
                    "Subtotal itens: " + String.format("%.2f €", valorItensBar) + "\n" +
                    "TOTAL: " + String.format("%.2f €", precoTotal) + "\n\n" +
                    "Por favor, efetue o pagamento em 48 horas para validar sua compra.\n" +
                    "Agradecemos a preferência!";
        }

        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Pagamento " + (metodoPagamento.equals("Cartão de Crédito") ? "Finalizado" : "Pendente"),
                JOptionPane.INFORMATION_MESSAGE
        );

        // Voltar para o menu principal
        voltarParaPainelPrincipal();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JanelaPrincipal("Cinema e Bar").setVisible(true);
        });
    }
}
