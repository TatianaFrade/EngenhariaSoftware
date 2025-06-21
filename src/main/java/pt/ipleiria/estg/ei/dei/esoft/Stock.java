package pt.ipleiria.estg.ei.dei.esoft;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Stock {

    private String item;
    private int quantidade;
    private Date dataValidade;
    private String fornecedor;

    // Construtor
    public Stock(String item, int quantidade, Date dataValidade, String fornecedor) {
        this.item = item;
        this.quantidade = quantidade;
        this.dataValidade = dataValidade;
        this.fornecedor = fornecedor;
    }

    // Getters e Setters
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = Math.max(0, quantidade);
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void aumentar(int valor) {
        if (valor > 0) {
            this.quantidade += valor;
        }
    }

    public void diminuir(int valor) {
        if (valor > 0) {
            this.quantidade = Math.max(0, this.quantidade - valor);
        }
    }

    public boolean estaVencido() {
        return dataValidade.before(new Date());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;
        Stock s = (Stock) o;
        return Objects.equals(item, s.item)
                && Objects.equals(fornecedor, s.fornecedor)
                && Objects.equals(dataValidade, s.dataValidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, fornecedor, dataValidade);
    }
}
