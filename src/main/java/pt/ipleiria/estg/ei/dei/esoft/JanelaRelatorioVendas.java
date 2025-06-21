package pt.ipleiria.estg.ei.dei.esoft;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class JanelaRelatorioVendas extends JDialog {

    private final JPanel painelGraficos;

    public JanelaRelatorioVendas(JFrame parent) {
        super(parent, "📊 Relatório de Vendas", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Topo com botões
        JButton btnFilmes = new JButton("🎞️ Filmes");
        JButton btnComidaBebida = new JButton("🍿 Itens Bar");
        JButton btnVendasTempo = new JButton("💰 Vendas ao Longo do Tempo");
        JButton btnEstatisticas = new JButton("📊 Estatísticas");


        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.add(btnFilmes);
        painelBotoes.add(btnComidaBebida);
        painelBotoes.add(btnVendasTempo);
        painelBotoes.add(btnEstatisticas);

        add(painelBotoes, BorderLayout.NORTH);

        // Painel central para alternar os gráficos
        painelGraficos = new JPanel(new BorderLayout());
        add(painelGraficos, BorderLayout.CENTER);

        // Eventos
        btnFilmes.addActionListener(e -> mostrarGraficoFilmes());
        btnComidaBebida.addActionListener(e -> mostrarGraficoItens());
        btnVendasTempo.addActionListener(e -> mostrarGraficoVendasPorTempo());
        btnEstatisticas.addActionListener(e -> mostrarEstatisticas());

        // Mostra o gráfico inicial
        mostrarGraficoFilmes();
    }

    private void mostrarGraficoFilmes() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<Compra> compras = PersistenciaService.carregarCompras();
        List<Sessao> sessoes = PersistenciaService.carregarSessoes();

        // Mapeia as sessões por ID para acesso rápido
        Map<String, Sessao> sessoesPorId = new HashMap<>();
        for (Sessao s : sessoes) {
            sessoesPorId.put(s.getId(), s);
        }

        Map<String, Integer> bilhetesPorFilme = new HashMap<>();

        for (Compra compra : compras) {
            if (compra.getBilhetes() == null || compra.getBilhetes().isEmpty()) {
                continue;
            }

            for (Bilhete b : compra.getBilhetes()) {
                Sessao sessao = sessoesPorId.get(b.getIdSessao());
                if (sessao != null && sessao.getFilme() != null) {
                    String nomeFilme = sessao.getFilme().getNome();

                    bilhetesPorFilme.put(nomeFilme, bilhetesPorFilme.getOrDefault(nomeFilme, 0) + 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : bilhetesPorFilme.entrySet()) {
            dataset.addValue(entry.getValue(), "Bilhetes", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Bilhetes Vendidos por Filme",
                "Filme",
                "Quantidade",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(33, 150, 243));
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setMaximumBarWidth(0.08);


        atualizarGrafico(chart);
    }

    private void mostrarGraficoItens() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<Compra> compras = PersistenciaService.carregarCompras();
        Map<String, Integer> contagemItens = new HashMap<>();

        for (Compra compra : compras) {
            for (Item item : compra.getItensBar()) {
                String nome = item.getNome().trim().toLowerCase(); // normaliza nome
                contagemItens.put(nome, contagemItens.getOrDefault(nome, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : contagemItens.entrySet()) {
            String nomeCapitalizado = capitalizeNome(entry.getKey());
            dataset.addValue(entry.getValue(), "Itens", nomeCapitalizado);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Itens Vendidos",
                "Produto",
                "Quantidade",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(33, 150, 243));
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setMaximumBarWidth(0.08);

        atualizarGrafico(chart);
    }

    private void mostrarGraficoVendasPorTempo() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Compra> compras = PersistenciaService.carregarCompras();

        Map<String, Double> montantePorData = new TreeMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Agrupa por dia

        for (Compra compra : compras) {
            String dataStr = formatter.format(compra.getDataHora());
            montantePorData.put(dataStr,
                    montantePorData.getOrDefault(dataStr, 0.0) + compra.getPrecoTotal());
        }

        for (Map.Entry<String, Double> entry : montantePorData.entrySet()) {
            dataset.addValue(entry.getValue(), "Vendas (€)", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Total de Vendas por Dia",
                "Data",
                "Montante (€)",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, new Color(255, 0, 0));
        plot.setRenderer(renderer);

        atualizarGrafico(chart);
    }

    private void mostrarEstatisticas() {
        List<Compra> compras = PersistenciaService.carregarCompras();
        List<Sessao> sessoes = PersistenciaService.carregarSessoes();

        Map<String, Sessao> sessaoPorId = sessoes.stream()
                .collect(Collectors.toMap(Sessao::getId, s -> s));

        Map<String, Integer> filmesVendidos = new HashMap<>();
        Map<String, Integer> itensVendidos = new HashMap<>();
        Map<DayOfWeek, Integer> vendasPorDiaSemana = new HashMap<>();

        for (Compra c : compras) {
            // Filmes
            if (c.getBilhetes() == null || c.getBilhetes().isEmpty()) {
                continue; // Ignora compras sem bilhetes
            }
            for (Bilhete b : c.getBilhetes()) {
                Sessao s = sessaoPorId.get(b.getIdSessao());
                if (s != null) {
                    String filme = s.getFilme().getNome();
                    filmesVendidos.put(filme, filmesVendidos.getOrDefault(filme, 0) + 1);
                }
            }

            // Produtos
            for (Item i : c.getItensBar()) {
                String nome = i.getNome().trim();
                itensVendidos.put(nome, itensVendidos.getOrDefault(nome, 0) + 1);
            }

            // Dias da semana
            LocalDateTime data = c.getDataHora().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            DayOfWeek dia = data.getDayOfWeek();
            vendasPorDiaSemana.put(dia, vendasPorDiaSemana.getOrDefault(dia, 0) + 1);
        }

        // Ordena Top 3 Filmes
        List<Map.Entry<String, Integer>> topFilmes = filmesVendidos.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .toList();

        // Top 3 Produtos
        List<Map.Entry<String, Integer>> topProdutos = itensVendidos.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .toList();

        // Dias com mais e menos vendas
        List<Map.Entry<DayOfWeek, Integer>> diasOrdenados = vendasPorDiaSemana.entrySet().stream()
                .sorted(Map.Entry.<DayOfWeek, Integer>comparingByValue().reversed())
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("📊 **Estatísticas de Vendas**\n\n");

        sb.append("🎬 Top 3 Filmes Mais Vendidos:\n");
        topFilmes.forEach(e -> sb.append(" - ").append(e.getKey()).append(": ").append(e.getValue()).append(" bilhetes\n"));

        sb.append("\n🍿 Top 3 Produtos do Bar:\n");
        topProdutos.forEach(e -> sb.append(" - ").append(e.getKey()).append(": ").append(e.getValue()).append(" unidades\n"));

        sb.append("\n📅 Desempenho por Dia da Semana:\n");
        sb.append(" 🟢 Dias com mais vendas:\n");
        diasOrdenados.stream().limit(2)
                .forEach(e -> sb.append("   - ").append(e.getKey()).append(": ").append(e.getValue()).append(" compras\n"));

        sb.append(" 🔴 Dias com menos vendas:\n");
        diasOrdenados.stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(2)
                .forEach(e -> sb.append("   - ").append(e.getKey()).append(": ").append(e.getValue()).append(" compras\n"));

        // Mostrar numa área formatada
        String html = """
                <html>
                    <body style='font-family: Consolas, monospace; text-align: center; background-color: #f5f5f5; padding: 20px'>
                        <h1>📊 Estatísticas de Vendas</h1>
                
                        <h2>🎬 Top 3 Filmes Mais Vendidos</h2>
                        %s
                
                        <h2>🍿 Top 3 Produtos do Bar</h2>
                        %s
                
                        <h2>📅 Desempenho por Dia da Semana</h2>
                        <b>🟢 Dias com mais vendas:</b><br/>%s
                        <br/><br/>
                        <b>🔴 Dias com menos vendas:</b><br/>%s
                    </body>
                </html>
                """;

        String filmesHtml = topFilmes.stream()
                .map(e -> e.getKey() + ": " + e.getValue() + " bilhetes")
                .collect(Collectors.joining("<br/>"));

        String produtosHtml = topProdutos.stream()
                .map(e -> e.getKey() + ": " + e.getValue() + " unidades")
                .collect(Collectors.joining("<br/>"));

        String maisVendasHtml = diasOrdenados.stream().limit(2)
                .map(e -> e.getKey() + ": " + e.getValue() + " compras")
                .collect(Collectors.joining("<br/>"));

        String menosVendasHtml = diasOrdenados.stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(2)
                .map(e -> e.getKey() + ": " + e.getValue() + " compras")
                .collect(Collectors.joining("<br/>"));

        String htmlFinal = String.format(html, filmesHtml, produtosHtml, maisVendasHtml, menosVendasHtml);

        JTextPane textPane = new JTextPane();
        textPane.setFocusable(false);
        textPane.setHighlighter(null);
        textPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); // cursor normal
        textPane.setContentType("text/html");
        textPane.setText(htmlFinal);
        textPane.setEditable(false);
        textPane.setBackground(new Color(245, 245, 245));

        painelGraficos.removeAll();
        painelGraficos.add(new JScrollPane(textPane), BorderLayout.CENTER);
        painelGraficos.revalidate();
        painelGraficos.repaint();
    }

    private String capitalizeNome(String nome) {
        return Character.toUpperCase(nome.charAt(0)) + nome.substring(1);
    }

    private void atualizarGrafico(JFreeChart chart) {

        painelGraficos.removeAll();
        painelGraficos.add(new ChartPanel(chart), BorderLayout.CENTER);
        painelGraficos.revalidate();
        painelGraficos.repaint();
    }
}

