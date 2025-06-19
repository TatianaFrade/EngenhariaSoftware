package pt.ipleiria.estg.ei.dei.esoft;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sessao {
    private Filme filme;
    private LocalDateTime dataHora;
    private Sala sala;
    private double preco;

    /**
     * Construtor de compatibilidade que cria uma sala padrão
     * @param filme Filme exibido na sessão
     * @param dataHora Data e hora da sessão
     * @param nomeSala Nome da sala
     * @param preco Preço da sessão
     */
    public Sessao(Filme filme, LocalDateTime dataHora, String nomeSala, double preco) {
        this(filme, dataHora, new Sala(nomeSala, "sim", 8, 10), preco);
    }

    /**
     * Construtor completo com objeto Sala
     * @param filme Filme exibido na sessão
     * @param dataHora Data e hora da sessão
     * @param sala Sala onde a sessão ocorre
     * @param preco Preço da sessão
     */
    public Sessao(Filme filme, LocalDateTime dataHora, Sala sala, double preco) {
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

    /**
     * @return Objeto Sala completo
     */
    public Sala getSala() {
        return sala;
    }

    /**
     * @return Nome da sala (para compatibilidade com código existente)
     */
    public String getNomeSala() {
        return sala.getNome();
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return filme.getNome() + " - " + getDataHoraFormatada() + " - Sala " + sala.getNome();
    }
}
