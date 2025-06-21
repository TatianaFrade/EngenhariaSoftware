package pt.ipleiria.estg.ei.dei.esoft.unit;

import org.junit.jupiter.api.Test;
import pt.ipleiria.estg.ei.dei.esoft.Filme;

import static org.junit.jupiter.api.Assertions.*;

class TestFilme {
    
    @Test
    void testConstructorAndGetters() {
        String nome = "Inception";
        boolean legendado = true;
        String dataLancamento = "2010-07-16";
        double rate = 8.8;
        String imagemPath = "/images/inception.jpg";
        
        Filme filme = new Filme(nome, legendado, dataLancamento, rate, imagemPath);
        
        assertEquals(nome, filme.getNome());
        assertTrue(filme.isLegendado());
        assertEquals(dataLancamento, filme.getDataLancamento());
        assertEquals(rate, filme.getRate());
        assertEquals(imagemPath, filme.getImagemPath());
    }
    
    @Test
    void testFilmeNaoLegendado() {
        Filme filme = new Filme("Avatar", false, "2009-12-18", 7.8, "/images/avatar.jpg");
        assertFalse(filme.isLegendado());
    }
    
    @Test
    void testFilmeComValoresLimite() {
        // Testando com uma nota mínima
        Filme filmeNotaMinima = new Filme("Filme Ruim", true, "2025-01-01", 0.0, "/images/ruim.jpg");
        assertEquals(0.0, filmeNotaMinima.getRate());
        
        // Testando com uma nota máxima
        Filme filmeNotaMaxima = new Filme("Filme Ótimo", true, "2025-01-01", 10.0, "/images/otimo.jpg");
        assertEquals(10.0, filmeNotaMaxima.getRate());
    }
    
    @Test
    void testFilmeComCaminhoImagemVazio() {
        Filme filme = new Filme("Sem Imagem", true, "2025-01-01", 5.0, "");
        assertEquals("", filme.getImagemPath());
    }
    
    @Test
    void testSerializable() {
        // Test if Filme implements Serializable correctly
        Filme filme = new Filme("Test", true, "2025-01-01", 5.0, "/test.jpg");
        assertTrue(filme instanceof java.io.Serializable);
    }
}
