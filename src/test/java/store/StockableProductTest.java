package store;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


class StockableProductTest {

    private StockableProduct sp = new StockableProduct("1|product|brand|m|true|unit|10|1.5|2.5");


    @Test
    void testConstructorWithEmpty() {
        StockableProduct sp2 = new StockableProduct();
        assertEquals(0, sp2.getProductID());
        assertEquals("Product", sp2.getName());
        assertEquals('m', sp2.getCategory());
        assertEquals(false, sp2.isCountable());
        assertEquals("Units", sp2.getMeasurementUnit());
        assertEquals(0, sp2.getNumUnits());
        assertEquals(0f, sp2.getCostPerUnit());
        assertEquals(0f, sp2.getPricePerUnit());
        assertEquals(0f, sp2.getTotalCost());
        assertEquals(0f, sp2.getTotalPrice());
    }

    @Test
    void testConstructorWithString() {
        StockableProduct sp2 = new StockableProduct("1|product|brand|m|true|unit|10|1.5|2.5");
        assertEquals(1, sp2.getProductID());
        assertEquals("product", sp2.getName());
        assertEquals('m', sp2.getCategory());
        assertEquals(true, sp2.isCountable());
        assertEquals("unit", sp2.getMeasurementUnit());
        assertEquals(10, sp2.getNumUnits());
        assertEquals(1.5f, sp2.getCostPerUnit());
        assertEquals(2.5f, sp2.getPricePerUnit());
        assertEquals(15f, sp2.getTotalCost());
        assertEquals(25f, sp2.getTotalPrice());
    }

    @Test
    void testGet() {
        assertEquals("1", sp.get("productID"));
        assertEquals("product", sp.get("name"));
        assertEquals("brand", sp.get("brand"));
        assertEquals("m", sp.get("category"));
        assertEquals("true", sp.get("isCountable"));
        assertEquals("unit", sp.get("measurementUnit"));
        assertEquals("10", sp.get("numUnits"));
        assertEquals("1.5", sp.get("costPerUnit"));
        assertEquals("2.5", sp.get("pricePerUnit"));
        assertEquals("15.0", sp.get("totalCost"));
        assertEquals("25.0", sp.get("totalPrice"));
    }

    @Test
    void settersTest() {
        StockableProduct sp = new StockableProduct();
        sp.setProductID(1);
        assertEquals(1, sp.getProductID());
        sp.setNumUnits(10);
        assertEquals(10, sp.getNumUnits());
        sp.setCostPerUnit(1.5f);
        assertEquals(1.5f, sp.getCostPerUnit());
        sp.setPricePerUnit(2.5f);
        assertEquals(2.5f, sp.getPricePerUnit());
        sp.setName("product");
        assertEquals("product", sp.getName());
        sp.setBrand("brand");
        assertEquals("brand", sp.getBrand());
        sp.setCategory('f');
        assertEquals('f', sp.getCategory());
        sp.setCountable(true);
        assertEquals(true, sp.isCountable);
        sp.setMeasurementUnit("unit");
        assertEquals("unit", sp.getMeasurementUnit());
    }

    @Test
    void constructorsTest() {
        StockableProduct sp1 = new StockableProduct();
        assertEquals(0, sp1.getProductID());
        assertEquals("Product", sp1.getName());
        assertEquals('m', sp1.getCategory());
        assertEquals(false, sp1.isCountable);
        assertEquals("Units", sp1.getMeasurementUnit());
        assertEquals(0, sp1.getNumUnits());
        assertEquals(0f, sp1.getCostPerUnit());
        assertEquals(0f, sp1.getPricePerUnit());
        assertEquals(0f, sp1.getTotalCost());
        assertEquals(0f, sp1.getTotalPrice());

        ArrayList<String> params = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            params.add("0");
        }
        params.set(1, "product");
        params.set(2, "brand");
        params.set(3, "f");
        params.set(4, "true");
        params.set(5, "unit");
        params.set(6, "10");
        params.set(7, "1.5");
        params.set(8, "2.5");
        StockableProduct sp2 = new StockableProduct(params);
        assertEquals(0, sp2.getProductID());
        assertEquals("product", sp2.getName());
        assertEquals("brand", sp2.getBrand());
        assertEquals('f', sp2.getCategory());
        assertEquals(true, sp2.isCountable);
        assertEquals("unit", sp2.getMeasurementUnit());
        assertEquals(10, sp2.getNumUnits());
        assertEquals(1.5f, sp2.getCostPerUnit());
        assertEquals(2.5f, sp2.getPricePerUnit());
        assertEquals(15f, sp2.getTotalCost());
        assertEquals(25f, sp2.getTotalPrice());

