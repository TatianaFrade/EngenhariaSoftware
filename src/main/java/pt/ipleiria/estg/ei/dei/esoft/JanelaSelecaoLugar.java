package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JanelaSelecaoLugar extends JPanel {
    private JButton btnProximo;
    private JButton btnVoltar;
    private Sessao sessao;
    private JPanel lugarSelecionado;
    private static final int FILAS = 8;
    private static final int COLUNAS = 10;

    // Constantes para cores dos lugares
    private static final Color COR_LUGAR_DISPONIVEL = Color.LIGHT_GRAY;
    private static final Color COR_LUGAR_VIP = Color.BLACK;
    private static final Color COR_LUGAR_OCUPADO = Color.RED;
    private static final Color COR_LUGAR_SELECIONADO = new Color(0, 120, 215);
    private static final Color COR_TEXTO_VIP = Color.WHITE;

    public JanelaSelecaoLugar(Sessao sessao, ActionListener onVoltar, ActionListener onProximo) {
        this.sessao = sessao;
        setLayout(new BorderLayout(10, 10));

        // Adicionar título
        JPanel painelTitulo = new JPanel();
        JLabel labelTitulo = new JLabel("Selecione um lugar para " + sessao.getFilme().getNome());
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.BOLD, 16));
        painelTitulo.add(labelTitulo);
        JLabel labelSala = new JLabel("Sala: " + sessao.getSala() + " - " + sessao.getDataHoraFormatada());
        painelTitulo.add(labelSala);
        add(painelTitulo, BorderLayout.NORTH);

        // Criar painel central com lugares e legenda
        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Criar lugares e legenda
        JPanel painelLugaresExterno = criarPainelLugares();
        painelCentral.add(painelLugaresExterno, BorderLayout.CENTER);
        painelCentral.add(criarPainelLegenda(), BorderLayout.SOUTH);
        add(painelCentral, BorderLayout.CENTER);

        // Configuração dos botões de navegação
        configurarBotoes(onVoltar, onProximo);
    }

    private JPanel criarPainelLugares() {
        // Painel principal com borderlayout
        JPanel painelPrincipal = new JPanel(new BorderLayout(0, 10));

        // Criar tela no topo
        JPanel telaCinema = new JPanel();
        telaCinema.setPreferredSize(new Dimension(450, 25));
        telaCinema.setBackground(Color.DARK_GRAY);
        JLabel labelTela = new JLabel("TELA");
        labelTela.setForeground(Color.WHITE);
        telaCinema.add(labelTela);

        // Centralizar a tela
        JPanel painelTela = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTela.add(telaCinema);
        painelPrincipal.add(painelTela, BorderLayout.NORTH);

        // Criar grid de lugares com espaçamento mínimo
        JPanel gridLugares = new JPanel(new GridLayout(FILAS, COLUNAS, 2, 2));
        gridLugares.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Criar lugares
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUNAS; j++) {
                JPanel lugar = criarLugar(i, j);
                gridLugares.add(lugar);
            }
        }

        // Centralizar a grade de lugares
        JPanel painelCentralizador = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelCentralizador.add(gridLugares);
        painelPrincipal.add(painelCentralizador, BorderLayout.CENTER);

        return painelPrincipal;
    }

    // Método auxiliar para criar um lugar individual com tipo fixo
    private JPanel criarLugar(int fila, int coluna) {
        JPanel lugar = new JPanel(new BorderLayout());
        lugar.setPreferredSize(new Dimension(35, 35)); // Tamanho reduzido para caber melhor

        // Determinar tipo de lugar com base na posição fixa
        Color corFundo;
        Color corTexto = Color.BLACK;
        boolean ocupado = false;
        boolean vip = false;

        // Aumentar número de lugares VIP para que sejam mais visíveis
        // VIP: Filas C, D, E e F (2, 3, 4, 5), colunas centrais (2-7)
        if ((fila >= 2 && fila <= 5) && (coluna >= 2 && coluna <= 7)) {
            corFundo = COR_LUGAR_VIP;
            corTexto = COR_TEXTO_VIP;
            vip = true;
        }
        // Definir alguns lugares ocupados (distribuídos pela sala)
        else if ((fila == 0 && coluna == 2) ||
                (fila == 1 && coluna == 8) ||
                (fila == 6 && coluna == 3) ||
                (fila == 7 && coluna == 6)) {
            corFundo = COR_LUGAR_OCUPADO;
            ocupado = true;
        }
        // Restantes são lugares disponíveis normais
        else {
            corFundo = COR_LUGAR_DISPONIVEL;
        }

        // Garantir que a cor de fundo seja aplicada adequadamente
        lugar.setBackground(corFundo);
        lugar.setOpaque(true); // Importante para garantir que a cor seja exibida
        lugar.putClientProperty("ocupado", ocupado);
        lugar.putClientProperty("vip", vip);
        lugar.putClientProperty("fila", fila);
        lugar.putClientProperty("coluna", coluna);

        // Adicionar label com identificação do lugar
        char letraFila = (char)('A' + fila);
        JLabel labelLugar = new JLabel(letraFila + "" + (coluna + 1));
        labelLugar.setForeground(corTexto);
        labelLugar.setHorizontalAlignment(SwingConstants.CENTER);
        labelLugar.setFont(new Font(labelLugar.getFont().getName(), Font.BOLD, 10)); // Fonte reduzida
        lugar.add(labelLugar, BorderLayout.CENTER);

        lugar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Adicionar evento de clique apenas para lugares disponíveis
        if (!ocupado) {
            lugar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lugar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selecionarLugar(lugar);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (lugarSelecionado != lugar) {
                        lugar.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (lugarSelecionado != lugar) {
                        lugar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                    }
                }
            });
        }

        return lugar;
    }

    private JPanel criarPainelLegenda() {
        JPanel painelLegenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        painelLegenda.setBorder(BorderFactory.createTitledBorder("Legenda"));

        // Array com as definições dos itens de legenda: cor, texto, isBorda
        Object[][] legendaItens = {
                {COR_LUGAR_DISPONIVEL, "Disponível", false},
                {COR_LUGAR_VIP, "VIP (+2.00 €)", false},
                {COR_LUGAR_OCUPADO, "Ocupado", false},
                {COR_LUGAR_DISPONIVEL, "Selecionado", true}
        };

        // Criar cada item da legenda
        for (Object[] item : legendaItens) {
            JPanel cor = new JPanel();
            cor.setPreferredSize(new Dimension(30, 20));
            cor.setBackground((Color)item[0]);
            cor.setOpaque(true);

            if ((boolean)item[2]) {
                cor.setBorder(BorderFactory.createLineBorder(COR_LUGAR_SELECIONADO, 3));
            } else {
                cor.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            }

            // Se for o item VIP, adicionar um texto em branco para ilustrar
            if (item[0] == COR_LUGAR_VIP) {
                JLabel vipLabel = new JLabel("A1");
                vipLabel.setForeground(COR_TEXTO_VIP);
                vipLabel.setFont(new Font(vipLabel.getFont().getName(), Font.BOLD, 10));
                vipLabel.setHorizontalAlignment(SwingConstants.CENTER);
                cor.setLayout(new BorderLayout());
                cor.add(vipLabel, BorderLayout.CENTER);
            }

            painelLegenda.add(cor);
            painelLegenda.add(new JLabel((String)item[1]));
        }

        return painelLegenda;
    }

    private void configurarBotoes(ActionListener onVoltar, ActionListener onProximo) {
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVoltar = new JButton("Voltar");
        btnProximo = new JButton("Próximo");

        btnVoltar.addActionListener(onVoltar != null ? onVoltar : e -> {});
        btnProximo.addActionListener(onProximo != null ? onProximo : e -> {});
        btnProximo.setEnabled(false); // Desabilitado até que um lugar seja selecionado

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnProximo);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void selecionarLugar(JPanel painel) {
        // Restaurar borda do lugar anteriormente selecionado
        if (lugarSelecionado != null) {
            lugarSelecionado.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }

        // Atualizar seleção
        lugarSelecionado = painel;
        painel.setBorder(BorderFactory.createLineBorder(COR_LUGAR_SELECIONADO, 3));

        // Habilitar botão próximo
        btnProximo.setEnabled(true);
    }

    // Getters
    public JButton getBtnProximo() { return btnProximo; }
    public JButton getBtnVoltar() { return btnVoltar; }
    public Sessao getSessao() { return sessao; }

    public String getLugarSelecionado() {
        if (lugarSelecionado == null) return null;

        int fila = (int) lugarSelecionado.getClientProperty("fila");
        int coluna = (int) lugarSelecionado.getClientProperty("coluna");
        boolean vip = (boolean) lugarSelecionado.getClientProperty("vip");

        char letraFila = (char)('A' + fila);
        return letraFila + "" + (coluna + 1) + (vip ? " (VIP)" : "");
    }

    public double getPrecoTotal() {
        double preco = sessao.getPreco();

        // Adicional para lugares VIP
        if (lugarSelecionado != null && (boolean) lugarSelecionado.getClientProperty("vip")) {
            preco += 2.00;
        }

        return preco;
    }
}
