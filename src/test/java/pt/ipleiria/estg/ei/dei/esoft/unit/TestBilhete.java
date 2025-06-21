package pt.ipleiria.estg.ei.dei.esoft.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipleiria.estg.ei.dei.esoft.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TestBilhete {
    private Sessao sessao;
    private Lugar lugar;
    private Filme filme;
    private Sala sala;

    @BeforeEach
    void setUp() {
        // Create a real Filme instance
        filme = new Filme("Test Movie", true, "2025-06-21", 4.5, "test.jpg");
        
        // Create a real Sala instance
        sala = new Sala("Sala 1", "sim", 8, 10);
        
        // Create a real Sessao instance
        sessao = new Sessao(filme, LocalDateTime.now(), sala, 10.0);
        
        // Create a real Lugar instance
        lugar = new Lugar(1, 1, true, false); // Row B, Seat 2, VIP, not occupied
    }

    @Test
    void testConstructorAndGetters() {
        Bilhete bilhete = new Bilhete(sessao, lugar);

        assertNotNull(bilhete.getId());
        assertEquals(sessao.getId(), bilhete.getIdSessao());
        assertEquals(lugar.getIdentificacao(), bilhete.getIdLugar());
        assertEquals(12.0, bilhete.getPreco()); // Base price 10.0 + 2.0 for VIP
        assertEquals(sessao, bilhete.getSessao());
        assertEquals(lugar, bilhete.getLugar());
    }

    @Test
    void testRestaurarReferencias() {
        Bilhete bilhete = new Bilhete(null, null);
        bilhete.restaurarReferencias(sessao, lugar);
        assertEquals(sessao, bilhete.getSessao());
        assertEquals(lugar, bilhete.getLugar());
    }

    @Test
    void testToStringWithValidData() {
        Bilhete bilhete = new Bilhete(sessao, lugar);
        String str = bilhete.toString();
        assertTrue(str.contains("Test Movie"));
        assertTrue(str.contains("B2 (VIP)"));
        assertTrue(str.contains("12.00€")); // Base price 10.0 + 2.0 for VIP
    }

    @Test
    void testToStringWithNulls() {
        Bilhete bilhete = new Bilhete(null, null);
        String str = bilhete.toString();
        assertTrue(str.contains("Filme não disponível"));
        assertTrue(str.contains("Lugar não definido"));
    }
}
