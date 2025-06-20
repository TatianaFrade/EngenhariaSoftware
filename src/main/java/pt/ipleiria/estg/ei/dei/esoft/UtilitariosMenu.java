package pt.ipleiria.estg.ei.dei.esoft;

import java.util.ArrayList;
import java.util.List;

public class UtilitariosMenu {
    public static Filme obterFilmePorNome(String nomeFilme) {
        List<Filme> filmes = PersistenciaService.carregarFilmes();

        for (Filme f : filmes) {
            if (f.getNome().equalsIgnoreCase(nomeFilme.trim())) {
                return f;
            }
        }

        return null;
    }

    public static List<Item> obterItensPorNomes(List<String> nomes) {
        List<Item> todosItens = PersistenciaService.carregarItens();
        List<Item> resultado = new ArrayList<>();

        for (String nome : nomes) {
            for (Item item : todosItens) {
                if (item.getNome().equalsIgnoreCase(nome.trim())) {
                    resultado.add(item);
                    break;
                }
            }
        }

        return resultado;
    }
}
