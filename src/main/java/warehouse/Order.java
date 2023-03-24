package warehouse;

public class Order extends ProductList {
	
	static int maxOrderID = 0;
	private int orderID;
	private Person client, employee;

	public Order() {
		this(new Person(), new Person());
	}
	public Order(Person client, Person employee) {
		this(maxOrderID++, client, employee);
	}
	public Order(int orderID, Person client, Person employee) {
		setOrderID(orderID);
		setClient(client);
		setEmployee(employee);
	}

	// Global getters and setters
	public void set(String[] data) {
		if (data.length != 9) {
			throw new IllegalArgumentException("Invalid number of data fields.");
		}
		setOrderID(Integer.parseInt(data[0]));
		setClient(new Person(data[1], data[2], data[3], data[4]));
		setEmployee(new Person(data[5], data[6], data[7], data[8]));
	}
	public String get(String varId) throws IllegalArgumentException {
		switch (varId) {
		case "orderID":
			return Integer.toString(getOrderID());
		case "client":
			return getClient().toString();
		case "employee":
			return getEmployee().toString();
		default:
			throw new IllegalArgumentException();
		}
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
	
	// TODO: Override toString() method
	// TODO: Add print() and writeToFile(String file) methods
	// TODO: Add static readFromStdio() and readFromFile(String file) methods
}
