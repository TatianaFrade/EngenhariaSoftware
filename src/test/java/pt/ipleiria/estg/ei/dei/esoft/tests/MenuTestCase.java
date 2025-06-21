package pt.ipleiria.estg.ei.dei.esoft.tests;
import pt.ipleiria.estg.ei.dei.esoft.Menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;

public class MenuTestCase {

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu(1, "Menu Pipoca", "Inclui pipoca e bebida",
                Arrays.asList("Pipoca", "Coca-Cola"), 7.50, "Matrix", true);
    }

    @Test
    void testConstrutorComParametros() {
        assertEquals("Menu Pipoca", menu.getNome());
        assertEquals("Inclui pipoca e bebida", menu.getDescricao());
        assertEquals(7.50, menu.getPreco());
        assertEquals("Matrix", menu.getFilme());
        assertTrue(menu.isAtivo());
        assertEquals(2, menu.getItens().size());
    }

    @Test
    void testConstrutorVazio() {
        Menu vazio = new Menu();
        assertEquals(-1, vazio.getId());
        assertEquals(0.0, vazio.getPreco());
        assertTrue(vazio.isAtivo());
        assertNotNull(vazio.getItens());
        assertTrue(vazio.getItens().isEmpty());
    }

    @Test
    void testAddItem() {
        menu.addItem("Nachos");
        assertTrue(menu.getItens().contains("Nachos"));
    }

    @Test
    void testRemoveItemPorNome() {
        boolean removido = menu.removeItemPorNome("Pipoca");
        assertTrue(removido);
        assertFalse(menu.getItens().contains("Pipoca"));
    }

    @Test
    void testRemoveItemPorNomeInexistente() {
        boolean removido = menu.removeItemPorNome("Chocolate");
        assertFalse(removido);
    }

    @Test
    void testSetItensComListaVazia() {
        menu.setItens(Collections.emptyList());
        assertTrue(menu.getItens().isEmpty());
    }

    @Test
    void testToStringContemNomeDoFilme() {
        String texto = menu.toString();
        assertTrue(texto.contains("Matrix"));
        assertTrue(texto.contains("Menu Pipoca"));
    }
}