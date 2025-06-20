package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class ItemCartao extends Cartao {
    public ItemCartao(Item item) {
        super();
        this.putClientProperty("item", item);
        construirConteudo(item);
    }

    @Override
    protected void construirConteudo(Object objeto) {
        if (!(objeto instanceof Item item)) return;

        // Parte esquerda: imagem
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(100, 140));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Exemplo: imgLabel.setIcon(new ImageIcon(item.getImagemPath()));
        imgLabel.setText("[Imagem]");
        this.add(imgLabel, BorderLayout.WEST);

        // Parte central: info textual
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel nomeLabel = new JLabel("Nome: " + item.getNome());
        nomeLabel.setFont(new Font(nomeLabel.getFont().getName(), Font.BOLD, 14));
        infoPanel.add(nomeLabel);

        JLabel tipoLabel = new JLabel("Categoria: " + item.getCategoria());
        infoPanel.add(tipoLabel);

        JLabel precoLabel = new JLabel("Preço: €" + item.getPreco());
        infoPanel.add(precoLabel);

        this.add(infoPanel, BorderLayout.CENTER);
    }

    public void mouseEntrou() {
        setBorder(BorderFactory.createLineBorder(BORDA_HOVER, LARGURA_BORDA_HOVER));
    }

    public void mouseSaiu() {
        setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
    }

    public void atualizaBorda(Boolean selecionado) {
        setBorder(BorderFactory.createLineBorder(
                selecionado ? BORDA_SELECIONADO : BORDA_NORMAL,
                selecionado ? LARGURA_BORDA_SELECIONADO : LARGURA_BORDA_NORMAL
        ));
    }
}
