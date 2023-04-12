package warehouse;

import java.util.ArrayList;

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
		setters.put("client", (data) -> setClient(Person.fromString(data)));
		setters.put("employee", (data) -> setEmployee(Person.fromString(data)));
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
	

	public static Order fromString(String string) {
		return new Order(string);
	}
}
