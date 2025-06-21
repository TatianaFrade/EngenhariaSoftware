package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class JanelaRemoverItem extends JDialog {
    private Item item;

    public JanelaRemoverItem(JFrame parent, Item item, List<Item> listaItens) {
        super(parent, "Remover Item do Stock", true);
        this.item = item;

        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);

        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new BoxLayout(painelInfo, BoxLayout.Y_AXIS));
        painelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelInfo.add(new JLabel("Nome: " + item.getNome()));
        painelInfo.add(new JLabel("Categoria: " + item.getCategoria()));
        if (item.getCategoria().equals("Combo")) {
            painelInfo.add(new JLabel("Deseja remover este Combo?"));
        } else {
            painelInfo.add(new JLabel("Quantidade atual: " + item.getQuantidade()));
        }

        painelInfo.add(Box.createVerticalStrut(10));

        JPanel painelInput = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelInput.add(new JLabel("Quantidade a remover: "));
        JTextField campoQuantidade = new JTextField(5);
        painelInput.add(campoQuantidade);

        if (!item.getCategoria().equals("Combo")) painelInfo.add(painelInput);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton cancelar = new JButton("Cancelar");
        JButton removerTudo = new JButton("Remover Item por Completo");

        cancelar.addActionListener(e -> dispose());
        painelBotoes.add(cancelar);

        if (!item.getCategoria().equals("Combo")) {
            JButton confirmar = new JButton("Confirmar Remoção");

            confirmar.addActionListener(e -> {
                try {
                    int qtd = Integer.parseInt(campoQuantidade.getText());
                    if (qtd <= 0) {
                        JOptionPane.showMessageDialog(this, "Quantidade deve ser positiva.");
                        return;
                    }
                    if (qtd > item.getQuantidade()) {
                        JOptionPane.showMessageDialog(this, "Quantidade excede o stock atual.");
                        return;
                    }

                    item.mudarQuantidade(-qtd);

                    PersistenciaService.salvarItens(listaItens);
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Insira um número válido.");
                }
            });
            painelBotoes.add(confirmar);
        }

        removerTudo.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem a certeza que quer remover permanentemente este item?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                listaItens.remove(item);
                PersistenciaService.salvarItens(listaItens);
                dispose();
            }
        });

        painelBotoes.add(removerTudo);

        add(painelInfo, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }
}
