package store;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;



public class ProductTest {

    private Product product = new Product("Detergent", "Tide", 'f', true, "fl oz");

    @Test
    public void testGlobalGetters() {
        assertEquals("Detergent", product.get("name"));
        assertEquals("Tide", product.get("brand"));
        assertEquals("f", product.get("category"));
        assertEquals("true", product.get("isCountable"));
        assertEquals("fl oz", product.get("measurementUnit"));
    }

    @Test
    public void testGetInvalidVarID() {
        try {
            product.get("invalidVarID");
            fail("Should have thrown an IllegalArgumentException for invalid varID.");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid varID: invalidVarID.", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithString() {
        product = new Product("Detergent|Tide|f|true|fl oz");
        assertEquals("Detergent", product.getName());
        assertEquals("Tide", product.getBrand());
        assertEquals('f', product.getCategory());
        assertTrue(product.isCountable());
        assertEquals("fl oz", product.getMeasurementUnit());
    }

    @Test
    public void testConstructorWithNotEnoughParams() {
        try {
            new Product("Detergent|Tide|f|true");
            fail("Should have thrown an IllegalArgumentException for invalid params.");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid data length 4, it must be 5.", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithBADParams() {
        try {
            new Product("Detergent|Tide|x|true|fl oz");
            fail("Should have thrown an IllegalArgumentException for invalid params.");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid category x, it must be one of the following: f, s, e, m.", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithEmpty() {
        product = new Product();
        assertEquals(" ", product.getName());
        assertEquals(" ", product.getBrand());
        assertEquals('m', product.getCategory());
        assertFalse(product.isCountable());
        assertEquals(" ", product.getMeasurementUnit());
    }

    @Test
    public void testConstructorWithArrayListParams() {
        ArrayList<String> params = new ArrayList<String>();
        params.add("Detergent");
        params.add("Tide");
        params.add("f");
        params.add("true");
        params.add("fl oz");
        product = new Product(params);
        assertEquals("Detergent", product.getName());
        assertEquals("Tide", product.getBrand());
        assertEquals('f', product.getCategory());
        assertTrue(product.isCountable());
        assertEquals("fl oz", product.getMeasurementUnit());
    }

    @Test
    public void testConstructorWithInvalidArrayListParams() {
        ArrayList<String> params = new ArrayList<String>();
        params.add("Detergent");
        params.add("Tide");
        params.add("f");
        params.add("true");
        try {
            new Product(params);
            fail("Should have thrown an IllegalArgumentException for invalid params.");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid data length 4, it must be 5.", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        assertEquals("(Detergent|Tide|f|true|fl oz)", product.toString());
    }

    @Test
    public void testFromString() {
        product = Product.readFromString("Detergent|Tide|f|true|fl oz");
        assertEquals("Detergent", product.getName());
        assertEquals("Tide", product.getBrand());
        assertEquals('f', product.getCategory());
        assertTrue(product.isCountable());
        assertEquals("fl oz", product.getMeasurementUnit());
    }
}