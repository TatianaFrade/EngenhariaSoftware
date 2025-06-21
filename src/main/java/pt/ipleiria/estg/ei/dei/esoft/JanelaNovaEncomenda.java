package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class JanelaNovaEncomenda extends JDialog {
    private final Item item;
    private JTextField campoFornecedor;
    private JTextField campoQuantidade;
    private JButton botaoConfirmar;
    private Encomenda encomendaCriada = null;

    public JanelaNovaEncomenda(JFrame parent, Item item) {
        super(parent, "Nova Encomenda", true);
        this.item = item;

        setLayout(new GridLayout(5, 2, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new JLabel("Item:"));
        add(new JLabel(item.getNome()));

        add(new JLabel("Fornecedor:"));
        campoFornecedor = new JTextField(item.getFornecedor());
        add(campoFornecedor);

        add(new JLabel("Quantidade:"));
        campoQuantidade = new JTextField();
        add(campoQuantidade);

        botaoConfirmar = new JButton("Confirmar");
        botaoConfirmar.addActionListener(e -> criarEncomenda());
        add(new JLabel()); // espaço vazio
        add(botaoConfirmar);

        pack();
        setLocationRelativeTo(parent);
    }

    private void criarEncomenda() {
        String fornecedor = campoFornecedor.getText().trim();
        if (fornecedor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O fornecedor não pode estar vazio.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(campoQuantidade.getText().trim());
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade deve ser um número positivo.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
            return;
        }

        // Gera código único, por exemplo UUID
        String codigo = UUID.randomUUID().toString();

        encomendaCriada = new Encomenda(codigo, quantidade, item.getNome(), fornecedor);

        JOptionPane.showMessageDialog(this, "Encomenda criada com sucesso!\nCódigo: " + codigo);
        dispose();
    }

    public Encomenda getEncomendaCriada() {
        return encomendaCriada;
    }
}