        StockableProduct sp3 = new StockableProduct("1|product|brand|f|true|unit|10|1.5|2.5");
        assertEquals(1, sp3.getProductID());
        assertEquals("product", sp3.getName());
        assertEquals("brand", sp3.getBrand());
        assertEquals('f', sp3.getCategory());
        assertEquals(true, sp3.isCountable);
        assertEquals("unit", sp3.getMeasurementUnit());
        assertEquals(10, sp3.getNumUnits());
        assertEquals(1.5f, sp3.getCostPerUnit());
        assertEquals(2.5f, sp3.getPricePerUnit());
        assertEquals(15f, sp3.getTotalCost());
        assertEquals(25f, sp3.getTotalPrice());

        StockableProduct sp4 = new StockableProduct(1, "product", "brand", 'f', true, "unit", 10, 1.5f, 2.5f);
        assertEquals(1, sp4.getProductID());
        assertEquals("product", sp4.getName());
        assertEquals("brand", sp4.getBrand());
        assertEquals('f', sp4.getCategory());
        assertEquals(true, sp4.isCountable);
        assertEquals("unit", sp4.getMeasurementUnit());
        assertEquals(10, sp4.getNumUnits());
        assertEquals(1.5f, sp4.getCostPerUnit());
        assertEquals(2.5f, sp4.getPricePerUnit());
        assertEquals(15f, sp4.getTotalCost());
        assertEquals(25f, sp4.getTotalPrice());
    }

    @Test
    void setExceptionTest() {
        StockableProduct sp = new StockableProduct();
        assertThrows(IllegalArgumentException.class, () -> sp.set(new String[]{"0"}));
        assertThrows(IllegalArgumentException.class, () -> sp.set(new String[]{"0", "", "", "", "", "", "", "", "", ""}));
    }

    @Test
    void fromStringTest() {
        StockableProduct sp = StockableProduct.readFromString("1|product|brand|f|true|unit|10|1.5|2.5");
        assertEquals(1, sp.getProductID());
        assertEquals("product", sp.getName());
        assertEquals("brand", sp.getBrand());
        assertEquals('f', sp.getCategory());
        assertEquals(true, sp.isCountable);
        assertEquals("unit", sp.getMeasurementUnit());
        assertEquals(10, sp.getNumUnits());
        assertEquals(1.5f, sp.getCostPerUnit());
        assertEquals(2.5f, sp.getPricePerUnit());
        assertEquals(15f, sp.getTotalCost());
        assertEquals(25f, sp.getTotalPrice());
    }

    @Test
    void toFromStringTest() {
        StockableProduct sp1 = new StockableProduct(1, "product", "brand", 'm', true, "unit", 10, 1.5f, 2.5f);
        StockableProduct sp2 = StockableProduct.readFromString(sp1.toString());
        assertEquals(sp1.getProductID(), sp2.getProductID());
        assertEquals(sp1.getName(), sp2.getName());
        assertEquals(sp1.getBrand(), sp2.getBrand());
        assertEquals(sp1.getCategory(), sp2.getCategory());
        assertEquals(sp1.isCountable, sp2.isCountable);
        assertEquals(sp1.getMeasurementUnit(), sp2.getMeasurementUnit());
        assertEquals(sp1.getNumUnits(), sp2.getNumUnits());
        assertEquals(sp1.getCostPerUnit(), sp2.getCostPerUnit());
        assertEquals(sp1.getPricePerUnit(), sp2.getPricePerUnit());
        assertEquals(sp1.getTotalCost(), sp2.getTotalCost());
        assertEquals(sp1.getTotalPrice(), sp2.getTotalPrice());
    }
}