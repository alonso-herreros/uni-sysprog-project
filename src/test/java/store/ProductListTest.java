package store;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class ProductListTest {

    private ProductList pl = new ProductList();
    private StockableProduct sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.0|20.0");
    private StockableProduct sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.0|30.0");

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
    public void testGetWithIntIndex() {
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(sp1, pl.get(0));
        assertEquals(sp2, pl.get(1));
    }

    @Test
    public void testGetWithStringIndex() {
        pl.add(sp1);
        pl.add(sp2);
        assertEquals(sp1.toString(), pl.get("0"));
        assertEquals(sp2.toString(), pl.get("1"));
    }

    @Test
    public void testGetWithInvalidIndex() {
        pl.add(sp1);
        pl.add(sp2);
        assertThrows(IndexOutOfBoundsException.class, () -> pl.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> pl.get(2));
    }

    @Test
    public void testGetWithInvalidStringIndex() {
        pl.add(sp1);
        pl.add(sp2);
        assertThrows(IllegalArgumentException.class, () -> pl.get("-1"));
        assertThrows(IllegalArgumentException.class, () -> pl.get("2"));
    }

    @Test
    public void testSet() {
        pl.add(sp2);
        pl.add(sp1);
        pl.add(new StockableProduct());
        pl.set(sp1.toString(), sp2.toString());
        assertEquals(sp1, pl.get(0));
        assertEquals(sp2, pl.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> pl.get(2));
    }

    @Test
    public void testSetWithIntIndex() {
        pl.add(sp1);
        pl.set(0, sp2);
        assertEquals(sp2, pl.get(0));
    }

    @Test
    public void testSetWithIntIndexInvalid() {
        pl.add(sp1);
        assertThrows(IndexOutOfBoundsException.class, () -> pl.set(-1, sp2));
        assertThrows(IndexOutOfBoundsException.class, () -> pl.set(1, sp2));
    }

    @Test
    public void testSetIndexWithIDClash() {
        pl.add(sp1);
        pl.add(sp2);
        assertThrows(IllegalArgumentException.class, () -> pl.set(0, sp2));
    }

    @Test
    public void testAdd() {
        assertTrue(pl.add(sp1));
    }

    @Test
    public void testAddWithSameID() {
        pl.add(sp1);
        assertEquals(30, pl.get(0).getNumUnits());
        assertEquals(300.0, pl.get(0).getTotalCost(), 0.001);

        assertTrue(pl.add(sp1));
        assertEquals(1, pl.size());
        assertEquals(60, pl.get(0).getNumUnits());
        assertEquals(600.0, pl.get(0).getTotalCost(), 0.001);
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
        ProductList pl = ProductList.readFromFile("src\\test\\resources\\testProductList1.txt");
        assertEquals(2, pl.size());
        assertEquals(sp1, pl.getList().get(0));
        assertEquals(sp2, pl.getList().get(1));
    }

    @Test
    public void testToFile() {
        String filepath = "src\\test\\tmp\\testProductList.txt";
        ProductList pl = new ProductList();
        pl.add(sp1);
        pl.add(sp2);
        pl.writeToFile(filepath);
        ProductList pl2 = ProductList.readFromFile(filepath);
        ArrayList<String> expected = WarehouseElement.stringsFromFile(filepath);
        int i = 0;
        for (String s : expected) {
            assertTrue(pl2.get(i++).equals(new StockableProduct(s)));
        }
        assertEquals(2, pl2.size());
        assertEquals(sp1, pl2.getList().get(0));
        assertEquals(sp2, pl2.getList().get(1));
    }
}