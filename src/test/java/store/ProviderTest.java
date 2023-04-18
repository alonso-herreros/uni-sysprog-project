package store;

import static org.junit.Assert.*;

import org.junit.Test;


public class ProviderTest {
    
    @Test
    public void testAttributeGetters() {
        Provider provider = new Provider("00000123", "Acme Inc.", "123 Main St");
        assertEquals("00000123", provider.get("vat"));
        assertEquals("Acme Inc.", provider.getName());
        assertEquals("123 Main St", provider.getTaxAddress());
    }
    
    @Test
    public void testGetContactPerson() {
        Provider provider = new Provider("123", "Acme Inc.", "123 Main St", new Person("John", "Doe"));
        assertTrue(new Person("John", "Doe").equals(provider.getContactPerson()));
    }
    
    @Test
    public void testGetContactPersonAsString() {
        Provider provider = new Provider("123", "Acme Inc.", "123 Main St", new Person("John", "Doe"));
        assertEquals("(00000001|John|Doe|email@example.com)", provider.get("contactPerson"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidVarId() {
        Provider provider = new Provider();
        provider.get("invalidVarId");
    }
    
    @Test
    public void testSetWithArrayLength4() {
        Provider provider = new Provider();
        String[] data = {"123", "Acme Inc.", "123 Main St", "(0|John|Doe| )"};
        provider.set(data);
        assertEquals("00000123", provider.get("vat"));
        assertEquals("Acme Inc.", provider.getName());
        assertEquals("123 Main St", provider.getTaxAddress());
        assertTrue(new Person("(0|John|Doe| )").equals(provider.getContactPerson()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidArrayLength() {
        Provider provider = new Provider();
        String[] data = {"123", "Acme Inc.", "123 Main St"};
        provider.set(data);
    }
    
    @Test
    public void testToString() {
        Provider provider = new Provider("123", "Acme Inc.", "123 Main St", new Person("Some", "Dude"));
        assertEquals("(00000123|Acme Inc.|123 Main St|(00000001|Some|Dude|email@example.com))", provider.toString());
    }
    
    @Test
    public void testFromString() {
        String providerString = "(123|Acme Inc.|123 Main St|(00000001|John|Doe|email@example.com))";
        Provider provider = Provider.readFromString(providerString);
        assertTrue("00000123".equals(provider.get("vat")));
        assertTrue("Acme Inc.".equals(provider.getName()));
        assertTrue("123 Main St".equals(provider.getTaxAddress()));
        assertTrue(new Person("John", "Doe").equals(provider.getContactPerson()));
    }
}