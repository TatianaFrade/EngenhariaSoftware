package pt.ipleiria.estg.ei.dei.esoft;

import java.io.File;
import javax.swing.SwingUtilities;

/**
 * Classe principal que inicia a aplicação do Cinema e Bar.
 */
public class Main {
    public static void main(String[] args) {
        // Garantir que o diretório de dados existe
        File diretorioDados = new File("dados");
        if (!diretorioDados.exists()) {
            diretorioDados.mkdirs();
            System.out.println("Diretório de dados criado: " + diretorioDados.getAbsolutePath());
        }
        System.out.println("Iniciando a aplicação do Cinema e Bar");

        // Iniciar a aplicação usando SwingUtilities para garantir que
        // operações de UI sejam executadas na thread de despacho de eventos
        SwingUtilities.invokeLater(() -> {
            try {
                new JanelaPrincipal("Cinema e Bar").setVisible(true);
            } catch (Exception e) {
                System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}