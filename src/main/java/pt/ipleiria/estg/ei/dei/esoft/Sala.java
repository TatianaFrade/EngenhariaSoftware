package pt.ipleiria.estg.ei.dei.esoft;

import java.util.ArrayList;
import java.util.List;


public class Sala {
    private String nome;
    private String acessibilidade; // "sim" ou "nao"
    private int totalLugares;
    private List<Lugar> lugares;
    private int lugaresOcupados;


    public Sala(String nome, String acessibilidade, int filas, int colunas) {
        this.nome = nome;
        this.acessibilidade = acessibilidade;
        this.totalLugares = filas * colunas;
        this.lugares = new ArrayList<>(totalLugares);
        this.lugaresOcupados = 0;

        // Inicializa os lugares da sala
        inicializarLugares(filas, colunas);
    }


    private void inicializarLugares(int filas, int colunas) {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < colunas; j++) {
                lugares.add(Lugar.criarLugarPorPosicao(i, j));
            }
        }

        // Define lugares ocupados iniciais para exemplo
        marcarLugaresOcupadosIniciais();
    }

    /**
     * Define alguns lugares como ocupados para exemplificar
     * (Mesma lógica que estava em JanelaSelecaoLugar)
     */
    private void marcarLugaresOcupadosIniciais() {
        // Procura lugares específicos e marca como ocupados
        // Note: Estes correspondem aos assentos A3, B9, G4, H6
        for (Lugar lugar : lugares) {
            int fila = lugar.getFila();
            int coluna = lugar.getColuna();

            if ((fila == 0 && coluna == 2) || // A3
                    (fila == 1 && coluna == 8) || // B9
                    (fila == 6 && coluna == 3) || // G4
                    (fila == 7 && coluna == 5)) { // H6 (corrigido de H7)
                lugar.setOcupado(true);
                lugaresOcupados++;
            }
        }
    }


    public Lugar getLugar(int fila, int coluna) {
        for (Lugar lugar : lugares) {
            if (lugar.getFila() == fila && lugar.getColuna() == coluna) {
                return lugar;
            }
        }
        return null;
    }


    public boolean isLugarDisponivel(int fila, int coluna) {
        Lugar lugar = getLugar(fila, coluna);
        return lugar != null && !lugar.isOcupado();
    }

    /**
     * Ocupa um lugar específico
     * @param fila Fila do lugar
     * @param coluna Coluna do lugar
     * @return true se conseguiu ocupar, false se lugar já estava ocupado ou não existe
     */
    public boolean ocuparLugar(int fila, int coluna) {
        Lugar lugar = getLugar(fila, coluna);
        if (lugar != null && !lugar.isOcupado()) {
            lugar.setOcupado(true);
            lugaresOcupados++;
            return true;
        }
        return false;
    }

    /**
     * Libera um lugar específico
     * @param fila Fila do lugar
     * @param coluna Coluna do lugar
     * @return true se conseguiu liberar, false se lugar já estava livre ou não existe
     */
    public boolean libertarLugar(int fila, int coluna) {
        Lugar lugar = getLugar(fila, coluna);
        if (lugar != null && lugar.isOcupado()) {
            lugar.setOcupado(false);
            lugaresOcupados--;
            return true;
        }
        return false;
    }

    /**
     * @return Percentual de ocupação da sala
     */
    public double getPercentualOcupacao() {
        return (double) lugaresOcupados / totalLugares * 100;
    }

    // Getters e setters

    public String getNome() {
        return nome;
    }

    public String getAcessibilidade() {
        return acessibilidade;
    }

    public int getTotalLugares() {
        return totalLugares;
    }

    public List<Lugar> getLugares() {
        return new ArrayList<>(lugares); // Retorna uma cópia para evitar modificações externas
    }

    public int getLugaresOcupados() {
        return lugaresOcupados;
    }

    public int getLugaresDisponiveis() {
        return totalLugares - lugaresOcupados;
    }
}
