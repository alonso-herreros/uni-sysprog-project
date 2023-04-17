package store;

import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Before;


public class ProductListTest {

    private ProductList pl;
    private StockableProduct sp;

    @Before
    public void setUp() {
        pl = new ProductList();
        sp = new StockableProduct("0|Pods|Tide|f|true|fl oz|30|10.0|20.0");
    }

    @Test
    public void testAdd() {
        assertTrue(pl.add(sp));
    }

    @Test
    public void testRemove() {
        pl.add(sp);
        assertTrue(pl.remove(sp));
        assertFalse(pl.remove(sp));
    }

    @Test
    public void testSize() {
        assertEquals(0, pl.size());
        pl.add(sp);
        assertEquals(1, pl.size());
    }

    @Test
    public void testToArray() {
        StockableProduct sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.0|20.0");
        StockableProduct sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.0|30.0");
        pl.add(sp1);
        pl.add(sp2);
        Object[] expected = {sp1, sp2};
        assertArrayEquals(expected, pl.toArray());
    }

    @Test
    public void testIterator() {
        ProductList pl = new ProductList();
        StockableProduct sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.0|20.0");
        StockableProduct sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.0|30.0");
        pl.add(sp1);
        pl.add(sp2);
        String expected = "(1|Pods|Tide|f|true|fl oz|30|10.0|20.0)\n(2|Product2|Tide|f|true|fl oz|40|20.0|30.0)\n";
        StringBuilder sb = new StringBuilder();
        for (StockableProduct sp : pl) {
            sb.append(sp.toString() + "\n");
        }
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testCalculateCost() {
        ProductList pl = new ProductList();
        StockableProduct sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.0|20.0");
        StockableProduct sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.0|30.0");
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(1100.0, pl.calculateCost(), 0.001);
    }

    @Test
    public void testCalculatePrice() {
        ProductList pl = new ProductList();
        StockableProduct sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.0|20.0");
        StockableProduct sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.0|30.0");
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(1800.0, pl.calculatePrice(), 0.001);
    }

    @Test
    public void testCalculateBenefit() {
        ProductList pl = new ProductList();
        StockableProduct sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.0|20.0");
        StockableProduct sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.0|30.0");
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(700.0, pl.calculateBenefit(), 0.001);
    }

    @Test
    public void testMostExpensiveProduct() {
        ProductList pl = new ProductList();
        StockableProduct sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.0|20.0");
        StockableProduct sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.0|30.0");
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(sp2, pl.mostExpensiveProduct());
    }

    @Test
    public void testCheapestProduct() {
        ProductList pl = new ProductList();
        StockableProduct sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.0|20.0");
        StockableProduct sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.0|30.0");
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(sp1, pl.cheapestProduct());
    }

    @Test
    public void testToString() {
        ProductList pl = new ProductList();
        StockableProduct sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.0|20.0");
        StockableProduct sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.0|30.0");
        pl.add(sp1);
        pl.add(sp2);
        String expected = "((1|Pods|Tide|f|true|fl oz|30|10.0|20.0)|(2|Product2|Tide|f|true|fl oz|40|20.0|30.0))";
        assertEquals(expected, pl.toString());
    }
    /* 
    @Test
    public void testFromFile() {
        ProductList pl = ProductList.readFromFile("test_products.txt");
        StockableProduct sp1 = new StockableProduct("Product1,10.0,20.0,30");
        StockableProduct sp2 = new StockableProduct("Product2,20.0,30.0,40");
        assertEquals(sp1, pl.getList().get(0));
        assertEquals(sp2, pl.getList().get(1));
    }*/
}