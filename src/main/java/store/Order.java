package store;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Order extends ProductList {

    static int maxOrderID = 0;
    protected int orderID;
    protected Person client, employee;

    protected static final String[] def = {"00000001A|John|Doe|email@example.com", "00000002B|Jane|Doe|email2@example.com"};


    // Constructors
    public Order() {
        this(def);
    } // vv FALL THROUGH (3) vv
    public Order(String string) {
        this(paramsFromString(string));
    } // vv FALL THROUGH vv
    public Order(ArrayList<String> params) {
        this(params.toArray(new String[0]));
    } // vv FALL THROUGH vv
    public Order(String... data) {
        super(data);
    }
    public Order(String clientString, String employeeString) {
        this(Integer.toString(maxOrderID++), clientString, employeeString);
    }
    public Order(Person client, Person employee) {
        this(maxOrderID++, client, employee);
    } // vv FALL THROUGH vv
    public Order(int orderID, Person client, Person employee) {
        this(Integer.toString(orderID), client.toString(), employee.toString());
    }


    // Getters and Setters
    @Override
    protected void defineGetters() {
        super.defineGetters();
        getters.put("orderID", () -> Integer.toString(getOrderID()));
        getters.put("client", () -> getClient().toString());
        getters.put("employee", () -> getEmployee().toString());
    }
    @Override
    protected void defineSetters() {
        super.defineSetters();
        setters.put("orderID", (data) -> setOrderID(Integer.parseInt(data)));
        setters.put("client", (data) -> setClient(Person.readFromString(data)));
        setters.put("employee", (data) -> setEmployee(Person.readFromString(data)));
    }

    public int getOrderID() { return orderID; }
    private void setOrderID(int orderID) {
        this.orderID = orderID;
        maxOrderID = Math.max(maxOrderID, orderID);
    }

    public Person getClient() { return client; }
    private void setClient(Person client) { this.client = client; }

    public Person getEmployee() { return employee; }
    private void setEmployee(Person employee) { this.employee = employee; }
    

    public static Order readFromString(String string) {
        return new Order(string);
    }

    public static Order readFromStdio() {
        return new Order(stringFromStdio());
    }
    public static Order readFromFile(String filepath) {
        Order order = new Order(getParamsFromFilename(filepath));
        
        return order;
    }

    protected static String[] getParamsFromFilename(String filepath) {
        String[] params = new String[3];
        Matcher m = Pattern.compile("(\\d+)_(\\d+)_(\\d+)\\.txt").matcher(filepath);
        m.find();
        try {
            params[0] = m.group(1);
            params[1] = m.group(2);
            params[2] = m.group(3);
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(String.format("Invalid filepath: %s.", filepath));
        }
        return params;
    }
}
