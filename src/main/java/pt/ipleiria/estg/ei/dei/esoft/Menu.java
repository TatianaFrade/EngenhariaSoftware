package pt.ipleiria.estg.ei.dei.esoft;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String descricao;
    private List<String> itens;
    private double preco;
    private String filme;
    private boolean isAtivo;

    public Menu(int id, String nome, String descricao, List<String> itens, double preco, String filme, boolean isAtivo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        if (itens == null || itens.isEmpty()) {
            this.itens = new ArrayList<>();
        }
        else {
            this.itens = new ArrayList<>(itens);
        }
        this.preco = preco;
        this.filme = filme;
        this.isAtivo = isAtivo;
    }

    public Menu() {
        this.id = -1;
        this.nome = "";
        this.descricao = "";
        this.itens = new ArrayList<>();
        this.preco = 0.0;
        this.filme = "";
        this.isAtivo = true;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<String> getItens() {
        return new ArrayList<>(itens);
    }

    public double getPreco() {
        return preco;
    }

    public String getFilme() {
        return filme;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setItens(List<String> itens) {
        if (itens == null || itens.isEmpty()) {
            this.itens = new ArrayList<>();
        } else {
            this.itens = new ArrayList<>(itens);
        }
    }

    public void addItem(String nomeItem) {
        if (this.itens == null) {
            this.itens = new ArrayList<>();
        }
        this.itens.add(nomeItem.trim());
    }

    public boolean removeItemPorNome(String nomeItem) {
        if (this.itens != null && nomeItem != null) {
            return this.itens.removeIf(nome -> nome.equalsIgnoreCase(nomeItem.trim()));
        }
        return false;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setFilme(String filme) {
        this.filme = filme;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", itens=" + itens +
                ", preco=" + preco +
                ", filme='" + filme + '\'' +
                ", isAtivo=" + isAtivo +
                '}';
    }
}