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
	
}
