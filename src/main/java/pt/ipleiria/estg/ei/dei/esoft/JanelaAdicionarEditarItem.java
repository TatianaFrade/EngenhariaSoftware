package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class JanelaAdicionarEditarItem extends JDialog {
    private JTextField campoNome;
    private JComboBox<String> comboCategoria;
    private JTextField campoFornecedor;
    private JTextField campoPrecoFornecedor;
    private JTextField campoPrecoVenda;
    private JTextField campoDescricao;
    private JCheckBox checkboxDisponivel;
    private JPanel painelDependencias;

    JScrollPane scrollDependencias;

    private final Item item;
    private final List<Dependencia> dependenciasTemp;

    public JanelaAdicionarEditarItem(JFrame parent, Item item, List<Item> listaCompletaItens) {
        super(parent, (item == null ? "Adicionar Item" : "Editar Item"), true);
        this.item = (item != null) ? item : new Item("", "", 0, "Comida", true, 0, new ArrayList<>(), "", 0);
        this.dependenciasTemp = new ArrayList<>();
        if (this.item.getDependencias() != null) {
            dependenciasTemp.addAll(this.item.getDependencias());
        }

        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        campoNome = new JTextField(this.item.getNome());
        checkboxDisponivel = new JCheckBox("Disponível");
        campoFornecedor = new JTextField(this.item.getFornecedor());
        campoPrecoFornecedor = new JTextField(String.valueOf(this.item.getPrecoFornecedor()));
        campoPrecoVenda = new JTextField(String.valueOf(this.item.getPreco()));
        campoDescricao = new JTextField();

        JPanel painelCampos = new JPanel();
        painelCampos.setLayout(new GridLayout(0, 2, 5, 5));
        add(painelCampos, BorderLayout.NORTH);

        JPanel painelBotoesInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(painelBotoesInferior, BorderLayout.SOUTH);

        if (item != null) {
            comboCategoria = new JComboBox<>();
            if (item.getCategoria().equals("Combo")) {
                comboCategoria.addItem("Combo");
            } else {
                comboCategoria.addItem("Comida");
                comboCategoria.addItem("Bebida");
            }
            comboCategoria.setSelectedItem(item.getCategoria());
        } else {
            comboCategoria = new JComboBox<>();
            comboCategoria.addItem("Comida");
            comboCategoria.addItem("Bebida");
            comboCategoria.addItem("Combo");
            comboCategoria.addActionListener(e -> atualizarVisibilidadeDependencias(painelCampos, painelBotoesInferior, listaCompletaItens));
        }

        configurarCampos(painelCampos);
        configurarBotoes(painelBotoesInferior, listaCompletaItens);

        pack();
        // Adicionar a descrição depois do pack para não aumentar o tamanho da pagina
        campoDescricao.setText(this.item.getDescricao());
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void configurarCampos(JPanel painelCampos) {
        painelCampos.removeAll();

        painelCampos.add(new JLabel("Nome:"));
        painelCampos.add(campoNome);

        painelCampos.add(new JLabel("Categoria:"));
        painelCampos.add(comboCategoria);

        painelCampos.add(new JLabel("Preço Venda (€):"));
        painelCampos.add(campoPrecoVenda);

        if (!comboCategoria.getSelectedItem().equals("Combo")) {
            checkboxDisponivel.setSelected(this.item.isDisponivel());
            painelCampos.add(new JLabel("Disponível:"));
            painelCampos.add(checkboxDisponivel);

            painelCampos.add(new JLabel("Fornecedor:"));
            painelCampos.add(campoFornecedor);

            painelCampos.add(new JLabel("Preço Fornecedor (€):"));
            painelCampos.add(campoPrecoFornecedor);
        }

        painelCampos.add(new JLabel("Descrição:"));
        painelCampos.add(campoDescricao);
        painelCampos.revalidate();
        painelCampos.repaint();
    }

    private void configurarBotoes(JPanel painelBotoesInferior, List<Item> listaCompletaItens) {
        painelBotoesInferior.removeAll();

        JButton cancelar = new JButton("Cancelar");
        JButton salvar = new JButton("Salvar");

        cancelar.addActionListener(e -> dispose());
        salvar.addActionListener(e -> {
            if (salvarAlteracoes(listaCompletaItens)) dispose();
        });

        if (comboCategoria.getSelectedItem().equals("Combo")) {
            if (scrollDependencias == null) {
                painelDependencias = new JPanel();
                painelDependencias.setLayout(new BoxLayout(painelDependencias, BoxLayout.Y_AXIS));
                atualizarPainelDependencias();

                scrollDependencias = new JScrollPane(painelDependencias);
                scrollDependencias.setBorder(BorderFactory.createTitledBorder("Dependências"));
                scrollDependencias.setPreferredSize(new Dimension(300, 150));
                add(scrollDependencias, BorderLayout.CENTER);
            } else {
                scrollDependencias.setVisible(true);
            }
            JButton botaoAdicionarDependencia = new JButton("Adicionar Dependência");
            botaoAdicionarDependencia.addActionListener(e -> adicionarDependencia(listaCompletaItens));
            painelBotoesInferior.add(botaoAdicionarDependencia);
        } else if (scrollDependencias != null) {
            scrollDependencias.setVisible(false);
        }

        painelBotoesInferior.add(cancelar);
        painelBotoesInferior.add(salvar);
        painelBotoesInferior.revalidate();
        painelBotoesInferior.repaint();
    }

    private void atualizarVisibilidadeDependencias(JPanel painelCampos, JPanel painelBotoesInferior, List<Item> listaCompletaItens) {
        configurarCampos(painelCampos);
        configurarBotoes(painelBotoesInferior, listaCompletaItens);
        // Para nao aumentar o tamanho com a descrição
        String texto = campoDescricao.getText();
        campoDescricao.setText("");
        pack();
        campoDescricao.setText(texto);
    }

    private void atualizarPainelDependencias() {
        painelDependencias.removeAll();
        for (Dependencia dep : dependenciasTemp) {
            JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT));
            linha.add(new JLabel(dep.getItem().getNome() + " (Qtd: " + dep.getQuantidade() + ")"));

            JButton remover = new JButton("Remover");
            remover.addActionListener(e -> {
                dependenciasTemp.remove(dep);
                atualizarPainelDependencias();
            });
            linha.add(remover);
            painelDependencias.add(linha);
        }
        painelDependencias.revalidate();
        painelDependencias.repaint();
    }

    private void adicionarDependencia(List<Item> listaCompletaItens) {
        // Filtrar para todos os itens que nao forem do tipo combo
        List<Item> itensValidos = new ArrayList<>();
        for (Item i : listaCompletaItens) {
            if (!i.getCategoria().equals("Combo")) {
                itensValidos.add(i);
            }
        }

        JComboBox<Item> comboItens = new JComboBox<>(itensValidos.toArray(new Item[0]));
        JTextField campoQtd = new JTextField();

        JPanel painel = new JPanel(new GridLayout(0, 2));
        painel.add(new JLabel("Item:"));
        painel.add(comboItens);
        painel.add(new JLabel("Quantidade:"));
        painel.add(campoQtd);

        int result = JOptionPane.showConfirmDialog(this, painel, "Nova Dependência", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Item itemSelecionado = (Item) comboItens.getSelectedItem();
                int quantidade = Integer.parseInt(campoQtd.getText());
                if (itemSelecionado != null) {
                    boolean existe = false;
                    for (Dependencia dep : dependenciasTemp) {
                        if (dep.getItem().getNome().equals(itemSelecionado.getNome())) {
                            existe = true;
                            JOptionPane.showMessageDialog(this, "O item " + itemSelecionado.getNome() + " já existe como dependência.\nRemova primeiro.");
                        }
                    }
                    if (!existe && quantidade > 0) {
                        dependenciasTemp.add(new Dependencia(itemSelecionado, quantidade));
                        atualizarPainelDependencias();
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida.");
            }
        }
    }

    private boolean salvarAlteracoes(List<Item> listaCompletaItens) {
        // Validações
        double preco;
        try {
            preco = Double.parseDouble(campoPrecoVenda.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço de venda inválido.");
            return false;
        }
        if (preco < 0) {
            JOptionPane.showMessageDialog(this, "Preço de venda tem de ser positivo.");
            return false;
        }
        double precoFornecedor;
        try {
            precoFornecedor = Double.parseDouble(campoPrecoFornecedor.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço do fornecedor inválido.");
            return false;
        }
        if (precoFornecedor < 0) {
            JOptionPane.showMessageDialog(this, "Preço do fornecedor tem de ser positivo.");
            return false;
        }
        System.out.println(this.item.getNome());
        if (!campoNome.getText().equals("")) {
            for (Item item : listaCompletaItens) {
                if (item.equals(this.item)) continue;
                if (item.getNome().equals(campoNome.getText())) {
                    JOptionPane.showMessageDialog(this, "Já existe um item com esse nome.");
                    return false;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "O item tem de ter um nome.");
            return false;
        }
        if (comboCategoria.getSelectedItem().equals("Combo")) {
            if (dependenciasTemp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Um combo deve ter pelo menos uma dependência.");
                return false;
            }
            item.setDependencias(dependenciasTemp);
        } else {
            item.setDependencias(new ArrayList<>());
        }

        // Adicionar item à lista se for novo
        if (this.item.getNome().equals("")) listaCompletaItens.add(item);

        // Aplicar e salvar os dados
        item.setNome(campoNome.getText());
        item.setCategoria((String) comboCategoria.getSelectedItem());
        item.setDescricao(campoDescricao.getText());
        if (!item.getCategoria().equals("Combo")) {
            item.setFornecedor(campoFornecedor.getText());
            item.setPrecoFornecedor(precoFornecedor);
        }
        item.setPreco(preco);

        PersistenciaService.salvarItens(listaCompletaItens);
        return true;
    }
}
