package pt.ipleiria.estg.ei.dei.esoft;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Classe que representa uma compra no sistema
 */
public class Compra implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Date dataHora;
    private String idSessao;
    private String idLugar;
    private double precoBase;
    private List<Item> itensBar;
    private double precoTotal;
    private String metodoPagamento;
    private boolean confirmada;

    public Compra(Sessao sessao, Lugar lugar, List<Item> itensBar, double precoTotal, String metodoPagamento) {
        this.id = UUID.randomUUID().toString();
        this.dataHora = new Date();
        this.idSessao = sessao.getId();
        this.idLugar = lugar.getIdentificacao();
        this.precoBase = lugar.calcularPreco(sessao.getPreco());
        this.itensBar = itensBar;
        this.precoTotal = precoTotal;
        this.metodoPagamento = metodoPagamento;
        this.confirmada = metodoPagamento.equals("Cartão de Crédito"); // Pré-confirmada se for cartão
    }

    // Getters
    public String getId() {
        return id;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public String getIdSessao() {
        return idSessao;
    }

    public String getIdLugar() {
        return idLugar;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public List<Item> getItensBar() {
        return itensBar;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    // Método para confirmar pagamento (usado para multibanco)
    public void confirmarPagamento() {
        this.confirmada = true;
    }

    // Calcular valor dos itens do bar
    public double getValorItensBar() {
        if (itensBar == null || itensBar.isEmpty()) {
            return 0.0;
        }

        return itensBar.stream().mapToDouble(Item::getPreco).sum();
    }
}
