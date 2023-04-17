package store;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class PersonTest {

    private Person personUnderTest;

    private static final String[] DEF = {"00000001", "Name", "LastName", "email@example.com"};

    private final String testId = "00000123";
    private final String testFirstName = "John";
    private final String testLastName = "Doe";
    private final String testEmail = "jdoe@test.com";

    private ArrayList<String> testParams;

    @Before
    public void setUp() {
        personUnderTest = new Person(testId, testFirstName, testLastName, testEmail);
        testParams = new ArrayList<String>();
        testParams.add(testId);
        testParams.add(testFirstName);
        testParams.add(testLastName);
        testParams.add(testEmail);
    }


    @Test
    public void testDefaultConstructor() {
        Person defaultPerson = new Person();
        assertEquals(DEF[0], defaultPerson.get("id"));
        assertEquals(DEF[1], defaultPerson.getFirstName());
        assertEquals(DEF[2], defaultPerson.getLastName());
        assertEquals(DEF[3], defaultPerson.getEmail());
    }

    @Test
    public void testFullConstructor() {
        Person fullPerson = new Person(testId, testFirstName, testLastName, testEmail);
        assertEquals(testId, fullPerson.get("id"));
        assertEquals(testFirstName, fullPerson.getFirstName());
        assertEquals(testLastName, fullPerson.getLastName());
        assertEquals(testEmail, fullPerson.getEmail());
        assertTrue(fullPerson.equals(personUnderTest));
    }

    @Test
    public void testStringConstructor() {
        Person stringPerson = new Person(String.join("|", testParams));
        assertEquals(testId, stringPerson.get("id"));
        assertEquals(testFirstName, stringPerson.getFirstName());
        assertEquals(testLastName, stringPerson.getLastName());
        assertEquals(testEmail, stringPerson.getEmail());
    }

    @Test
    public void testArrayListConstructor() {
        Person arrayListPerson = new Person(testParams);
        assertEquals(testId, arrayListPerson.get("id"));
        assertEquals(testFirstName, arrayListPerson.getFirstName());
        assertEquals(testLastName, arrayListPerson.getLastName());
        assertEquals(testEmail, arrayListPerson.getEmail());
    }

    @Test
    public void testInvalidArrayListConstructor() {
        ArrayList<String> invalidParams = new ArrayList<String>() {{
            addAll(testParams);
            add("invalidParam");
        }};
        assertThrows(IllegalArgumentException.class, () -> new Person(invalidParams));
    }

    @Test
    public void testInvalidStringConstructor() {
        String invalidString = String.join("|", testParams) + "|invalidParam";
        assertThrows(IllegalArgumentException.class, () -> new Person(invalidString));
    }

    @Test
    public void testNonsenseStringConstructor() {
        String nonsenseString = "nonsense";
        assertThrows(IllegalArgumentException.class, () -> new Person(nonsenseString));
    }


    @Test
    public void testSetWithStringArray() {
        String[] data = {
                "00000456",
                "Jane",
                "Doe",
                "jane@test.com"
        };
        personUnderTest.set(data);

        assertEquals(data[0], personUnderTest.get("id"));
        assertEquals(data[1], personUnderTest.getFirstName());
        assertEquals(data[2], personUnderTest.getLastName());
        assertEquals(data[3], personUnderTest.getEmail());
    }
    @Test
    public void testSetWithStringArgs() {
        String id = "00000456";
        String firstName = "Jane";
        String lastName = "Doe";
        String email = "jane@test.com";
        personUnderTest.set(id, firstName, lastName, email);
        assertEquals(id, personUnderTest.get("id"));
        assertEquals(firstName, personUnderTest.getFirstName());
        assertEquals(lastName, personUnderTest.getLastName());
        assertEquals(email, personUnderTest.getEmail());
    }
    @Test
    public void testSetWithString() {
        String[] data = {
                "00000456",
                "Jane",
                "Doe",
                "jane@test.com"
        };
        String dataString = String.join("|", data);
        personUnderTest.set(dataString);
        assertEquals(data[0], personUnderTest.get("id"));
        assertEquals(data[1], personUnderTest.getFirstName());
        assertEquals(data[2], personUnderTest.getLastName());
        assertEquals(data[3], personUnderTest.getEmail());
    }
    @Test
    public void testSetWithVarIdAndValue() {
        String[] data = {
                "00000456",
                "Jane",
                "Doe",
                "jane@test.com"
        };
        String[] varIds = {
                "id",
                "firstName",
                "lastName",
                "email"
        };
        for (int i = 0; i < data.length; i++) {
            personUnderTest.set(varIds[i], data[i]);
            assertEquals(data[i], personUnderTest.get(varIds[i]));
        }
        assertEquals(data[0], personUnderTest.get("id"));
        assertEquals(data[1], personUnderTest.getFirstName());
        assertEquals(data[2], personUnderTest.getLastName());
        assertEquals(data[3], personUnderTest.getEmail());
    }

    @Test
    public void testSetWithInvalidString() {
        String invalidString = "invalidString";
        assertThrows(IllegalArgumentException.class, () -> personUnderTest.set(invalidString));
    }
    @Test
    public void testSetWithInvalidStringArray() {
        String[] invalidStringArray = {"1248", "Jane", "Doe"}; //No email
        assertThrows(IllegalArgumentException.class, () -> personUnderTest.set(invalidStringArray));
    }


    @Test
    public void testGetWithAllVarIds() {
        String[] varIds = {
                "id",
                "firstName",
                "lastName",
                "email"
        };
        for (int i = 0; i < varIds.length; i++) {
            assertEquals(testParams.get(i), personUnderTest.get(varIds[i]));
        }
    }
    @Test
    public void testGetWithInvalidVarId() {
        String invalidVarId = "invalidVarId";
        assertThrows(IllegalArgumentException.class, () -> personUnderTest.get(invalidVarId));
    }

    @Test
    public void testToString() {
        String expectedString = "(" + String.join("|", testParams) + ")";
        String actualString = personUnderTest.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testWriteToFile() {
        String filepath = "src\\test\\tmp\\testWriteToFile.txt";

        String expectedString = "(" + String.join("|", testParams) + ")";

        try {
            File file = new File(filepath);
            file.createNewFile();
            Scanner scanner = new Scanner(file);

            personUnderTest.writeToFile(filepath);
            String fileContent = scanner.nextLine();
            assertEquals(fileContent, expectedString);
            scanner.close();
        }
        catch (Exception e) {
            fail(e.getStackTrace().toString());
        }
    }

    @Test
    public void testFromFile() {
        String filepath = "src\\test\\tmp\\testFromFile.txt";
        String personString = "(" + String.join("|", testParams) + ")";

        try {
            File file = new File(filepath);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(personString);
            writer.close();

            Person personFromFile = Person.fromFile(filepath);
            assertTrue(personFromFile.equals(personUnderTest));
        }
        catch (Exception e) {
            fail(e.getStackTrace().toString());
        }
    }
}