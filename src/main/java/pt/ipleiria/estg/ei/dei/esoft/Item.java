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
    private int quantidade = 0;
    private List<Dependencia> dependencias = new ArrayList<Dependencia>();
    private String fornecedor;
    private double precoFornecedor;

    /**
     * Construtor da classe Item
     *
     * @param nome Nome do item
     * @param descricao Descrição curta do item
     * @param preco Preço do item em euros
     * @param categoria Categoria do item ("Bebida", "Comida", etc.)
     * @param disponivel Se o item está disponível para compra
     * @param quantidade Quanto está em stock
     * @param dependencias Quais as dependencias do combo
     * @param fornecedor Qual o nome do fornecedor
     * @param precoFornecedor O preço cobrado pelo fornecedor
     */
    public Item(String nome, String descricao, double preco, String categoria, boolean disponivel, int quantidade, List<Dependencia> dependencias, String fornecedor, double precoFornecedor) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.disponivel = disponivel;
        if (categoria.equals("Combo")) {
            this.dependencias.addAll(dependencias);
        } else {
            this.quantidade = quantidade;
            this.fornecedor = fornecedor;
            this.precoFornecedor = precoFornecedor;
        }
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
     * @return Quantidade disponivel no stock
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * @return Quantidade disponivel no stock
     */
    public List<Dependencia> getDependencias() {
        return dependencias;
    }

    /**
     * @return Nome do fornecedor
     */
    public String getFornecedor() {
        return fornecedor;
    }

    /**
     * @return Preco feito pelo fornecedor
     */
    public double getPrecoFornecedor() {
        return precoFornecedor;
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

    public void setDependencias(List<Dependencia> dependencias) {
        this.dependencias = dependencias;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void setPrecoFornecedor(double precoFornecedor) {
        this.precoFornecedor = precoFornecedor;
    }

    public void mudarQuantidade(int quantidade) {
        if (this.categoria.equals("Combo")) {
            for (Dependencia dependencia : dependencias) {
                dependencia.getItem().mudarQuantidade(quantidade * dependencia.getQuantidade());
            }
            atualizarComboDisponibilidade();
        } else {
            this.quantidade += quantidade;
            if (this.quantidade <= 0) this.quantidade = 0;
        }
    }

    public void atualizarComboDisponibilidade() {
        for (Dependencia dependencia : dependencias) {
            if (dependencia.getItem().getQuantidade() < dependencia.getQuantidade()) {
                this.disponivel = false;
            } else {
                this.disponivel = true;
            }
        }
    }

    public void atualizarDependencias(List<Item> itens) {
        if (this.categoria.equals("Combo")){
            // Encontrar os nomes das dependencias
            List<String> nomes = new ArrayList<>();
            List<Integer> quantidades = new ArrayList<>();
            for (Dependencia dependencia : this.dependencias) {
                nomes.add(dependencia.getItem().getNome());
                quantidades.add(dependencia.getQuantidade());
            }

            List<Dependencia> dependencias = new ArrayList<Dependencia>();

            for (Item item : itens) {
                if (nomes.contains(item.getNome())) dependencias.add(new Dependencia(item, quantidades.get(nomes.indexOf(item.getNome()))));
            }

            this.setDependencias(dependencias);
        }
    }
}
