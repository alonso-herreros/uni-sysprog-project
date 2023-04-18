package store;

import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Before;


public class ProductListTest {

    private ProductList pl;
    private StockableProduct sp1;
    private StockableProduct sp2;

    @Before
    public void setUp() {
        pl = new ProductList();
        sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.0|20.0");
        sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.0|30.0");
    }

    @Test
    public void testConstructorEmpty() {
        assertEquals(0, pl.size());
    }

    @Test
    public void testConstructor1Product() {
        ProductList pl = new ProductList("((0|Pods|Tide|f|true|fl oz|30|10.0|20.0))");
        assertEquals(1, pl.size());
    }

    @Test
    public void testConstructor2Products() {
        String p1String = "(0|Pods|Tide|f|true|fl oz|30|10.0|20.0)";
        String p2String = "(1|Product2|Tide|f|true|fl oz|40|20.0|30.0)";
        ProductList pl = new ProductList(String.join("|", p1String, p2String));
        assertEquals(2, pl.size());
    }

    @Test
    public void testAdd() {
        assertTrue(pl.add(sp1));
    }

    @Test
    public void testRemove() {
        pl.add(sp1);
        assertTrue(pl.remove(sp1));
        assertFalse(pl.remove(sp1));
    }

    @Test
    public void testSize() {
        assertEquals(0, pl.size());
        pl.add(sp1);
        assertEquals(1, pl.size());
    }

    @Test
    public void testToArray() {
        pl.add(sp1);
        pl.add(sp2);
        Object[] expected = {sp1, sp2};
        assertArrayEquals(expected, pl.toArray());
    }

    @Test
    public void testIterator() {
        ProductList pl = new ProductList();
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
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(1100.0, pl.calculateCost(), 0.001);
    }

    @Test
    public void testCalculatePrice() {
        ProductList pl = new ProductList();
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(1800.0, pl.calculatePrice(), 0.001);
    }

    @Test
    public void testCalculateBenefit() {
        ProductList pl = new ProductList();
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(700.0, pl.calculateBenefit(), 0.001);
    }

    @Test
    public void testMostExpensiveProduct() {
        ProductList pl = new ProductList();
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(sp2, pl.mostExpensiveProduct());
    }

    @Test
    public void testCheapestProduct() {
        ProductList pl = new ProductList();
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(sp1, pl.cheapestProduct());
    }

    @Test
    public void testToString() {
        ProductList pl = new ProductList();
        pl.add(sp1);
        pl.add(sp2);
        String expected = "((1|Pods|Tide|f|true|fl oz|30|10.0|20.0)|(2|Product2|Tide|f|true|fl oz|40|20.0|30.0))";
        assertEquals(expected, pl.toString());
    }
    
    @Test
    public void testFromFile() {
        ProductList pl = ProductList.readFromFile("src\\test\\testObjectFiles\\testProductList1.txt");
        assertEquals(2, pl.size());
        assertEquals(sp1, pl.getList().get(0));
        assertEquals(sp2, pl.getList().get(1));
    }
}