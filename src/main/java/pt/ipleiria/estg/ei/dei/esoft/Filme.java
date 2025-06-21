package pt.ipleiria.estg.ei.dei.esoft;

import java.io.Serializable;

public class Filme implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private boolean legendado;
    private String dataLancamento;
    private double rate;
    private String imagemPath;

    public Filme(String nome, boolean legendado, String dataLancamento, double rate, String imagemPath) {
        this.nome = nome;
        this.legendado = legendado;
        this.dataLancamento = dataLancamento;
        this.rate = rate;
        this.imagemPath = imagemPath;
    }

    public String getNome() { return nome; }
    public boolean isLegendado() { return legendado; }
    public String getDataLancamento() { return dataLancamento; }
    public double getRate() { return rate; }
    public String getImagemPath() { return imagemPath; }

    @Override
    public String toString() {
        return nome;
    public void atualizar(String nome, boolean legendado, String dataLancamento, double rate, String imagemPath) {
        this.nome = nome;
        this.legendado = legendado;
        this.dataLancamento = dataLancamento;
        this.rate = rate;
        this.imagemPath = imagemPath;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Filme filme = (Filme) obj;
        return nome.equals(filme.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
