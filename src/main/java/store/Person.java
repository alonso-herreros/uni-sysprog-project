package store;

import java.util.ArrayList;

public class Person extends WarehouseElement implements Comparable<Object> {
	
	protected int id;
	protected String firstName, lastName, email;
	protected static final String[] def = {"00000001", "Name", "LastName", "email@example.com"};


	// Constructors
	public Person() {
		this(def);
	} // vv FALL THROUGH (3) vv
	public Person(String string) {
        this(paramsFromString(string));
	} // vv FALL THROUGH vv
	public Person(ArrayList<String> params) {
		this(params.toArray(new String[0]));
	} // vv FALL THROUGH vv
	public Person(String... data) {
		super(data);
	}
	public Person(String firstName, String lastName) {
		this(def[0], firstName, lastName, def[3]);
	}


	// Getters and Setters
	protected void defineGetters() {
		getters.put("id", () -> String.format("%08d", getId()));
		getters.put("firstName", () -> getFirstName());
		getters.put("lastName", () -> getLastName());
		getters.put("email", () -> getEmail());
	}
	protected void defineSetters() {
		setters.put("id", (String value) -> setId(Integer.parseInt(value)));
		setters.put("firstName", (String value) -> setFirstName(value));
		setters.put("lastName", (String value) -> setLastName(value));
		setters.put("email", (String value) -> setEmail(value));
	}

	public int getId() { return id; }
	protected void setId(int id) { this.id = id; }
	
	public String getFirstName() { return firstName; }
	protected void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLastName() { return lastName; }
	private void setLastName(String lastName) { this.lastName = lastName; }

	public String getEmail() { return email; }
	private void setEmail(String email) { this.email = email; }


	// Import methods
	public static Person fromString(String string) {
		return new Person(paramsFromString(string));
	}

	public static Person fromStdio() {
		return new Person(stringFromStdio());
	}
	public static Person fromFile(String filepath) {
		return new Person(stringFromFile(filepath));
	}


	// Comparison methods
	@Override
	public int compareTo(Object o) {
		try {
			return Integer.valueOf(id).compareTo(((Person) o).getId());
		}
		catch (ClassCastException e) {
			throw new IllegalArgumentException("Cannot compare a Person with a " + o.getClass().getName());
		}
	}
	@Override
	public boolean equals(Object o) {
		return compareTo(o) == 0;
	}
}
