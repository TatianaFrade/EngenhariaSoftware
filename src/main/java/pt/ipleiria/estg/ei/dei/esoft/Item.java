package pt.ipleiria.estg.ei.dei.esoft;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um item disponível no bar
 */
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String descricao;
    private double preco;
    private String categoria; // Ex: "Bebida", "Comida", "Combo", etc.
    private boolean disponivel;

    /**
     * Construtor da classe Item
     *
     * @param nome Nome do item
     * @param descricao Descrição curta do item
     * @param preco Preço do item em euros
     * @param categoria Categoria do item ("Bebida", "Comida", etc.)
     * @param disponivel Se o item está disponível para compra
     */
    public Item(String nome, String descricao, double preco, String categoria, boolean disponivel) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.disponivel = disponivel;
    }

    /**
     * @return Nome do item
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return Descrição do item
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @return Preço do item em euros
     */
    public double getPreco() {
        return preco;
    }

    /**
     * @return Categoria do item ("Bebida", "Comida", etc.)
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * @return true se o item está disponível, false caso contrário
     */
    public boolean isDisponivel() {
        return disponivel;
    }

    /**
     * Define se o item está disponível
     * @param disponivel novo status de disponibilidade
     */
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }    /**
     * Retorna uma lista vazia pois os itens agora são carregados do arquivo itens.json
     * @return Lista vazia (os itens são gerenciados pelo PersistenciaService)
     */
    public static List<Item> getItensPadrao() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return nome + " - " + String.format("%.2f €", preco);
    }

    // Setters para permitir atualização dos itens
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
