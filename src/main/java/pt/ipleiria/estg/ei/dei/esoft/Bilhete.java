package pt.ipleiria.estg.ei.dei.esoft;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe que representa um bilhete de cinema, contendo
 * informações sobre a sessão e o lugar
 */
public class Bilhete implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private transient Sessao sessao; // transient para não serializar o objeto completo
    private transient Lugar lugar;   // transient para não serializar o objeto completo
    private String idSessao;  // para persistência
    private String idLugar;   // para persistência
    private double preco;

    /**
     * Construtor principal que recebe os objetos Sessao e Lugar
     */
    public Bilhete(Sessao sessao, Lugar lugar) {
        this.id = UUID.randomUUID().toString();
        this.sessao = sessao;
        this.lugar = lugar;
        this.idSessao = sessao != null ? sessao.getId() : null;
        this.idLugar = lugar != null ? lugar.getIdentificacao() : null;
        this.preco = sessao != null && lugar != null ? lugar.calcularPreco(sessao.getPreco()) : 0.0;
    }

    /**
     * Restaura a referência aos objetos Sessao e Lugar após a desserialização
     */
    public void restaurarReferencias(Sessao sessao, Lugar lugar) {
        this.sessao = sessao;
        this.lugar = lugar;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getIdSessao() {
        return idSessao;
    }

    public String getIdLugar() {
        return idLugar;
    }

    public double getPreco() {
        return preco;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public Lugar getLugar() {
        return lugar;
    }

    @Override
    public String toString() {
        String filmeName = sessao != null && sessao.getFilme() != null ? sessao.getFilme().getNome() : "Filme não disponível";
        String lugarId = lugar != null ? lugar.getIdentificacao() : "Lugar não definido";
        return filmeName + " - " + lugarId + " (" + String.format("%.2f€", preco) + ")";
    }
}
