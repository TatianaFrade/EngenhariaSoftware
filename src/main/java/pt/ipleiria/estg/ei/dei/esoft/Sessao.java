package pt.ipleiria.estg.ei.dei.esoft;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sessao {
    private Filme filme;
    private LocalDateTime dataHora;
    private String sala;
    private double preco;

    public Sessao(Filme filme, LocalDateTime dataHora, String sala, double preco) {
        this.filme = filme;
        this.dataHora = dataHora;
        this.sala = sala;
        this.preco = preco;
    }

    public Filme getFilme() {
        return filme;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getDataHoraFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataHora.format(formatter);
    }

    public String getSala() {
        return sala;
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return filme.getNome() + " - " + getDataHoraFormatada() + " - Sala " + sala;
    }
}
