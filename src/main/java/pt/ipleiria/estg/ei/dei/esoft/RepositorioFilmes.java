package pt.ipleiria.estg.ei.dei.esoft;

import java.util.ArrayList;
import java.util.List;

public class RepositorioFilmes {
    private List<Filme> filmes;

    public RepositorioFilmes() {
        filmes = new ArrayList<>();
    }

    public List<Filme> getFilmes() {
        return filmes;
    }

    public void adicionar(Filme filme) {
        filmes.add(filme);
    }

    public void remover(Filme filme) {
        filmes.remove(filme);
    }

    public void atualizar(Filme filmeAntigo, Filme filmeNovo) {
        int index = filmes.indexOf(filmeAntigo);
        if (index >= 0) {
            filmes.set(index, filmeNovo);
        }
    }
}