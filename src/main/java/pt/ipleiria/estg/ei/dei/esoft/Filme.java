package pt.ipleiria.estg.ei.dei.esoft;

public class Filme {
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
}
