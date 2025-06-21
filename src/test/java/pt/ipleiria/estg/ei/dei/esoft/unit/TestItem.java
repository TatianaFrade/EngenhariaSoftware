package pt.ipleiria.estg.ei.dei.esoft.unit;

import org.junit.jupiter.api.Test;
import pt.ipleiria.estg.ei.dei.esoft.Item;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestItem {
    
    @Test
    void testConstructorAndGetters() {
        String nome = "Pipoca Grande";
        String descricao = "Pipoca salgada tamanho grande";
        double preco = 5.0;
        String categoria = "Comida";
        boolean disponivel = true;
        
        Item item = new Item(nome, descricao, preco, categoria, disponivel, 0, new ArrayList<>(), "", 0);
        
        assertEquals(nome, item.getNome());
        assertEquals(descricao, item.getDescricao());
        assertEquals(preco, item.getPreco());
        assertEquals(categoria, item.getCategoria());
        assertTrue(item.isDisponivel());
    }
    
    @Test
    void testSetters() {
        Item item = new Item("Test", "Test Desc", 1.0, "Test Cat", true, 0, new ArrayList<>(), "", 0);
        
        item.setNome("New Name");
        item.setDescricao("New Description");
        item.setPreco(2.0);
        item.setDisponivel(false);
        
        assertEquals("New Name", item.getNome());
        assertEquals("New Description", item.getDescricao());
        assertEquals(2.0, item.getPreco());
        assertFalse(item.isDisponivel());
    }
    
    @Test
    void testToString() {
        Item item = new Item("Coca-Cola", "Refrigerante 500ml", 3.50, "Bebida", true, 0, new ArrayList<>(), "", 0);
        String expected = "Coca-Cola - 3,50 €";
        assertEquals(expected, item.toString());
    }
    
    @Test
    void testGetItensPadrao() {
        assertTrue(Item.getItensPadrao().isEmpty());
    }
    
    @Test
    void testSerializable() {
        Item item = new Item("Test", "Test", 1.0, "Test", true, 0, new ArrayList<>(), "", 0);
        assertTrue(item instanceof java.io.Serializable);
    }
}
