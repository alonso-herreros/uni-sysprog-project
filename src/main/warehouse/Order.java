package warehouse;

public class Order extends ProductList {
	
	static int maxOrderID = 0;
	private int orderID;
	private Person client, employee;

	public Order() {
		this(maxOrderID++, new Person(), new Person());
	}
	public Order(int orderID, Person client, Person employee) {
		setOrderID(orderID);
		setClient(client);
		setEmployee(employee);
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

}
