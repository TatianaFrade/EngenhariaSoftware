package pt.ipleiria.estg.ei.dei.esoft;

public class Dependencia {
    private Item item;
    private int quantidade;

    public Dependencia(Item item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
