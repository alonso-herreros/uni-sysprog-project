package warehouse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonTest {

    private Person personUnderTest;

    private String testId = "123";
    private String testFirstName = "John";
    private String testLastName = "Doe";
    private String testEmail = "jdoe@test.com";

    private ArrayList<String> testParams;

    @Before
    public void setUp() {
        personUnderTest = new Person(testId, testFirstName, testLastName, testEmail);
        testParams = new ArrayList<>();
        testParams.add(testId);
        testParams.add(testFirstName);
        testParams.add(testLastName);
        testParams.add(testEmail);
    }

    @Test
    public void testDefaultConstructor() {
        Person defaultPerson = new Person();
        Assert.assertEquals(" ", defaultPerson.getId());
        Assert.assertEquals(" ", defaultPerson.getFirstName());
        Assert.assertEquals(" ", defaultPerson.getLastName());
        Assert.assertEquals(" ", defaultPerson.getEmail());
    }

    @Test
    public void testStringConstructor() {
        Person stringPerson = new Person("|" + String.join("|", testParams));
        Assert.assertEquals(testId, stringPerson.getId());
        Assert.assertEquals(testFirstName, stringPerson.getFirstName());
        Assert.assertEquals(testLastName, stringPerson.getLastName());
        Assert.assertEquals(testEmail, stringPerson.getEmail());
    }

    @Test
    public void testArrayListConstructor() {
        Person arrayListPerson = new Person(testParams);
        Assert.assertEquals(testId, arrayListPerson.getId());
        Assert.assertEquals(testFirstName, arrayListPerson.getFirstName());
        Assert.assertEquals(testLastName, arrayListPerson.getLastName());
        Assert.assertEquals(testEmail, arrayListPerson.getEmail());

        ArrayList<String> invalidParams = new ArrayList<>();
        invalidParams.add(testId);
        invalidParams.add(testFirstName);
        invalidParams.add(testLastName);
        invalidParams.add(testEmail);
        invalidParams.add("invalidParam");
        try {
            new Person(invalidParams);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Invalid number of data fields
        }
    }

    @Test
    public void testSetAndGet() {
        String[] data = {
                "456",
                "Jane",
                "Doe",
                "jane@test.com"
        };

        personUnderTest.set(data);

        Assert.assertEquals(data[0], personUnderTest.getId());
        Assert.assertEquals(data[1], personUnderTest.getFirstName());
        Assert.assertEquals(data[2], personUnderTest.getLastName());
        Assert.assertEquals(data[3], personUnderTest.getEmail());

        try {
            String invalidVarId = "invalidVar";
            personUnderTest.get(invalidVarId);
            Assert.fail("Expected exception not thrown.");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("Invalid varId"));
        } catch (Exception e) {
            Assert.fail("Unexpected exception thrown.");
        }

        HashMap<String, String> validVarIdsAndValues = new HashMap<>();
        validVarIdsAndValues.put("id", data[0]);
        validVarIdsAndValues.put("firstName", data[1]);
        validVarIdsAndValues.put("lastName", data[2]);
        validVarIdsAndValues.put("email", data[3]);

        for (Map.Entry<String, String> entry : validVarIdsAndValues.entrySet()) {
            String varId = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = personUnderTest.get(varId);
            Assert.assertEquals(expectedValue, actualValue);
        }
    }

    @Test
    public void testToString() {
        String expectedString = String.join("|", testParams);
        String actualString = personUnderTest.toString();
        Assert.assertEquals(expectedString, actualString);
    }
}