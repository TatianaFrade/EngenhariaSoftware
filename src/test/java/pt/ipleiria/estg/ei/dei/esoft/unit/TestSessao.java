package pt.ipleiria.estg.ei.dei.esoft.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipleiria.estg.ei.dei.esoft.Filme;
import pt.ipleiria.estg.ei.dei.esoft.Sala;
import pt.ipleiria.estg.ei.dei.esoft.Sessao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TestSessao {
    private Filme filme;
    private Sala sala;
    private LocalDateTime dataHora;
    
    @BeforeEach
    void setUp() {
        filme = new Filme("Test Movie", true, "2025-06-21", 4.5, "test.jpg");
        sala = new Sala("Sala 1", "sim", 8, 10);
        dataHora = LocalDateTime.now();
    }
    
    @Test
    void testConstructorWithStringSala() {
        Sessao sessao = new Sessao(filme, dataHora, "Sala 1", 10.0);
        
        assertNotNull(sessao.getId());
        assertEquals(filme, sessao.getFilme());
        assertEquals(dataHora, sessao.getDataHora());
        assertEquals("Sala 1", sessao.getNomeSala());
        assertEquals(10.0, sessao.getPreco());
    }
    
    @Test
    void testConstructorWithSalaObject() {
        Sessao sessao = new Sessao(filme, dataHora, sala, 10.0);
        
        assertNotNull(sessao.getId());
        assertEquals(filme, sessao.getFilme());
        assertEquals(dataHora, sessao.getDataHora());
        assertEquals(sala, sessao.getSala());
        assertEquals("Sala 1", sessao.getNomeSala());
        assertEquals(10.0, sessao.getPreco());
    }
    
    @Test
    void testGetDataHoraFormatada() {
        Sessao sessao = new Sessao(filme, dataHora, sala, 10.0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String expected = dataHora.format(formatter);
        assertEquals(expected, sessao.getDataHoraFormatada());
    }
    
    @Test
    void testToString() {
        Sessao sessao = new Sessao(filme, dataHora, sala, 10.0);
        String expected = filme.getNome() + " - " + sessao.getDataHoraFormatada() + " - Sala " + sala.getNome();
        assertEquals(expected, sessao.toString());
    }
    
    @Test
    void testGetId() {
        Sessao sessao = new Sessao(filme, dataHora, sala, 10.0);
        String id = sessao.getId();
        assertNotNull(id);
        assertEquals(id, sessao.getId()); // Should return the same ID on subsequent calls
    }
    
    @Test
    void testSerializable() {
        Sessao sessao = new Sessao(filme, dataHora, sala, 10.0);
        assertTrue(sessao instanceof java.io.Serializable);
    }
}
