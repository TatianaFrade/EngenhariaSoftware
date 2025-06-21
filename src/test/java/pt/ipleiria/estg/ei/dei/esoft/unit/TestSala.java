package pt.ipleiria.estg.ei.dei.esoft.unit;

import org.junit.jupiter.api.Test;
import pt.ipleiria.estg.ei.dei.esoft.Lugar;
import pt.ipleiria.estg.ei.dei.esoft.Sala;

import static org.junit.jupiter.api.Assertions.*;

class TestSala {
    
    @Test
    void testConstructorAndGetters() {
        String nome = "Sala 1";
        String acessibilidade = "sim";
        int filas = 8;
        int colunas = 10;
        
        Sala sala = new Sala(nome, acessibilidade, filas, colunas);
        
        assertEquals(nome, sala.getNome());
        assertEquals(acessibilidade, sala.getAcessibilidade());
        assertEquals(filas * colunas, sala.getTotalLugares());
        assertNotNull(sala.getLugares());
        assertEquals(filas * colunas, sala.getLugares().size());
    }
    
    @Test
    void testSalaComAcessibilidadeInativa() {
        Sala sala = new Sala("Sala 2", "não", 8, 10);
        assertEquals("não", sala.getAcessibilidade());
    }
    
    @Test
    void testOcupacaoLugares() {
        Sala sala = new Sala("Sala Test", "sim", 8, 10);
        int lugaresOcupadosIniciais = sala.getLugaresOcupados(); // Alguns lugares são ocupados na inicialização
        
        // Tenta ocupar um lugar disponível
        assertTrue(sala.ocuparLugar(0, 0));
        assertEquals(lugaresOcupadosIniciais + 1, sala.getLugaresOcupados());
        
        // Tenta ocupar o mesmo lugar novamente
        assertFalse(sala.ocuparLugar(0, 0));
        
        // Liberta o lugar
        assertTrue(sala.libertarLugar(0, 0));
        assertEquals(lugaresOcupadosIniciais, sala.getLugaresOcupados());
    }
    
    @Test
    void testGetLugar() {
        Sala sala = new Sala("Sala Test", "sim", 8, 10);
        Lugar lugar = sala.getLugar(0, 0);
        
        assertNotNull(lugar);
        assertEquals(0, lugar.getFila());
        assertEquals(0, lugar.getColuna());
    }
    
    @Test
    void testPercentualOcupacao() {
        Sala sala = new Sala("Sala Test", "sim", 2, 2);
        double ocupacaoInicial = sala.getPercentualOcupacao();
        
        sala.ocuparLugar(0, 0);
        double novaOcupacao = sala.getPercentualOcupacao();
        assertTrue(novaOcupacao > ocupacaoInicial);
    }
    
    @Test
    void testLugaresDisponiveis() {
        Sala sala = new Sala("Sala Test", "sim", 2, 2);
        int disponiveis = sala.getLugaresDisponiveis();
        
        sala.ocuparLugar(0, 0);
        assertEquals(disponiveis - 1, sala.getLugaresDisponiveis());
    }
    
    @Test
    void testSerializable() {
        Sala sala = new Sala("Test", "sim", 8, 10);
        assertTrue(sala instanceof java.io.Serializable);
    }
}
