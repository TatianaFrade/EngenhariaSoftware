package pt.ipleiria.estg.ei.dei.esoft.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipleiria.estg.ei.dei.esoft.Stock;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class StockTestCase {

    private Stock stock;
    private Date dataFutura;
    private Date dataPassada;

    @BeforeEach
    void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 10);
        dataFutura = cal.getTime();

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -10);
        dataPassada = cal.getTime();

        stock = new Stock("Nachos", 20, dataFutura, "Fornecedor A");
    }

    @Test
    void testConstrutorEGetters() {
        assertEquals("Nachos", stock.getItem());
        assertEquals(20, stock.getQuantidade());
        assertEquals("Fornecedor A", stock.getFornecedor());
        assertEquals(dataFutura, stock.getDataValidade());
    }

    @Test
    void testSetQuantidadeNaoNegativa() {
        stock.setQuantidade(-5);
        assertEquals(0, stock.getQuantidade());
    }

    @Test
    void testAumentarQuantidade() {
        stock.aumentar(5);
        assertEquals(25, stock.getQuantidade());
    }

    @Test
    void testDiminuirQuantidade() {
        stock.diminuir(10);
        assertEquals(10, stock.getQuantidade());
    }

    @Test
    void testDiminuirParaZero() {
        stock.diminuir(100);
        assertEquals(0, stock.getQuantidade());
    }

    @Test
    void testEstaVencidoFalse() {
        assertFalse(stock.estaVencido());
    }

    @Test
    void testEstaVencidoTrue() {
        stock.setDataValidade(dataPassada);
        assertTrue(stock.estaVencido());
    }

    @Test
    void testEqualsAndHashCode() {
        Stock igual = new Stock("Nachos", 99, dataFutura, "Fornecedor A");
        assertEquals(stock, igual);
        assertEquals(stock.hashCode(), igual.hashCode());

        Stock diferente = new Stock("Nachos", 99, dataFutura, "Outro Fornecedor");
        assertNotEquals(stock, diferente);
    }
}