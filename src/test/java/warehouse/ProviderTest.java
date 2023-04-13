package warehouse;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProviderTest {
    
    @Test
    public void testGetVat() {
        Provider provider = new Provider("123", "Acme Inc.", "123 Main St");
        assertEquals("123", provider.getVat());
    }
    
    @Test
    public void testGetName() {
        Provider provider = new Provider("123", "Acme Inc.", "123 Main St");
        assertEquals("Acme Inc.", provider.getName());
    }
    
    @Test
    public void testGetTaxAddress() {
        Provider provider = new Provider("123", "Acme Inc.", "123 Main St");
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
        assertEquals("(00000001A|John|Doe|email@example.com)", provider.get("contactPerson"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidVarId() {
        Provider provider = new Provider();
        provider.get("invalidVarId");
    }
    
    @Test
    public void testSetWithArrayLength4() {
        Provider provider = new Provider();
        String[] data = {"123", "Acme Inc.", "123 Main St", "( |John|Doe| )"};
        provider.set(data);
        assertEquals("123", provider.getVat());
        assertEquals("Acme Inc.", provider.getName());
        assertEquals("123 Main St", provider.getTaxAddress());
        assertTrue(new Person("( |John|Doe| )").equals(provider.getContactPerson()));
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
        assertEquals("(123|Acme Inc.|123 Main St|(00000001A|Some|Dude|email@example.com))", provider.toString());
    }
    
    @Test
    public void testFromString() {
        String providerString = "(123|Acme Inc.|123 Main St|(00000001A|John|Doe|email@example.com))";
        Provider provider = Provider.fromString(providerString);
        assertTrue("123".equals(provider.getVat()));
        assertTrue("Acme Inc.".equals(provider.getName()));
        assertTrue("123 Main St".equals(provider.getTaxAddress()));
        assertTrue(new Person("John", "Doe").equals(provider.getContactPerson()));
    }
}