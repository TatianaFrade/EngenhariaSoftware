package pt.ipleiria.estg.ei.dei.esoft;

import java.awt.Color;

/**
 * Classe que representa um lugar na sala de cinema
 */
public class Lugar {
    private int fila;
    private int coluna;
    private boolean vip;
    private boolean ocupado;

    // Constantes para cores dos lugares (movidas da JanelaSelecaoLugar)
    public static final Color COR_LUGAR_DISPONIVEL = Color.LIGHT_GRAY;
    public static final Color COR_LUGAR_VIP = Color.BLACK;
    public static final Color COR_LUGAR_OCUPADO = Color.RED;
    public static final Color COR_LUGAR_SELECIONADO = new Color(0, 120, 215);
    public static final Color COR_TEXTO_VIP = Color.WHITE;


    public Lugar(int fila, int coluna, boolean vip, boolean ocupado) {
        this.fila = fila;
        this.coluna = coluna;
        this.vip = vip;
        this.ocupado = ocupado;
    }


    public static Lugar criarLugarPorPosicao(int fila, int coluna) {
        boolean vip = false;
        boolean ocupado = false;

        // VIP: Filas C, D, E e F (2, 3, 4, 5), colunas centrais (2-7)
        // Note: fila 0 = A, fila 1 = B, fila 2 = C, etc.
        if ((fila >= 2 && fila <= 5) && (coluna >= 2 && coluna <= 7)) {
            vip = true;
        }
        // Definir alguns lugares ocupados (distribuídos pela sala)
        // Note: Estes correspondem aos assentos A3, B9, G4, H7
        else if ((fila == 0 && coluna == 2) || // A3
                (fila == 1 && coluna == 8) || // B9
                (fila == 6 && coluna == 3) || // G4
                (fila == 7 && coluna == 5)) { // H6 (corrigido de H7)
            ocupado = true;
        }

        return new Lugar(fila, coluna, vip, ocupado);
    }


    public String getIdentificacao() {
        char letraFila = (char)('A' + fila);
        return letraFila + "" + (coluna + 1) + (vip ? " (VIP)" : "");
    }


    public double calcularPreco(double precoBase) {
        return precoBase + (vip ? 2.00 : 0.00);
    }


    public Color getCorFundo() {
        if (ocupado) {
            return COR_LUGAR_OCUPADO;
        } else if (vip) {
            return COR_LUGAR_VIP;
        } else {
            return COR_LUGAR_DISPONIVEL;
        }
    }


    public Color getCorTexto() {
        return vip ? COR_TEXTO_VIP : Color.BLACK;
    }

    // Getters e setters

    public int getFila() {
        return fila;
    }

    public int getColuna() {
        return coluna;
    }

    public boolean isVip() {
        return vip;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
}
