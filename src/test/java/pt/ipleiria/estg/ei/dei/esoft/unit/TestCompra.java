package pt.ipleiria.estg.ei.dei.esoft.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipleiria.estg.ei.dei.esoft.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestCompra {
    private Filme filme;
    private Sala sala;
    private Sessao sessao;
    private Lugar lugar;
    private Bilhete bilhete;
    private Item item1;
    private Item item2;
    private String idUsuario;

    @BeforeEach
    void setUp() {
        // Create objects for testing
        filme = new Filme("Test Movie", true, "2025-06-21", 4.5, "test.jpg");
        sala = new Sala("Sala 1", "sim", 8, 10);
        sessao = new Sessao(filme, LocalDateTime.now(), sala, 10.0);
        lugar = new Lugar(1, 1, true, false); // Row B, Seat 2, VIP
        bilhete = new Bilhete(sessao, lugar);
        
        // Create bar items
        item1 = new Item("Pipoca", "Pipoca grande", 5.0, "Comida", true, 0, new ArrayList<>(), "", 0);
        item2 = new Item("Refrigerante", "Coca-Cola 500ml", 3.0, "Bebida", true, 0, new ArrayList<>(), "", 0);
        
        idUsuario = "user123";
    }

    @Test
    void testConstructorWithBilhetesAndItens() {
        List<Bilhete> bilhetes = Arrays.asList(bilhete);
        List<Item> itens = Arrays.asList(item1, item2);
        
        Compra compra = new Compra(bilhetes, itens, "Cartão de Crédito", idUsuario);
        
        assertNotNull(compra.getId());
        assertNotNull(compra.getDataHora());
        assertEquals(bilhetes, compra.getBilhetes());
        assertEquals(itens, compra.getItensBar());
        assertEquals("Cartão de Crédito", compra.getMetodoPagamento());
        assertEquals(idUsuario, compra.getIdUsuario());
        assertTrue(compra.isConfirmada()); // Should be true for credit card
        assertEquals(20.0, compra.getPrecoTotal()); // 12.0 (bilhete VIP) + 5.0 (pipoca) + 3.0 (refrigerante)
    }

    @Test
    void testConstructorWithNullLists() {
        Compra compra = new Compra(null, null, "Multibanco", idUsuario);
        
        assertNotNull(compra.getId());
        assertTrue(compra.getBilhetes().isEmpty());
        assertTrue(compra.getItensBar().isEmpty());
        assertEquals(0.0, compra.getPrecoTotal());
        assertFalse(compra.isConfirmada()); // Should be false for Multibanco
    }

    @Test
    void testLegacyConstructor() {
        List<Item> itens = Arrays.asList(item1, item2);
        Compra compra = new Compra(sessao, lugar, itens, 20.0, "Cartão de Crédito", idUsuario);
        
        assertNotNull(compra.getId());
        assertEquals(1, compra.getBilhetes().size());
        assertEquals(2, compra.getItensBar().size());
        assertEquals(20.0, compra.getPrecoTotal());
        assertTrue(compra.isConfirmada());
        assertEquals(sessao.getId(), compra.getIdSessao());
        assertEquals(lugar.getIdentificacao(), compra.getIdLugar());
    }

    @Test
    void testConfirmarPagamento() {
        Compra compra = new Compra(Arrays.asList(bilhete), null, "Multibanco", idUsuario);
        assertFalse(compra.isConfirmada()); // Initially false for Multibanco
        
        compra.confirmarPagamento();
        assertTrue(compra.isConfirmada());
    }

    @Test
    void testGetValorItensBar() {
        List<Item> itens = Arrays.asList(item1, item2);
        Compra compra = new Compra(null, itens, "Cartão de Crédito", idUsuario);
        
        assertEquals(8.0, compra.getValorItensBar()); // 5.0 + 3.0
    }

    @Test
    void testGetResumoItensBarVazio() {
        Compra compra = new Compra(Arrays.asList(bilhete), new ArrayList<>(), "Cartão de Crédito", idUsuario);
        assertEquals("Nenhum item de bar", compra.getResumoItensBar());
    }

    @Test
    void testGetResumoItensBarComBilhete() {
        List<Item> itens = Arrays.asList(item1, item1, item2); // 2 pipocas, 1 refrigerante
        Compra compra = new Compra(Arrays.asList(bilhete), itens, "Cartão de Crédito", idUsuario);
        
        String resumo = compra.getResumoItensBar();
        assertTrue(resumo.contains("2x Pipoca"));
        assertTrue(resumo.contains("1x Refrigerante"));
    }

    @Test
    void testGetResumoItensBarSemBilhete() {
        List<Item> itens = Arrays.asList(item1, item1, item2); // 2 pipocas, 1 refrigerante
        Compra compra = new Compra(null, itens, "Cartão de Crédito", idUsuario);
        
        String resumo = compra.getResumoItensBar();
        assertTrue(resumo.contains("Detalhes:"));
        assertTrue(resumo.contains("Comida: 2x Pipoca"));
        assertTrue(resumo.contains("Bebida: 1x Refrigerante"));
    }

    @Test
    void testSetIdUsuario() {
        Compra compra = new Compra(null, null, "Cartão de Crédito", idUsuario);
        String novoId = "newUser123";
        compra.setIdUsuario(novoId);
        assertEquals(novoId, compra.getIdUsuario());
    }
}
