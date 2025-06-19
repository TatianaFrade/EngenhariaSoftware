package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JButton loginButton;

    private JPanel painelAtual;
    private List<Filme> filmes;
    private List<Sessao> sessoes;

    public JanelaPrincipal(String title) {
        super(title);
        inicializarDados();
        criarPainelPrincipal();
        setContentPane(painelPrincipal);
        painelAtual = painelPrincipal;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
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
    }

    private void criarPainelPrincipal() {
        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        loginButton = new JButton("Login");

        JPanel topButtonPanel = new JPanel();
        topButtonPanel.add(loginButton);
        painelPrincipal.add(topButtonPanel, BorderLayout.NORTH);
        comprarBilheteButton = new JButton("Comprar Bilhete");
        comprarProdutosButton = new JButton("Comprar Produtos");
        verMenusButton = new JButton("Ver Menus");
        consultarSessoesPorDiaButton = new JButton("Consultar Sessões por Dia");

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.add(comprarBilheteButton);
        centerPanel.add(comprarProdutosButton);
        centerPanel.add(verMenusButton);
        centerPanel.add(consultarSessoesPorDiaButton);

        JPanel paddedCenterPanel = new JPanel(new BorderLayout());
        paddedCenterPanel.add(centerPanel, BorderLayout.CENTER);
        paddedCenterPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelPrincipal.add(paddedCenterPanel, BorderLayout.CENTER);

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
    }
    private void mostrarJanelaSelecaoLugar(Sessao sessaoSeleccionada) {
        // Criar o painel de seleção de lugar
        JanelaSelecaoLugar painelLugares = new JanelaSelecaoLugar(sessaoSeleccionada, null, null);

        // Adicionar listener para o botão Voltar
        painelLugares.getBtnVoltar().addActionListener(e -> {
            // Voltar para a seleção de sessão
            mostrarJanelaSelecaoSessao(sessaoSeleccionada.getFilme());
        });

        // Adicionar listener para o botão Próximo, mas sem redirecionamento
        painelLugares.getBtnProximo().addActionListener(e -> {
            String lugarSelecionado = painelLugares.getLugarSelecionado();
            if (lugarSelecionado != null) {
                // Confirmar a compra do bilhete
                int resposta = JOptionPane.showConfirmDialog(
                        this,
                        "Confirmar a compra do bilhete?\nLugar: " + lugarSelecionado + "\n" +
                                "Preço: " + String.format("%.2f €", painelLugares.getPrecoTotal()),
                        "Confirmar Compra",
                        JOptionPane.YES_NO_OPTION
                );

                if (resposta == JOptionPane.YES_OPTION) {
                    // Marcar o lugar como ocupado na sala
                    Lugar lugar = painelLugares.getLugarSelecionadoObjeto();
                    if (lugar != null) {
                        sessaoSeleccionada.getSala().ocuparLugar(lugar.getFila(), lugar.getColuna());
                        JOptionPane.showMessageDialog(
                                this,
                                "Compra realizada com sucesso!\n" +
                                        "Lugar: " + lugarSelecionado + "\n" +
                                        "Preço: " + String.format("%.2f €", painelLugares.getPrecoTotal()),
                                "Compra Finalizada",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        // Voltar para o menu principal após a compra
                        voltarParaPainelPrincipal();
                    }
                }
                // Se resposta for Não, permanece na tela de seleção de lugar
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um lugar primeiro.");
            }
        });

        trocarPainel(painelLugares);
    }

    private void voltarParaPainelPrincipal() {
        trocarPainel(painelPrincipal);
    }    private void trocarPainel(JPanel novoPainel) {
        setContentPane(novoPainel);
        painelAtual = novoPainel;

        // Ajustar o tamanho da janela para melhor exibir os painéis
        if (novoPainel instanceof JanelaSelecaoFilme || novoPainel instanceof JanelaSelecaoSessao || novoPainel instanceof JanelaSelecaoLugar) {
            setSize(900, 600);
        } else {
            setSize(600, 400);
        }

        setLocationRelativeTo(null); // Centraliza novamente após redimensionar
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JanelaPrincipal("Cinema e Bar").setVisible(true);
        });
    }
}
