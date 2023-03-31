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
        assertEquals(" |John|Doe| ", provider.get("contactPerson"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidVarId() {
        Provider provider = new Provider();
        provider.get("invalidVarId");
    }
    
    @Test
    public void testSetWithArrayLength4() {
        Provider provider = new Provider();
        String[] data = {"123", "Acme Inc.", "123 Main St", " |John|Doe| "};
        provider.set(data);
        assertEquals("123", provider.getVat());
        assertEquals("Acme Inc.", provider.getName());
        assertEquals("123 Main St", provider.getTaxAddress());
        assertTrue(new Person(" |John|Doe| ").equals(provider.getContactPerson()));
    }
    
    @Test
    public void testSetWithArrayLength7() {
        Provider provider = new Provider();
        String[] data = {"123", "Acme Inc.", "123 Main St", "1", "John", "Doe", "john.doe@example.com"};
        provider.set(data);
        assertEquals("123", provider.getVat());
        assertEquals("Acme Inc.", provider.getName());
        assertEquals("123 Main St", provider.getTaxAddress());
        assertTrue(new Person("1", "John", "Doe", "john.doe@example.com").equals(provider.getContactPerson()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidArrayLength() {
        Provider provider = new Provider();
        String[] data = {"123", "Acme Inc.", "123 Main St"};
        provider.set(data);
    }
    
    @Test
    public void testToString() {
        Provider provider = new Provider("123", "Acme Inc.", "123 Main St", new Person("John", "Doe"));
        assertEquals("123|Acme Inc.|123 Main St|( |John|Doe| )", provider.toString());
    }
    
    @Test
    public void testFromString() {
        String providerString = "123|Acme Inc.|123 Main St|( |John|Doe| )";
        Provider provider = Provider.fromString(providerString);
        assertTrue("123".equals(provider.getVat()));
        assertTrue("Acme Inc.".equals(provider.getName()));
        assertTrue("123 Main St".equals(provider.getTaxAddress()));
        assertTrue(new Person("John", "Doe").equals(provider.getContactPerson()));
    }
}