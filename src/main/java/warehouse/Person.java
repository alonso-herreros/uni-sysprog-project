package warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class Person extends WarehouseElement {

	private String id, firstName, lastName, email;

	private HashMap<String, Callable<String>> getters = new HashMap<String, Callable<String>>() {{
		put("id", () -> getId());
		put("firstName", () -> getFirstName());
		put("lastName", () -> getLastName());
		put("email", () -> getEmail());
	}};
	
	// Constructors
	public Person() {
		this("", "", "", "");
	}
	public Person(String string) {
		this(paramsFromString(string));
	}
	public Person(ArrayList<String> params) {
		this(params.get(0), params.get(1), params.get(2), params.get(3));
		if (params.size() != 4) {
			throw new IllegalArgumentException("Invalid number of data fields.");
		}
	}
	public Person(String id, String firstName, String lastName, String email) {
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
	}

	// Global getters and Setters
	public void set(String[] data) {
		if (data.length != 4) {
			throw new IllegalArgumentException("Invalid number of data fields.");
		}
		setId(data[0]);
		setFirstName(data[1]);
		setLastName(data[2]);
		setEmail(data[3]);
	}
	public String get(String varId) {
		try {
			return getters.get(varId).call();
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(String.format("Invalid varId: %s.", varId));
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error retrieving variable: %s.", varId));
		}
	}
	// Getters and Setters
	public String getId() {
		return id;
	}
	private void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	private void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	private void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	private void setEmail(String email) {
		this.email = email;
	}

	public static Person fromString(String string) {
		return new Person(paramsFromString(string));
	}
	@Override
	public String toString() {
		return String.join("|", id, firstName, lastName, email);
	}

}
