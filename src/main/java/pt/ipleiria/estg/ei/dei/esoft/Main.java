package pt.ipleiria.estg.ei.dei.esoft;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Garantir que o diretório de dados existe
        File diretorioDados = new File("dados");
        if (!diretorioDados.exists()) {
            diretorioDados.mkdirs();
            System.out.println("Diretório de dados criado: " + diretorioDados.getAbsolutePath());
        }

        System.out.println("Iniciando a aplicação do Cinema e Bar");

        // Iniciar a aplicação
        new JanelaPrincipal("Cinema e Bar").setVisible(true);
    }
}