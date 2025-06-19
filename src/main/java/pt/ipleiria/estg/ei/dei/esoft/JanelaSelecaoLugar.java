package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class JanelaSelecaoLugar extends JPanel {
    private JButton btnProximo;
    private JButton btnVoltar;
    private Sessao sessao;
    private JPanel painelLugarSelecionado;
    private Lugar lugarSelecionado;

    // Constantes para a aparência dos lugares
    private static final Color BORDA_NORMAL = Color.GRAY;
    private static final Color BORDA_HOVER = Color.BLUE;
    private static final Color BORDA_SELECIONADO = Lugar.COR_LUGAR_SELECIONADO;
    private static final int LARGURA_BORDA_NORMAL = 1;
    private static final int LARGURA_BORDA_HOVER = 2;
    private static final int LARGURA_BORDA_SELECIONADO = 3;

    // Mapa que associa painéis (componentes visuais) aos objetos Lugar
    private Map<JPanel, Lugar> mapaPainelLugar;

    public JanelaSelecaoLugar(Sessao sessao, ActionListener onVoltar, ActionListener onProximo) {
        this.sessao = sessao;
        this.mapaPainelLugar = new HashMap<>();
        setLayout(new BorderLayout(10, 10));

        // Configurar título e informações da sessão
        configurarPainelTitulo();

        // Configurar painel central com lugares e legenda
        configurarPainelCentral();

        // Configurar botões de navegação
        configurarBotoes(onVoltar, onProximo);
    }

    private void configurarPainelTitulo() {
        JPanel painelTitulo = new JPanel();
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));

        JLabel labelTitulo = new JLabel("Selecione um lugar para " + sessao.getFilme().getNome());
        labelTitulo.setFont(new Font(labelTitulo.getFont().getName(), Font.BOLD, 16));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelTitulo);

        JLabel labelSala = new JLabel("Sala: " + sessao.getNomeSala() + " - " + sessao.getDataHoraFormatada());
        labelSala.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelSala);

        // Adicionar informação de acessibilidade da sala
        String acessibilidade = sessao.getSala().getAcessibilidade().equals("sim") ? "Com acessibilidade" : "Sem acessibilidade";
        JLabel labelAcessibilidade = new JLabel(acessibilidade);
        labelAcessibilidade.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelAcessibilidade);

        // Adicionar informação de ocupação da sala
        JLabel labelOcupacao = new JLabel(String.format("Lugares disponíveis: %d de %d (%.1f%% livre)",
                sessao.getSala().getLugaresDisponiveis(),
                sessao.getSala().getTotalLugares(),
                100 - sessao.getSala().getPercentualOcupacao()));
        labelOcupacao.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(labelOcupacao);

        add(painelTitulo, BorderLayout.NORTH);
    }

    private void configurarPainelCentral() {
        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Criar lugares e legenda
        JPanel painelLugaresExterno = criarPainelLugares();
        painelCentral.add(painelLugaresExterno, BorderLayout.CENTER);
        painelCentral.add(criarPainelLegenda(), BorderLayout.SOUTH);
        add(painelCentral, BorderLayout.CENTER);
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

        // Determinar o número de filas e colunas com base na sala
        int filas = sessao.getSala().getLugares().size() / 10; // Assumindo que cada sala tem 10 colunas
        int colunas = 10; // Número padrão de colunas

        // Criar grid de lugares com espaçamento mínimo
        JPanel gridLugares = new JPanel(new GridLayout(filas, colunas, 2, 2));
        gridLugares.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Criar lugares a partir do objeto Sala
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < colunas; j++) {
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
        // Obter o objeto Lugar da sala
        Lugar lugar = sessao.getSala().getLugar(fila, coluna);

        if (lugar == null) {
            // Caso o lugar não exista na sala (não deve acontecer, mas como precaução)
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(35, 35));
            panel.setBackground(Color.WHITE);
            return panel;
        }

        // Criar o componente visual para representar o lugar
        JPanel painelLugar = new JPanel(new BorderLayout());
        painelLugar.setPreferredSize(new Dimension(35, 35)); // Tamanho reduzido para caber melhor

        // Aplicar as propriedades visuais de acordo com o objeto Lugar
        painelLugar.setBackground(lugar.getCorFundo());
        painelLugar.setOpaque(true); // Importante para garantir que a cor seja exibida

        // Adicionar label com identificação do lugar
        JLabel labelLugar = new JLabel(lugar.getFila() + 1 + "" + (lugar.getColuna() + 1));
        labelLugar.setForeground(lugar.getCorTexto());
        labelLugar.setHorizontalAlignment(SwingConstants.CENTER);
        labelLugar.setFont(new Font(labelLugar.getFont().getName(), Font.BOLD, 10));
        painelLugar.add(labelLugar, BorderLayout.CENTER);

        painelLugar.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));

        // Adicionar evento de clique apenas para lugares disponíveis
        if (!lugar.isOcupado()) {
            painelLugar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            painelLugar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selecionarLugar(painelLugar);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (painelLugarSelecionado != painelLugar) {
                        painelLugar.setBorder(BorderFactory.createLineBorder(BORDA_HOVER, LARGURA_BORDA_HOVER));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (painelLugarSelecionado != painelLugar) {
                        painelLugar.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
                    }
                }
            });
        }

        // Associar o componente visual ao objeto de modelo
        mapaPainelLugar.put(painelLugar, lugar);

        return painelLugar;
    }

    private JPanel criarPainelLegenda() {
        JPanel painelLegenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        painelLegenda.setBorder(BorderFactory.createTitledBorder("Legenda"));

        // Array com as definições dos itens de legenda: cor, texto, isBorda
        Object[][] legendaItens = {
                {Lugar.COR_LUGAR_DISPONIVEL, "Disponível", false},
                {Lugar.COR_LUGAR_VIP, "VIP (+2.00 €)", false},
                {Lugar.COR_LUGAR_OCUPADO, "Ocupado", false},
                {Lugar.COR_LUGAR_DISPONIVEL, "Selecionado", true}
        };

        // Criar cada item da legenda
        for (Object[] item : legendaItens) {
            JPanel cor = new JPanel();
            cor.setPreferredSize(new Dimension(30, 20));
            cor.setBackground((Color)item[0]);
            cor.setOpaque(true);

            if ((boolean)item[2]) {
                cor.setBorder(BorderFactory.createLineBorder(BORDA_SELECIONADO, LARGURA_BORDA_SELECIONADO));
            } else {
                cor.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
            }

            // Se for o item VIP, adicionar um texto em branco para ilustrar
            if (item[0] == Lugar.COR_LUGAR_VIP) {
                JLabel vipLabel = new JLabel("A1");
                vipLabel.setForeground(Lugar.COR_TEXTO_VIP);
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
        setLugarSelecionado(mapaPainelLugar.get(painel));
        atualizarSelecao(painel);
    }

    private void atualizarSelecao(JPanel painelSelecionado) {
        // Restaurar borda do lugar anteriormente selecionado
        if (painelLugarSelecionado != null && painelLugarSelecionado != painelSelecionado) {
            painelLugarSelecionado.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
        }

        // Atualizar seleção
        painelLugarSelecionado = painelSelecionado;
        if (painelSelecionado != null) {
            painelSelecionado.setBorder(BorderFactory.createLineBorder(BORDA_SELECIONADO, LARGURA_BORDA_SELECIONADO));
        }
    }

    public void setLugarSelecionado(Lugar lugar) {
        this.lugarSelecionado = lugar;
        btnProximo.setEnabled(lugar != null);
    }

    // Getters
    public JButton getBtnProximo() { return btnProximo; }
    public JButton getBtnVoltar() { return btnVoltar; }
    public Sessao getSessao() { return sessao; }

    public String getLugarSelecionado() {
        if (lugarSelecionado == null) return null;
        return lugarSelecionado.getIdentificacao();
    }

    public double getPrecoTotal() {
        if (lugarSelecionado == null) return sessao.getPreco();
        return lugarSelecionado.calcularPreco(sessao.getPreco());
    }

    // Getter para o objeto Lugar selecionado
    public Lugar getLugarSelecionadoObjeto() {
        return lugarSelecionado;
    }
}
