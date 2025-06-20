package pt.ipleiria.estg.ei.dei.esoft;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public abstract class Cartao extends JPanel {

    // Constantes visuais (podes mover para um ficheiro de config se quiseres)
    protected static final Color BORDA_NORMAL = Color.GRAY;
    protected static final Color BORDA_HOVER = new Color(100, 100, 100);
    protected static final Color BORDA_SELECIONADO = new Color(0, 120, 215);
    protected static final int LARGURA_BORDA_NORMAL = 1;
    protected static final int LARGURA_BORDA_HOVER = 2;
    protected static final int LARGURA_BORDA_SELECIONADO = 3;

    protected Cartao() {
        this.setLayout(new BorderLayout(5, 5));
        this.setPreferredSize(new Dimension(200, 180));
        this.setBorder(BorderFactory.createLineBorder(BORDA_NORMAL, LARGURA_BORDA_NORMAL));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Muda o cursor para indicar que é clicável
    }

    protected abstract void construirConteudo(Object objeto);
}