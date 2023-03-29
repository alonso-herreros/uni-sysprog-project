package warehouse;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class Order extends ProductList {

	private HashMap<String, Callable<String>> getters = new HashMap<String, Callable<String>>() {{
		put("orderID", () -> Integer.toString(getOrderID()));
		put("client", () -> getClient().toString());
		put("employee", () -> getEmployee().toString());
	}};
	
	static int maxOrderID = 0;
	private int orderID;
	private Person client, employee;

	public Order() {
		this(new Person(), new Person(), new ProductList());
	}
	public Order(Person client, Person employee) {
		this(client, employee, new ProductList());
	}
	public Order(Person client, Person employee, ProductList products) {
		this(maxOrderID++, client, employee);
	}
	public Order(int orderID, Person client, Person employee) {
		setOrderID(orderID);
		setClient(client);
		setEmployee(employee);
	}

	// Global getters and setters
	@Override
	public String get(String varId) {
		try {
			return getters.get(varId).call();
		}
		catch (NullPointerException e) {
			return super.get(varId);
		}
		catch (Exception e) {
			throw new IllegalArgumentException(String.format("Invalid varId %s.", varId));
		}
	}
	@Override
	public void set(String[] data) {
		if (data.length != 9) {
			throw new IllegalArgumentException(String.format("Invalid data length %d, it must be 9.", data.length));
		}
		setOrderID(Integer.parseInt(data[0]));
		setClient(new Person(data[1], data[2], data[3], data[4]));
		setEmployee(new Person(data[5], data[6], data[7], data[8]));
	}

	// Getters and Setters
	public int getOrderID() {
		return orderID;
	}
	private void setOrderID(int orderID) {
		this.orderID = orderID;
		maxOrderID = Math.max(maxOrderID, orderID);
	}
	public Person getClient() {
		return client;
	}
	private void setClient(Person client) {
		this.client = client;
	}
	public Person getEmployee() {
		return employee;
	}
	private void setEmployee(Person employee) {
		this.employee = employee;
	}
	

	@Override
	public String toString() {
		return String.format("%d|(%s)|(%s)|(%s)", orderID, client.toString(), employee.toString(), super.toString());
	}
	public static Order fromString(String string) {
		String[] data = string.split("\\|");
		Order order = new Order();
		order.set(data);
		return order;
	}
}
