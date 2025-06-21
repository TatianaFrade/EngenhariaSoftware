package pt.ipleiria.estg.ei.dei.esoft.unit;

import org.junit.jupiter.api.Test;
import pt.ipleiria.estg.ei.dei.esoft.Lugar;

import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class TestLugar {
    
    @Test
    void testConstructorAndGetters() {
        int fila = 2;
        int coluna = 3;
        boolean vip = true;
        boolean ocupado = false;
        
        Lugar lugar = new Lugar(fila, coluna, vip, ocupado);
        
        assertEquals(fila, lugar.getFila());
        assertEquals(coluna, lugar.getColuna());
        assertTrue(lugar.isVip());
        assertFalse(lugar.isOcupado());
    }
    
    @Test
    void testGetIdentificacao() {
        // Test normal seat (A1)
        Lugar lugarNormal = new Lugar(0, 0, false, false);
        assertEquals("A1", lugarNormal.getIdentificacao());
        
        // Test VIP seat (C4 VIP)
        Lugar lugarVIP = new Lugar(2, 3, true, false);
        assertEquals("C4 (VIP)", lugarVIP.getIdentificacao());
    }
    
    @Test
    void testCalcularPreco() {
        double precoBase = 10.0;
        
        // Test normal seat
        Lugar lugarNormal = new Lugar(0, 0, false, false);
        assertEquals(precoBase, lugarNormal.calcularPreco(precoBase));
        
        // Test VIP seat (adds 2.00)
        Lugar lugarVIP = new Lugar(0, 0, true, false);
        assertEquals(precoBase + 2.00, lugarVIP.calcularPreco(precoBase));
    }
    
    @Test
    void testCriarLugarPorPosicao() {
        // Test VIP area (rows C-F, columns 3-8)
        Lugar lugarVIP = Lugar.criarLugarPorPosicao(3, 4); // Row D, Column 5
        assertTrue(lugarVIP.isVip());
        assertFalse(lugarVIP.isOcupado());
        
        // Test non-VIP area
        Lugar lugarNormal = Lugar.criarLugarPorPosicao(0, 0); // Row A, Column 1
        assertFalse(lugarNormal.isVip());
        assertFalse(lugarNormal.isOcupado());
        
        // Test pre-occupied seat (A3)
        Lugar lugarOcupado = Lugar.criarLugarPorPosicao(0, 2);
        assertTrue(lugarOcupado.isOcupado());
    }
    
    @Test
    void testGetCorFundo() {
        // Test occupied seat
        Lugar lugarOcupado = new Lugar(0, 0, false, true);
        assertEquals(Lugar.COR_LUGAR_OCUPADO, lugarOcupado.getCorFundo());
        
        // Test VIP seat
        Lugar lugarVIP = new Lugar(0, 0, true, false);
        assertEquals(Lugar.COR_LUGAR_VIP, lugarVIP.getCorFundo());
        
        // Test normal seat
        Lugar lugarNormal = new Lugar(0, 0, false, false);
        assertEquals(Lugar.COR_LUGAR_DISPONIVEL, lugarNormal.getCorFundo());
    }
    
    @Test
    void testGetCorTexto() {
        // Test VIP seat text color
        Lugar lugarVIP = new Lugar(0, 0, true, false);
        assertEquals(Lugar.COR_TEXTO_VIP, lugarVIP.getCorTexto());
        
        // Test normal seat text color
        Lugar lugarNormal = new Lugar(0, 0, false, false);
        assertEquals(Color.BLACK, lugarNormal.getCorTexto());
    }
    
    @Test
    void testSetOcupado() {
        Lugar lugar = new Lugar(0, 0, false, false);
        assertFalse(lugar.isOcupado());
        
        lugar.setOcupado(true);
        assertTrue(lugar.isOcupado());
    }
    
    @Test
    void testSerializable() {
        Lugar lugar = new Lugar(0, 0, false, false);
        assertTrue(lugar instanceof java.io.Serializable);
    }
}
