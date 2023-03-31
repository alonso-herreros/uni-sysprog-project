package warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class Provider extends WarehouseElement {
	
	private HashMap<String, Callable<String>> getters = new HashMap<String, Callable<String>>() {{
		put("vat", () -> getVat());
		put("name", () -> getName());
		put("taxAddress", () -> getTaxAddress());
		put("contactPerson", () -> getContactPerson().toString());
	}};

	protected String vat, name, taxAddress;
	protected Person contactPerson;


	// Constructors
	public Provider() {
		this("", "", "", new Person());
	}
	public Provider(String string) {
		this(paramsFromString(string));
	}
	public Provider(ArrayList<String> params) {
		this(params.get(0), params.get(1), params.get(2), params.get(3));
		if(params.size() != 4) {
			throw new IllegalArgumentException(String.format("Invalid data length %d, it must be 4.", params.size()));
		}
	}
	public Provider(String vat, String name, String taxAddress) {
		this(vat, name, taxAddress, new Person());
	}
	public Provider(String vat, String name, String taxAddress, String contactPersonString) {
		this(vat, name, taxAddress, new Person(contactPersonString));
	}
	public Provider(String vat, String name, String taxAddress, String id, String firstName, String lastName, String email) {
		this(vat, name, taxAddress, new Person(id, firstName, lastName, email));
	}
	public Provider(String vat, String name, String taxAddress, Person contactPerson) {
		setVat(vat);
		setName(name);
		setTaxAddress(taxAddress);
		setContactPerson(contactPerson);
	}


	// Global getters and Setters
	public String get(String varId) {
		try {
			return getters.get(varId).call();
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format("Invalid varId %s.", varId));
		}
	}
	public void set(String[] data) {
		Person person;
		if (data.length == 7) {
			person = new Person(data[3], data[4], data[5], data[6]);
		}
		else if (data.length == 4) {
			person = new Person(data[3]);
		}
		else {
			throw new IllegalArgumentException(String.format("Invalid data length %d, it must be 7 or 4.", data.length));
		}
		setVat(data[0]);
		setName(data[1]);
		setTaxAddress(data[2]);
		setContactPerson(person);
	}
	// Getters and Setters
	public String getVat() {
		return vat;
	}
	private void setVat(String vat) {
		this.vat = vat;
	}
	public String getName() {
		return name;
	}
	private void setName(String name) {
		this.name = name;
	}
	public String getTaxAddress() {
		return taxAddress;
	}
	private void setTaxAddress(String taxAddress) {
		this.taxAddress = taxAddress;
	}
	public Person getContactPerson() {
		return contactPerson;
	}
	private void setContactPerson(Person contactPerson) {
		this.contactPerson = contactPerson;
	}
	
	@Override
	public String toString() {
		return String.join("|", vat, name, taxAddress, "("+contactPerson.toString()+")");
	}
	public static Provider fromString(String string) {
		return new Provider(string);
	}
	
}
