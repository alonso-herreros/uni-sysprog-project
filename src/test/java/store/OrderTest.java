package store;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class OrderTest {

    private StockableProduct sp1;
    private StockableProduct sp2;
	private Order order;

    private static final Person person1Def = new Person();
    private static final Person person2Def = new Person("00000002|Jane|Doe|email2@example.com");
    private static final String person1String = "00000123|John|Doe|jdoe@test.com";
    private static final String person2String = "00000456|Some|Dude|some.dude@test.com";


    @BeforeEach
    public void setUp() {
        try {
            order = new Order();
            sp1 = new StockableProduct("1|Pods|Tide|f|true|fl oz|30|10.00|20.00");
            sp2 = new StockableProduct("2|Product2|Tide|f|true|fl oz|40|20.00|30.00");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testConstructorEmpty() {
        Order order = new Order();
        assertEquals(0, order.size());
        assertTrue(order.getClient().equals(person1Def));
        assertTrue(order.getEmployee().equals(person2Def));
        assertEquals(0.0, order.getTotalCost(), 0.0);
        assertEquals(0.0, order.getTotalPrice(), 0.0);
        assertThrows(IndexOutOfBoundsException.class, () -> order.get(0));
        assertThrows(IllegalArgumentException.class, () -> order.get("0"));
    }

    @Test
    public void testConstructorEmployeeAndClientStrings() {
        order = new Order(person1String, person2String);
        assertTrue(order.getClient().equals(new Person(person1String)));
        assertTrue(order.getEmployee().equals(new Person(person2String)));
        assertEquals(0, order.size());
    }

    @Test
    public void testConstructorStringWithEmployeeAndClient() {
        String orderString = new Person(person1String).toString() + "|" + new Person(person2String).toString();
        order = new Order(orderString);
        assertTrue(order.getClient().equals(new Person(person1String)));
        assertTrue(order.getEmployee().equals(new Person(person2String)));
        assertEquals(0, order.size());
    }

    @Test
    public void testConstructorEmployeeClientAndProductStrings() {
        order = new Order(person1String, person2String, sp1.toString());
        assertEquals(Order.maxOrderID, order.getOrderID());
        assertEquals(null, order.getDir());
        assertTrue(order.getClient().equals(new Person(person1String)));
        assertTrue(order.getEmployee().equals(new Person(person2String)));
        assertEquals(1, order.size());
        assertEquals(sp1, order.get(0));
    }


    @Test
    public void testGetParamsFromFileName() {
        String filepath = "src\\test\\resources\\orders\\001_00000001_00000002.txt";

        String[] params = Order.getParamsFromFilename(filepath);

        assertEquals("001", params[0]);
        assertEquals("00000001", params[1]);
        assertEquals("00000002", params[2]);
    }

    @Test
    public void testGetParamsFromFileNameWithInvalidPath() {
        String filepath = "src\\test\\resources\\orders\\001_00000001_00000002";

        assertThrows(IllegalArgumentException.class, () -> Order.getParamsFromFilename(filepath), "Invalid filepath: " + filepath + ".");
    }

    @Test
    public void testToJSON() {
        order.setClient(new Person(person1String));
        order.setEmployee(new Person(person2String));
        order.add(sp1);
        order.add(sp2);

        String expectedJSON = "{\n" +
                "\"orderID\": " + Order.maxOrderID + ",\n" +
                "\"client\": " + new Person(person1String).toJSON() + ",\n" +
                "\"employee\": " + new Person(person2String).toJSON() + ",\n" +
                "\"dir\": \"null\",\n"+
                "\"totalCost\": 1100.00,\n" +
                "\"totalPrice\": 1800.00,\n" +
                "\"totalBenefit\": 700.00,\n" +
                "\"products\": [\n" +
                sp1.toJSON() + ",\n" +
                sp2.toJSON() + "\n" +
                "]\n" +
                "}";
        assertEquals(expectedJSON, order.toJSON());
    }

    @Test 
    public void testReadFromFile() {
        String filepath = "src\\test\\resources\\orders\\001_00000001_00000002.txt";

        Order order = Order.readFromFile(filepath);

        assertEquals(1, order.getOrderID());
        assertEquals(1, order.getClient().getID());
        assertEquals(2, order.getEmployee().getID());

        assertEquals(2, order.size());
        assertEquals(1, order.get(0).getProductID());
    }

    @Test
    public void testWriteToFile() {
        String filepath = "src\\test\\tmp";

        Order order = new Order(1, new Person(person1String), new Person(person2String));

        order.add(sp1);
        order.add(sp2);
        order.setDir(filepath);

        order.writeToFile();

        try {
            ArrayList<String> lines = WarehouseElement.stringsFromFile(filepath + "\\001_00000123_00000456.txt");
            assertEquals(2, lines.size());
            assertTrue(lines.get(0).equals(sp1.toString()));
            assertTrue(lines.get(1).equals(sp2.toString()));
        }
        catch (Exception e) {
            fail("File writing failed: " + e.getMessage());
        }
    }

}
