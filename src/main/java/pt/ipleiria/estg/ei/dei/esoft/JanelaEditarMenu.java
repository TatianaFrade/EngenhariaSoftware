package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class JanelaEditarMenu extends JDialog {
    private JTextField campoNome;
    private JTextField campoDescricao;
    private JTextField campoDesconto;
    private JCheckBox checkAtivo;
    private JComboBox<Filme> comboFilmes;
    private JLabel labelTotal;

    private final Map<String, Integer> quantidades = new HashMap<>();
    private final Map<String, JLabel> etiquetasQuantidade = new HashMap<>();
    private final Map<String, Double> precosItens = new HashMap<>();

    private final double PRECO_FILME = 7.0;
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final Runnable onClose;

    public JanelaEditarMenu(Window parent, Menu menu, Runnable onClose) {
        super(parent, "Editar Menu", ModalityType.APPLICATION_MODAL);
        this.onClose = onClose;

        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        campoNome = new JTextField(menu.getNome());
        campoDescricao = new JTextField(menu.getDescricao());
        campoDesconto = new JTextField("0");
        checkAtivo = new JCheckBox("Menu Ativo", menu.isAtivo());

        List<Filme> filmes = PersistenciaService.carregarFilmes();
        comboFilmes = new JComboBox<>(filmes.toArray(new Filme[0]));
        for (Filme f : filmes) {
            if (f.getNome().equalsIgnoreCase(menu.getFilme())) {
                comboFilmes.setSelectedItem(f);
                break;
            }
        }

        List<Item> todosItens = PersistenciaService.carregarItens();
        List<String> itensSelecionados = menu.getItens() != null ? menu.getItens() : new ArrayList<>();

        JPanel painelItens = new JPanel();
        painelItens.setLayout(new BoxLayout(painelItens, BoxLayout.Y_AXIS));
        painelItens.setBorder(BorderFactory.createTitledBorder("Itens do Menu"));

        for (Item item : todosItens) {
            String nome = item.getNome();
            double preco = item.getPreco();
            precosItens.put(nome, preco);

            int qtd = (int) itensSelecionados.stream()
                    .filter(itemNome -> itemNome.trim().equalsIgnoreCase(nome.trim()))
                    .count();
            quantidades.put(nome, qtd);

            JPanel linha = new JPanel(new BorderLayout());
            JLabel lblNome = new JLabel(nome + " (" + df.format(preco) + " €)");
            lblNome.setPreferredSize(new Dimension(250, 25));
            linha.add(lblNome, BorderLayout.WEST);

            JLabel lblQtd = new JLabel(String.valueOf(qtd));
            etiquetasQuantidade.put(nome, lblQtd);

            JButton btnMais = new JButton("+");
            JButton btnMenos = new JButton("–");

            btnMais.addActionListener(e -> alterarQuantidade(nome, 1));
            btnMenos.addActionListener(e -> alterarQuantidade(nome, -1));

            JPanel painelControlo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            painelControlo.add(btnMenos);
            painelControlo.add(lblQtd);
            painelControlo.add(btnMais);

            linha.add(painelControlo, BorderLayout.EAST);
            painelItens.add(linha);
        }

        campoDesconto.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { atualizarTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { atualizarTotal(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { atualizarTotal(); }
        });

        labelTotal = new JLabel();
        atualizarTotal();

        JPanel painelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        painelCampos.add(new JLabel("Nome:"));
        painelCampos.add(campoNome);
        painelCampos.add(new JLabel("Filme:"));
        painelCampos.add(comboFilmes);
        painelCampos.add(new JLabel("Descrição:"));
        painelCampos.add(campoDescricao);
        painelCampos.add(new JLabel("Desconto (€):"));
        painelCampos.add(campoDesconto);
        painelCampos.add(new JLabel("Ativo:"));
        painelCampos.add(checkAtivo);
        painelCampos.add(new JLabel("Preço Total:"));
        painelCampos.add(labelTotal);

        JButton btnGuardar = new JButton("💾 Guardar");
        btnGuardar.addActionListener(e -> {
            try {
                menu.setNome(campoNome.getText().trim());
                menu.setDescricao(campoDescricao.getText().trim());
                menu.setAtivo(checkAtivo.isSelected());

                Filme filmeSelecionado = (Filme) comboFilmes.getSelectedItem();
                menu.setFilme(filmeSelecionado != null ? filmeSelecionado.getNome() : "");

                List<String> novosItens = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : quantidades.entrySet()) {
                    for (int i = 0; i < entry.getValue(); i++) {
                        novosItens.add(entry.getKey());
                    }
                }

                menu.setItens(novosItens); // substituição segura

                menu.setPreco(calcularPrecoTotal());
                PersistenciaService.atualizarMenu(menu);

                JOptionPane.showMessageDialog(this, "Menu atualizado com sucesso.");
                dispose();

                if (onClose != null) onClose.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao guardar: " + ex.getMessage());
            }
        });

        add(painelCampos, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(painelItens);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void alterarQuantidade(String nome, int delta) {
        int atual = quantidades.getOrDefault(nome, 0);
        int novo = Math.max(0, atual + delta);
        quantidades.put(nome, novo);
        etiquetasQuantidade.get(nome).setText(String.valueOf(novo));
        atualizarTotal();
    }

    private void atualizarTotal() {
        labelTotal.setText(df.format(calcularPrecoTotal()) + " €");
    }

    private double calcularPrecoTotal() {
        double subtotal = PRECO_FILME;
        for (Map.Entry<String, Integer> entry : quantidades.entrySet()) {
            double preco = precosItens.getOrDefault(entry.getKey(), 0.0);
            subtotal += preco * entry.getValue();
        }
        double desconto = 0;
        try {
            desconto = Double.parseDouble(campoDesconto.getText().trim());
        } catch (Exception ignored) {}
        return Math.max(0.0, subtotal - desconto);
    }
}