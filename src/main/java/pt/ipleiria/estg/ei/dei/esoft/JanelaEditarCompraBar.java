package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class JanelaEditarCompraBar extends JFrame {
    private final Compra compra;
    private final Map<Item, Integer> quantidades = new HashMap<>();
    private final Map<Item, JLabel> etiquetasQuantidade = new HashMap<>();
    private JLabel lblTotal;

    public JanelaEditarCompraBar(Compra compra) {
        this.compra = compra;

        setTitle("Editar Compra");
        setSize(550, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        configurarPainelFormulario();
    }

    private void configurarPainelFormulario() {
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new BoxLayout(painelFormulario, BoxLayout.Y_AXIS));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        JLabel lblData = new JLabel("Data da Compra:  " + sdf.format(compra.getDataHora()));
        painelFormulario.add(lblData);


        JLabel lblMetodo = new JLabel("Método de Pagamento:  " + compra.getMetodoPagamento());
        painelFormulario.add(lblMetodo);

        lblTotal = new JLabel("Total (€):  " + String.format("%.2f €", compra.getPrecoTotal()));
        painelFormulario.add(lblTotal);

        painelFormulario.add(new JLabel("Itens do Bar:"));

        JPanel painelItens = new JPanel();
        painelItens.setLayout(new BoxLayout(painelItens, BoxLayout.Y_AXIS));

        List<Item> todosItens = PersistenciaService.carregarItens();
        List<Item> itensAtuais = compra.getItensBar();

        for (Item item : todosItens) {
            int qtdInicial = (int) itensAtuais.stream()
                    .filter(i -> i.getNome().equals(item.getNome()))
                    .count();

            quantidades.put(item, qtdInicial);

            JPanel linha = new JPanel(new BorderLayout());

            // Nome do item com preço
            JLabel nome = new JLabel(item.getNome() + " (" + String.format("%.2f €", item.getPreco()) + ")");
            nome.setPreferredSize(new Dimension(250, 25));
            linha.add(nome, BorderLayout.WEST);

            // Painel com botões e quantidade
            JLabel qtdLabel = new JLabel(String.valueOf(qtdInicial));
            etiquetasQuantidade.put(item, qtdLabel);

            JButton btnMais = new JButton("+");
            JButton btnMenos = new JButton("–");

            btnMais.addActionListener(e -> alterarQuantidade(item, +1));
            btnMenos.addActionListener(e -> alterarQuantidade(item, -1));

            JPanel painelControlo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            painelControlo.add(btnMenos);
            painelControlo.add(qtdLabel);
            painelControlo.add(btnMais);

            linha.add(painelControlo, BorderLayout.EAST);
            painelItens.add(linha);
        }

        // Scroll com velocidade aumentada
        JScrollPane scrollItens = new JScrollPane(painelItens);
        scrollItens.setPreferredSize(new Dimension(400, 220));
        scrollItens.getVerticalScrollBar().setUnitIncrement(20);
        painelFormulario.add(scrollItens);

        JButton btnGuardar = new JButton("💾 Guardar");
        btnGuardar.addActionListener(e -> {
            compra.getItensBar().clear();
            for (Map.Entry<Item, Integer> entry : quantidades.entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    compra.addItemBar(entry.getKey());
                }
            }
            compra.setPrecoTotal(compra.calcularPrecoTotal());
            JOptionPane.showMessageDialog(this, "Compra atualizada com sucesso!");
            PersistenciaService.editarCompra(compra);
            dispose();
        });

        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelInferior.add(btnGuardar);

        add(new JScrollPane(painelFormulario), BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);
    }

    private void alterarQuantidade(Item item, int delta) {
        int atual = quantidades.get(item);
        int novoValor = Math.max(0, atual + delta);
        quantidades.put(item, novoValor);
        etiquetasQuantidade.get(item).setText(String.valueOf(novoValor));
        atualizarTotal();
    }

    private void atualizarTotal() {
        double precoItens = quantidades.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPreco() * e.getValue())
                .sum();

        double precoBilhetes = compra.getBilhetes() != null
                ? compra.getBilhetes().stream().mapToDouble(Bilhete::getPreco).sum()
                : 0.0;

        double total = precoItens + precoBilhetes;
        DecimalFormat df = new DecimalFormat("0.00");
        lblTotal.setText(df.format(total) + " €");
    }
}