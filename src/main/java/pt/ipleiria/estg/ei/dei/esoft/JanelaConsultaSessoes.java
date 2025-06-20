package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class JanelaConsultaSessoes extends JDialog {
    private final JComboBox<LocalDate> dataComboBox;
    private final JPanel painelSessoes;
    private final List<Sessao> todasSessoes;    private final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public JanelaConsultaSessoes(Frame parent, List<Sessao> sessoes) {
        super(parent, "Consultar Sessões por Dia", true);
        this.todasSessoes = sessoes;

        // Configurar a janela
        setSize(800, 600);
        setLocationRelativeTo(parent);

        // Painel principal com layout e margens
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel superior com título e seletor de data
        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));

        // Título
        JLabel titulo = new JLabel("Consultar Sessões por Dia");
        titulo.setFont(new Font(titulo.getFont().getName(), Font.BOLD, 20));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelSuperior.add(titulo, BorderLayout.NORTH);

        // Painel para seleção de data
        JPanel painelData = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelData = new JLabel("Selecione a data: ");
        labelData.setFont(new Font(labelData.getFont().getName(), Font.BOLD, 14));

        // Obter datas disponíveis e criar o ComboBox
        Set<LocalDate> datasDisponiveis = sessoes.stream()
                .map(s -> s.getDataHora().toLocalDate())
                .sorted()
                .collect(Collectors.toCollection(TreeSet::new));

        dataComboBox = new JComboBox<>(datasDisponiveis.toArray(new LocalDate[0]));
        dataComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof LocalDate) {
                    value = ((LocalDate) value).format(formatoData);
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        dataComboBox.addActionListener(e -> atualizarSessoes());

        painelData.add(labelData);
        painelData.add(dataComboBox);
        painelSuperior.add(painelData, BorderLayout.CENTER);

        // Painel para as sessões
        painelSessoes = new JPanel();
        painelSessoes.setLayout(new BoxLayout(painelSessoes, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(painelSessoes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Painel inferior com botão fechar
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        painelInferior.add(btnFechar);

        // Adicionar componentes ao painel principal
        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);

        // Configurar o painel principal como conteúdo da janela
        setContentPane(painelPrincipal);

        // Mostrar sessões iniciais se houver data selecionada
        if (dataComboBox.getSelectedItem() != null) {
            atualizarSessoes();
        }
    }

    private void atualizarSessoes() {
        painelSessoes.removeAll();
        LocalDate dataSelecionada = (LocalDate) dataComboBox.getSelectedItem();

        // Filtrar e ordenar sessões da data selecionada
        List<Sessao> sessoesData = todasSessoes.stream()
                .filter(s -> s.getDataHora().toLocalDate().equals(dataSelecionada))
                .sorted(Comparator.comparing(Sessao::getDataHora))
                .collect(Collectors.toList());

        // Adicionar painel para cada sessão
        for (Sessao sessao : sessoesData) {
            JPanel painelSessao = criarPainelSessao(sessao);
            painelSessoes.add(painelSessao);
            painelSessoes.add(Box.createVerticalStrut(10)); // Espaçamento entre painéis
        }

        painelSessoes.revalidate();
        painelSessoes.repaint();
    }

    private JPanel criarPainelSessao(Sessao sessao) {
        JPanel painelSessao = new JPanel();
        painelSessao.setLayout(new BoxLayout(painelSessao, BoxLayout.Y_AXIS));
        painelSessao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        painelSessao.setBackground(Color.WHITE);        // Informações da sessão
        JLabel labelDataHora = new JLabel("Data e Hora: " + formatoData.format(sessao.getDataHora()) + " " +
                sessao.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm")));
        JLabel labelFilme = new JLabel("Filme: " + sessao.getFilme().getNome());
        JLabel labelSala = new JLabel("Sala: " + sessao.getSala().getNome());
        JLabel labelLugares = new JLabel("Lugares disponíveis: " +
                sessao.getSala().getLugares().stream().filter(l -> !l.isOcupado()).count());        // Estilo das labels
        Font fonte = new Font(labelDataHora.getFont().getName(), Font.PLAIN, 14);
        labelDataHora.setFont(fonte);
        labelFilme.setFont(fonte);
        labelSala.setFont(fonte);
        labelLugares.setFont(fonte);

        // Alinhar à esquerda
        labelDataHora.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelFilme.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelSala.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelLugares.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Adicionar labels ao painel
        painelSessao.add(labelDataHora);
        painelSessao.add(Box.createVerticalStrut(5));
        painelSessao.add(labelFilme);
        painelSessao.add(Box.createVerticalStrut(5));
        painelSessao.add(labelSala);
        painelSessao.add(Box.createVerticalStrut(5));
        painelSessao.add(labelLugares);

        return painelSessao;
    }
}
