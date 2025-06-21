package pt.ipleiria.estg.ei.dei.esoft;

import java.io.Serializable;

public class Encomenda implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigo;
    private int quantidade;
    private String itemNome;
    private String fornecedor;
    private boolean concluido = false;
    private boolean cancelado = false;
    private String messagem;

    public Encomenda(String codigo, int quantidade, String itemNome, String fornecedor) {
        this.codigo = codigo;
        this.quantidade = quantidade;
        this.itemNome = itemNome;
        this.fornecedor = fornecedor;
        this.messagem = "";
    }

    public String getCodigo() { return codigo; }
    public int getQuantidade() { return quantidade; }
    public String getItemNome() { return itemNome; }
    public String getFornecedor() { return fornecedor; }
    public boolean isConcluido() { return concluido; }
    public boolean isCancelado() { return cancelado; }
    public String getMessagem() { return messagem; }

    public void setConcluido(boolean concluido) { this.concluido = concluido; }
    public void setCancelado(boolean cancelado) { this.cancelado = cancelado; }
    public void setMessagem(String messagem) { this.messagem = messagem; }
}
