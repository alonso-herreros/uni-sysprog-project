package warehouse;

public class Person {

	private String id, firstName, lastName, email;
	
	public Person() {
		this("", "", "", "");
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
	public String get(String varId) throws IllegalArgumentException {
		switch (varId) {
		case "id":
			return getId();
		case "firstName":
			return getFirstName();
		case "lastName":
			return getLastName();
		case "email":
			return getEmail();
		default:
			throw new IllegalArgumentException();
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

	// TODO: Override toString() method
	// TODO: Add print() and writeToFile(String file) methods
	// TODO: Add static readFromStdio() and readFromFile(String file) methods
}
