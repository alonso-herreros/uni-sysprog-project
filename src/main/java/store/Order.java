package store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Order extends ProductList {

    static int maxOrderID = 0;
    protected int orderID;
    protected Person client, employee;
    protected String dir;

    protected static final String[] def = {"00000001|John|Doe|email@example.com", "00000002|Jane|Doe|email2@example.com"};


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
        super();
        set(data);
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
        getters.put("dir", () -> getDir());
    }
    @Override
    protected void defineSetters() {
        super.defineSetters();
        setters.put("orderID", (data) -> setOrderID(Integer.parseInt(data)));
        setters.put("client", (data) -> setClient(Person.readFromString(data)));
        setters.put("employee", (data) -> setEmployee(Person.readFromString(data)));
        setters.put("dir", (data) -> setDir(data));
    }
    @Override
    public void set(String... data) {
        if (data.length == 0) {
            data = getDef();
        }

        ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(data));
        try { // This is how i check that the first entry does not correspond to an ID.
            Integer.parseInt(dataList.get(0));
        }
        catch(NumberFormatException e) {
            dataList.add(0, Integer.toString(maxOrderID++));
        }

        try { // If the fourth entry is a product, no dir was specified
            new StockableProduct(dataList.get(3));
            dataList.add(3, " ");
        }
        catch(IndexOutOfBoundsException e) { // If there is no fourth entry...
            dataList.add(3, " ");
        }
        catch(IllegalArgumentException e) {} // If the fourth entry is not a product, it must be a dir

        super.set(dataList.toArray(new String[0]));
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
    
    public String getDir() { return dir; }
    public void setDir(String dir) {
        if (dir.equals(" ") || dir.equals("")) {
            dir = null;
        }
        this.dir = dir;
    }


    public static Order readFromString(String string) {
        return new Order(string);
    }

    public static Order readFromStdio() {
        return new Order(stringFromStdio());
    }
    public static Order readFromFile(String filepath) {
        Order order = new Order(getParamsFromFilename(filepath));
        for (String line : stringsFromFile(filepath)) {
            order.add(StockableProduct.readFromString(line));
        }
        
        return order;
    }

    public void writeToFile() {
        if (getDir() == null) {
            throw new IllegalStateException("Order directory not set.");
        }
        String filename = String.format("%03d_%08d_%08d.txt", getOrderID(), getClient().getID(), getEmployee().getID());

        writeToFile(getDir() +  "\\" + filename);
    }

    protected static String[] getParamsFromFilename(String filepath) {
        String[] params = new String[3];
        Matcher m = Pattern.compile("(\\d+)_(\\d+)_(\\d+)\\.txt").matcher(filepath);
        m.find();
        try {
            params[0] = m.group(1); // order ID
            params[1] = m.group(2); // client ID
            params[2] = m.group(3); // employee ID
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(String.format("Invalid filepath: %s.", filepath));
        }
        return params;
    }
}
