package pt.ipleiria.estg.ei.dei.esoft;

/**
 * Classe que representa um item disponível no bar
 */
public class Item {
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
    }

    /**
     * Cria uma lista de itens pré-definidos para o bar
     * @return Array com itens disponíveis no bar
     */
    public static Item[] getItensPadrao() {
        return new Item[] {
                new Item("Pipoca Pequena", "Pipoca salgada tamanho pequeno", 3.50, "Comida", true),
                new Item("Pipoca Média", "Pipoca salgada tamanho médio", 4.50, "Comida", true),
                new Item("Pipoca Grande", "Pipoca salgada tamanho grande", 5.50, "Comida", true),
                new Item("Pipoca Caramelizada", "Pipoca doce com caramelo", 6.00, "Comida", true),
                new Item("Refrigerante 300ml", "Refrigerante em lata", 3.00, "Bebida", true),
                new Item("Refrigerante 500ml", "Refrigerante em garrafa", 4.00, "Bebida", true),
                new Item("Água Mineral 500ml", "Água mineral sem gás", 2.50, "Bebida", true),
                new Item("Combo 1", "Pipoca média + Refrigerante 300ml", 6.50, "Combo", true),
                new Item("Combo 2", "Pipoca grande + 2 Refrigerantes 300ml", 10.00, "Combo", true),
                new Item("Chocolate", "Barra de chocolate 80g", 4.00, "Comida", true),
                new Item("Nachos com Queijo", "Porção de nachos com molho de queijo", 7.50, "Comida", true),
                new Item("Hot Dog", "Hot dog tradicional", 6.00, "Comida", true)
        };
    }

    @Override
    public String toString() {
        return nome + " - " + String.format("%.2f €", preco);
    }
}
