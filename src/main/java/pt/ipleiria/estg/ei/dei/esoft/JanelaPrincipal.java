package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JanelaPrincipal extends JFrame {
    private JPanel painelPrincipal;    private JButton comprarBilheteButton;
    private JButton comprarProdutosButton;
    private JButton verMenusButton;
    private JButton consultarSessoesPorDiaButton;
    private JButton loginButton;
    private JButton perfilButton;
    private JLabel usuarioLabel; // Rótulo para mostrar o nome do usuário
    private Usuario usuarioLogado; // Usuário atualmente logado

    private List<Filme> filmes;
    private List<Sessao> sessoes;

    public JanelaPrincipal(String title) {
        super(title);
        inicializarDados();
        criarPainelPrincipal();
        setContentPane(painelPrincipal);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Usar o mesmo tamanho padronizado para todas as janelas (900x650)
        setSize(900, 650);
        setLocationRelativeTo(null);
    }    /**
     * Inicializa os dados da aplicação, garantindo persistência dos lugares ocupados.
     *
     * Fluxo de persistência:
     * 1. Tenta carregar dados salvos (filmes, salas, sessões e compras)
     * 2. Se existirem sessões salvas, as utiliza mantendo os lugares já ocupados
     * 3. Se não existirem, cria sessões com valores padrão
     * 4. Restaura lugares ocupados com base nas compras confirmadas
     * 5. Salva as sessões atualizadas para garantir persistência
     */
    private void inicializarDados() {
        try {
            System.out.println("Inicializando dados da aplicação...");

            // Tentar carregar dados salvos dos arquivos JSON
            filmes = PersistenciaService.carregarFilmes();
            List<Sala> salas = PersistenciaService.carregarSalas();
            List<Compra> compras = PersistenciaService.carregarCompras();

            // Se não existirem dados salvos, criar dados padrão
            if (filmes == null || filmes.isEmpty()) {
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
                PersistenciaService.salvarFilmes(filmes);
            }

            // Criar ou carregar salas
            List<Sala> salasList;
            if (salas == null || salas.isEmpty()) {
                salasList = new ArrayList<>();
                Sala sala1 = new Sala("Sala 1", "sim", 8, 10);
                Sala sala2 = new Sala("Sala 2", "sim", 8, 10);
                Sala sala3 = new Sala("Sala 3", "nao", 8, 10);
                salasList.add(sala1);
                salasList.add(sala2);
                salasList.add(sala3);
                PersistenciaService.salvarSalas(salasList);
            } else {
                salasList = salas;
            }

            // ALTERAÇÃO PRINCIPAL: Primeiro carregar sessões existentes
            List<Sessao> sessoesExistentes = PersistenciaService.carregarSessoes();

            // Verificar se existem sessões salvas
            if (sessoesExistentes != null && !sessoesExistentes.isEmpty()) {
                System.out.println("Encontradas " + sessoesExistentes.size() + " sessões salvas.");

                // Usar as sessões existentes que já contêm informações de ocupação de lugares
                sessoes = sessoesExistentes;

                // Mostrar quais lugares estão ocupados
                for (Sessao s : sessoes) {
                    for (Lugar l : s.getSala().getLugares()) {
                        if (l.isOcupado()) {
                            System.out.println("Sessão " + s.getId() + " (" + s.getFilme().getNome() +
                                    ") tem lugar ocupado: " + l.getIdentificacao());
                        }
                    }
                }
            } else {
                System.out.println("Nenhuma sessão encontrada. Criando sessões padrão...");

                // Só criar novas sessões se não houver nenhuma salva
                sessoes = new ArrayList<>();

                // Adicionar as sessões com os mesmos dados hardcoded
                sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(1).withHour(14).withMinute(30), salasList.get(0), 7.50));
                sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(1).withHour(18).withMinute(0), salasList.get(0), 9.00));
                sessoes.add(new Sessao(filmes.get(0), LocalDateTime.now().plusDays(2).withHour(16).withMinute(45), salasList.get(2), 7.50));

                sessoes.add(new Sessao(filmes.get(1), LocalDateTime.now().plusDays(1).withHour(15).withMinute(0), salasList.get(1), 7.50));
                sessoes.add(new Sessao(filmes.get(1), LocalDateTime.now().plusDays(2).withHour(14).withMinute(0), salasList.get(1), 7.00));

                sessoes.add(new Sessao(filmes.get(2), LocalDateTime.now().plusDays(1).withHour(20).withMinute(30), salasList.get(2), 9.00));
                sessoes.add(new Sessao(filmes.get(2), LocalDateTime.now().plusDays(3).withHour(19).withMinute(15), salasList.get(0), 9.00));

                sessoes.add(new Sessao(filmes.get(3), LocalDateTime.now().plusDays(2).withHour(15).withMinute(0), salasList.get(0), 8.00));
                sessoes.add(new Sessao(filmes.get(3), LocalDateTime.now().plusDays(2).withHour(21).withMinute(30), salasList.get(2), 9.50));

                sessoes.add(new Sessao(filmes.get(5), LocalDateTime.now().plusDays(1).withHour(16).withMinute(0), salasList.get(1), 7.50));
                sessoes.add(new Sessao(filmes.get(5), LocalDateTime.now().plusDays(3).withHour(18).withMinute(30), salasList.get(1), 8.50));

                sessoes.add(new Sessao(filmes.get(7), LocalDateTime.now().plusDays(4).withHour(17).withMinute(45), salasList.get(1), 8.00));

                // Salvar as sessões recém-criadas
                PersistenciaService.salvarSessoes(sessoes);
            }

            // Restaurar lugares ocupados com base nas compras confirmadas
            if (compras != null && !compras.isEmpty()) {
                restaurarLugaresOcupados(sessoes, compras);
                PersistenciaService.salvarSessoes(sessoes);
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
            e.printStackTrace();            // Em caso de erro, inicializar com dados padrão
            inicializarDadosPadrao();
        }
    }
    private void criarPainelPrincipal() {
        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());

        // Painel superior com título e login
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Título para o cinema com estilo neutro
        JLabel titulo = new JLabel("Cinema e Bar");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);        // Botão de login/logout com estilo neutro
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(80, 30));
        loginButton.addActionListener(e -> {
            if (usuarioLogado != null) {
                // Se estiver logado, faz logout
                realizarLogout();
            } else {
                // Se não estiver logado, mostra tela de login
                mostrarJanelaLogin();
            }
        });
        // Rótulo para mostrar o nome do usuário (inicialmente vazio)
        usuarioLabel = new JLabel("");
        usuarioLabel.setPreferredSize(new Dimension(150, 30));
        usuarioLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 12));

        // Botão de perfil (inicialmente invisível)
        perfilButton = new JButton("Meu Perfil");
        perfilButton.setPreferredSize(new Dimension(100, 30));
        perfilButton.setVisible(false); // Só fica visível quando o usuário está logado
        perfilButton.addActionListener(e -> mostrarJanelaEditarPerfil());

        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        loginPanel.add(usuarioLabel);
        loginPanel.add(perfilButton);
        loginPanel.add(loginButton);

        topPanel.add(titulo, BorderLayout.CENTER);
        topPanel.add(loginPanel, BorderLayout.EAST);

        // Adiciona o painel superior
        painelPrincipal.add(topPanel, BorderLayout.NORTH);

        // Criar botões com estilo neutro
        comprarBilheteButton = createStyledButton("Comprar Bilhete", null);
        comprarProdutosButton = createStyledButton("Comprar Produtos", null);
        verMenusButton = createStyledButton("Ver Menus", null);
        consultarSessoesPorDiaButton = createStyledButton("Consultar Sessões por Dia", null);        // Painel central com os botões principais dispostos em grid
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
        painelPrincipal.add(footerPanel, BorderLayout.SOUTH);        comprarBilheteButton.addActionListener(e -> mostrarJanelaSelecaoFilme());
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
        );        // Configurar o ActionListener para o botão Finalizar Pagamento separadamente
        painelPagamento.getBtnPagar().addActionListener(e -> {
            String metodoPagamento = painelPagamento.getMetodoPagamentoSelecionado();
            if (metodoPagamento == null || metodoPagamento.isEmpty()) {
                JOptionPane.showMessageDialog(painelPagamento,
                        "Por favor, selecione um método de pagamento",
                        "Método de Pagamento Obrigatório",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            finalizarPagamento(sessao, lugar, precoTotal, metodoPagamento, painelPagamento);
        });

        trocarPainel(painelPagamento);
    }    /**
     * Finaliza o processo de compra e pagamento, exibindo uma mensagem de confirmação
     * e retornando ao menu principal.
     *
     * IMPORTANTE: Este método garante a persistência dos lugares ocupados através de:
     * 1. Marcação do lugar como ocupado na sala da sessão
     * 2. Criação e salvamento de um objeto Compra no arquivo compras.json
     * 3. Atualização e salvamento do arquivo sessoes.json com o lugar ocupado
     *
     * Assim, mesmo quando a aplicação é reiniciada, os lugares ocupados permanecem assim.
     */
    private void finalizarPagamento(Sessao sessao, Lugar lugar, double precoTotal, String metodoPagamento, JanelaPagamento painelPagamento) {
        // Marcar o lugar como ocupado na sala
        sessao.getSala().ocuparLugar(lugar.getFila(), lugar.getColuna());

        try {
            // Criar uma compra com o método de pagamento escolhido e preço total
            List<Item> itensSelecionados = painelPagamento.getItensSelecionados();
            if (itensSelecionados == null) {
                itensSelecionados = new ArrayList<>();
            }

            Compra compra = new Compra(sessao, lugar, itensSelecionados, precoTotal, metodoPagamento);

            // Salvar a compra primeiro
            PersistenciaService.salvarCompra(compra);
            System.out.println("Compra salva com sucesso! ID: " + compra.getId());

            // Garantir que o lugar está ocupado na sessão correta
            for (Sessao s : sessoes) {
                if (s.getId().equals(sessao.getId())) {
                    s.getSala().ocuparLugar(lugar.getFila(), lugar.getColuna());
                    break;
                }
            }

            // Salvar as sessões após a atualização
            PersistenciaService.salvarSessoes(sessoes);

        } catch (Exception e) {
            System.err.println("Erro ao salvar compra: " + e.getMessage());
            e.printStackTrace();
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
     */    private void mostrarJanelaPagamentoComItens(Sessao sessao, Lugar lugar, double precoTotal, List<Item> itensSelecionados) {
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
                itensSelecionados, // Pass the selected items to constructor
                // ActionListener para o botão Voltar - retorna à tela de seleção de itens
                e -> mostrarJanelaSelecaoItensBar(sessao, lugar, lugar.calcularPreco(sessao.getPreco())),
                // ActionListener para o botão Próximo - finaliza o pagamento
                null // Será configurado após a criação
        );

        // Adicionar os detalhes dos itens do bar ao painel de pagamento
        painelPagamento.adicionarDetalhesItensBar(itensSelecionados, valorItensBar);
        // Configurar o ActionListener para o botão Finalizar Pagamento separadamente
        painelPagamento.getBtnPagar().addActionListener(e ->
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

    // Método para inicializar dados padrão quando não há persistência
    private void inicializarDadosPadrao() {
        // Criar filmes padrão
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

        // Criar salas padrão
        Sala sala1 = new Sala("Sala 1", "sim", 8, 10);
        Sala sala2 = new Sala("Sala 2", "sim", 8, 10);
        Sala sala3 = new Sala("Sala 3", "nao", 8, 10);

        // Criar sessões padrão (vários horários para diferentes filmes)
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
    }

    /**
     * Restaura os lugares ocupados com base nas compras confirmadas
     */    /**
     * Restaura os lugares ocupados com base nas compras confirmadas.
     * Este método é essencial para manter a persistência do estado de ocupação
     * dos assentos mesmo quando a aplicação é reiniciada.
     *
     * @param sessoes Lista de sessões onde os lugares serão marcados como ocupados
     * @param compras Lista de compras confirmadas com informações de ocupação de lugar
     */
    private void restaurarLugaresOcupados(List<Sessao> sessoes, List<Compra> compras) {
        if (compras == null || compras.isEmpty()) {
            System.out.println("Nenhuma compra encontrada para restaurar lugares ocupados");
            return;
        }

        System.out.println("Restaurando " + compras.size() + " compras para marcar lugares ocupados");

        // Criar mapa de sessões para acesso mais rápido
        Map<String, Sessao> mapaSessoes = new HashMap<>();
        for (Sessao sessao : sessoes) {
            mapaSessoes.put(sessao.getId(), sessao);
        }
        int lugaresRestaurados = 0;

        for (Compra compra : compras) {
            if (compra.isConfirmada()) {
                Sessao sessao = mapaSessoes.get(compra.getIdSessao());

                if (sessao != null) {
                    // Extrair fila e coluna da identificação do lugar (ex: "A5")
                    String lugarId = compra.getIdLugar();
                    if (lugarId != null && lugarId.length() >= 2) {
                        try {
                            char filaChar = lugarId.charAt(0);
                            int fila = filaChar - 'A';  // Converte A->0, B->1, etc.

                            String colunaStr = lugarId.substring(1).split(" ")[0]; // Remove "(VIP)" se existir
                            int coluna = Integer.parseInt(colunaStr) - 1;  // Ajuste para índice base-0

                            // Marcar lugar como ocupado
                            boolean ocupado = sessao.getSala().ocuparLugar(fila, coluna);
                            if (ocupado) {
                                lugaresRestaurados++;
                                System.out.println("Lugar " + lugarId + " restaurado como ocupado na sessão " +
                                        sessao.getId() + " (" + sessao.getFilme().getNome() + ")");
                            }                else {
                                System.out.println("Não foi possível ocupar lugar " + lugarId + " na sessão " +
                                        sessao.getId() + " (possivelmente já ocupado)");
                            }
                        } catch (Exception e) {
                            System.err.println("Erro ao processar lugar: " + lugarId + " - " + e.getMessage());                        }
                    }
                }
            }
        }

        System.out.println("Total de lugares restaurados: " + lugaresRestaurados);
    }    /**
     * Mostra a janela de login.
     */
    private void mostrarJanelaLogin() {
        // Criar o ouvinte para o botão Login
        ActionListener loginListener = e -> {
            // Obter uma referência ao componente de origem do evento
            JButton sourceButton = (JButton) e.getSource();
            // Obter o painel pai (que é a JanelaLogin)
            JanelaLogin painel = (JanelaLogin) sourceButton.getParent().getParent();

            // Obtendo os dados do formulário de login
            String usuario = painel.getUsuario();
            char[] senha = painel.getSenha();

            // Tentativa de autenticação via serviço
            Usuario usuarioAutenticado = UsuarioService.autenticarUsuario(usuario, new String(senha));

            // Limpar senha por segurança após uso
            for (int i = 0; i < senha.length; i++) {
                senha[i] = 0;
            }              if (usuarioAutenticado != null) {
                // Autenticação bem-sucedida
                usuarioLogado = usuarioAutenticado;
                // Atualizar botão de login para mostrar apenas "Logout"
                loginButton.setText("Logout");

                // Atualizar o rótulo com o nome do usuário
                usuarioLabel.setText("Olá, " + usuarioAutenticado.getNome());

                // Mostrar botão de perfil
                perfilButton.setVisible(true);

                // Atualizar título da janela
                atualizarTituloJanela();

                // Mostrar mensagem de sucesso e voltar para o painel principal
                JOptionPane.showMessageDialog(this,
                        "Login realizado com sucesso! Bem-vindo, " + usuarioAutenticado.getNome() + "!");
                voltarParaPainelPrincipal();
            } else {
                // Autenticação falhou
                JOptionPane.showMessageDialog(this,
                        "Usuário ou senha incorretos.",
                        "Erro de Autenticação",
                        JOptionPane.ERROR_MESSAGE);
            }
        };

        // Criar painel de login com ações para os botões
        JanelaLogin painelLogin = new JanelaLogin(
                // Botão Voltar
                e -> voltarParaPainelPrincipal(),

                // Botão Login (autenticação)
                loginListener,

                // Botão Criar Conta - redireciona para a tela de criação
                e -> mostrarJanelaCriarConta()
        );

        // Mostrar o painel de login
        trocarPainel(painelLogin);
    }    /**
     * Mostra a janela de criação de conta.
     */
    private void mostrarJanelaCriarConta() {
        // Criar o ouvinte para o botão Criar Conta
        ActionListener criarContaListener = e -> {
            // Obter uma referência ao componente de origem do evento
            JButton sourceButton = (JButton) e.getSource();
            // Obter o painel pai (que é a JanelaCriarConta)
            JanelaCriarConta painel = (JanelaCriarConta) sourceButton.getParent().getParent();

            // Primeiro validar o formulário de criação de conta
            if (painel.validarFormulario()) {
                // Coletar dados do formulário
                String nome = painel.getNome();
                String nomeUsuario = painel.getUsuario();
                String email = painel.getEmail();
                String senha = new String(painel.getSenha());

                // Criar objeto usuário
                Usuario novoUsuario = new Usuario(nome, nomeUsuario, email, senha);

                // Tentar salvar o usuário
                boolean sucesso = UsuarioService.salvarUsuario(novoUsuario);

                if (sucesso) {
                    // Usuário criado com sucesso
                    JOptionPane.showMessageDialog(this,
                            "Conta criada com sucesso! Por favor, faça login.");
                    mostrarJanelaLogin();
                } else {
                    // Falha ao criar usuário (nome já existe)
                    JOptionPane.showMessageDialog(this,
                            "Erro ao criar conta. O nome de usuário já existe.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        // Criar instância da janela de criação de conta
        JanelaCriarConta painelCriarConta = new JanelaCriarConta(
                // Ação para o botão Voltar: retornar à tela de login
                e -> mostrarJanelaLogin(),

                // Ação para o botão Criar Conta
                criarContaListener
        );

        // Exibir a janela de criação de conta
        trocarPainel(painelCriarConta);
    }

    /**
     * Mostra a janela de edição de perfil do usuário logado.
     */    private void mostrarJanelaEditarPerfil() {
        if (usuarioLogado == null) {
            JOptionPane.showMessageDialog(this,
                    "É necessário estar logado para editar o perfil.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("Abrindo janela de edição de perfil para usuário: " + usuarioLogado.getNomeUsuario());
        UsuarioService.listarUsuariosRegistrados();

        // Criar os listeners para os botões da janela de edição
        ActionListener voltarListener = e -> voltarParaPainelPrincipal();
        ActionListener salvarListener = e -> {
            // Obter o painel de edição - precisa encontrar o componente JanelaEditarPerfil
            // que pode estar em vários níveis na hierarquia de containers
            JButton sourceButton = (JButton) e.getSource();
            Container container = sourceButton.getParent();
            JanelaEditarPerfil painelEdicao = null;

            // Procurar pelo componente JanelaEditarPerfil na hierarquia
            while (container != null) {
                if (container instanceof JanelaEditarPerfil) {
                    painelEdicao = (JanelaEditarPerfil) container;
                    break;
                }
                container = container.getParent();
            }

            if (painelEdicao == null) {
                System.err.println("ERRO: Não foi possível encontrar o painel de edição de perfil.");
                JOptionPane.showMessageDialog(this,
                        "Erro ao processar alterações. Tente novamente.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("Formulário de edição encontrado, validando...");
            // Validar o formulário antes de continuar
            if (painelEdicao.validarFormulario()) {
                System.out.println("Formulário validado com sucesso.");
                // Obter os dados atualizados
                Usuario usuarioAtualizado = painelEdicao.getDadosAtualizados();

                System.out.println("Dados obtidos do formulário: Nome=" + usuarioAtualizado.getNome() +
                        ", Email=" + usuarioAtualizado.getEmail() +
                        ", Usuario=" + usuarioAtualizado.getUsuario());
                System.out.println("Iniciando processo de atualização de usuário...");
                System.out.println("Usuário atual logado: " + usuarioLogado.getNomeUsuario());

                // Mostrar usuários antes da atualização
                UsuarioService.listarUsuariosRegistrados();

                // Atualizar o usuário via serviço
                boolean sucesso = UsuarioService.atualizarUsuario(usuarioLogado.getNomeUsuario(), usuarioAtualizado);
                if (sucesso) {
                    System.out.println("Atualização do usuário retornou sucesso!");

                    // Mostrar usuários após a atualização
                    UsuarioService.listarUsuariosRegistrados();

                    // Atualizar o usuário logado com os novos dados
                    usuarioLogado = usuarioAtualizado;

                    // Atualizar a interface (rótulo de usuário e título da janela)
                    usuarioLabel.setText("Olá, " + usuarioLogado.getNome());
                    atualizarTituloJanela();

                    // Mostrar mensagem de sucesso
                    JOptionPane.showMessageDialog(this,
                            "Perfil atualizado com sucesso!");

                    // Voltar para a tela principal
                    voltarParaPainelPrincipal();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao atualizar perfil. Tente novamente.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        ActionListener excluirListener = e -> {
            // Confirmação antes de excluir a conta
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirmar == JOptionPane.YES_OPTION) {
                // Obter o painel de edição - precisa encontrar o componente JanelaEditarPerfil
                // que pode estar em vários níveis na hierarquia de containers
                JButton sourceButton = (JButton) e.getSource();
                Container container = sourceButton.getParent();
                JanelaEditarPerfil painelEdicao = null;

                // Procurar pelo componente JanelaEditarPerfil na hierarquia
                while (container != null) {
                    if (container instanceof JanelaEditarPerfil) {
                        painelEdicao = (JanelaEditarPerfil) container;
                        break;
                    }
                    container = container.getParent();
                }

                if (painelEdicao == null) {
                    System.err.println("ERRO: Não foi possível encontrar o painel de edição de perfil.");
                    JOptionPane.showMessageDialog(this,
                            "Erro ao processar exclusão da conta. Tente novamente.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                System.out.println("Formulário de edição encontrado, procedendo com exclusão...");

                // Verificar a senha para confirmar a exclusão
                char[] senhaAtual = painelEdicao.getSenhaAtual();
                if (senhaAtual == null || senhaAtual.length == 0) {
                    JOptionPane.showMessageDialog(this,
                            "É necessário inserir sua senha atual para confirmar a exclusão da conta.",
                            "Senha Necessária",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // Verificar se a senha está correta
                String senhaAtualStr = new String(senhaAtual);
                System.out.println("Verificando senha atual: " + (senhaAtualStr.isEmpty() ? "vazia" : "preenchida"));
                System.out.println("Senha registrada para o usuário: " + (usuarioLogado.getSenha() != null ? "definida" : "null"));

                if (!usuarioLogado.verificarSenha(senhaAtualStr)) {
                    System.out.println("ERRO: Verificação de senha falhou");
                    JOptionPane.showMessageDialog(this,
                            "A senha inserida está incorreta.",
                            "Senha Incorreta",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                System.out.println("Senha verificada com sucesso!");
                System.out.println("Iniciando processo de exclusão de conta...");
                System.out.println("Usuário a ser excluído: " + usuarioLogado.getNomeUsuario() + " (" + usuarioLogado.getNome() + ")");

                // Mostrar usuários antes da exclusão
                UsuarioService.listarUsuariosRegistrados();

                // Excluir o usuário via serviço
                boolean sucesso = UsuarioService.excluirUsuario(usuarioLogado.getNomeUsuario());

                if (sucesso) {
                    System.out.println("Exclusão de usuário retornou sucesso!");

                    // Mostrar usuários após a exclusão
                    UsuarioService.listarUsuariosRegistrados();

                    // Realizar logout após exclusão bem-sucedida
                    JOptionPane.showMessageDialog(this,
                            "Sua conta foi excluída com sucesso.",
                            "Conta Excluída",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Fazer logout e voltar para a tela principal
                    realizarLogout();
                } else {
                    System.out.println("ERRO: Falha na exclusão da conta de usuário.");
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir conta. Tente novamente.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        // Criar e exibir a janela de edição de perfil
        JanelaEditarPerfil painelEditarPerfil = new JanelaEditarPerfil(
                usuarioLogado,
                voltarListener,
                salvarListener,
                excluirListener
        );

        trocarPainel(painelEditarPerfil);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JanelaPrincipal("Cinema e Bar").setVisible(true);
        });
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }    /**
     * Realiza o logout do usuário atual e retorna para a página inicial.
     */    private void realizarLogout() {
        // Limpar o usuário logado
        usuarioLogado = null;

        // Atualizar o botão para mostrar "Login" novamente
        loginButton.setText("Login");

        // Limpar o rótulo do nome do usuário
        usuarioLabel.setText("");

        // Esconder botão de perfil
        perfilButton.setVisible(false);

        // Atualizar título da janela
        atualizarTituloJanela();

        // Mostrar mensagem de logout com sucesso
        JOptionPane.showMessageDialog(this,
                "Logout realizado com sucesso!",
                "Logout",
                JOptionPane.INFORMATION_MESSAGE);

        // Voltar diretamente para a página principal usando trocarPainel
        setContentPane(painelPrincipal);
        // Usar um tamanho consistente para todas as janelas
        setSize(900, 650);
        setLocationRelativeTo(null);
        validate();
        repaint();
    }

    /**
     * Atualiza o título da janela para refletir o estado de login.
     */
    private void atualizarTituloJanela() {
        if (usuarioLogado != null) {
            setTitle("Cinema e Bar - " + usuarioLogado.getNome());
        } else {
            setTitle("Cinema e Bar");
        }
    }
}
