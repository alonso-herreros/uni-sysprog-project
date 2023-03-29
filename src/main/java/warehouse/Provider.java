package warehouse;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class Provider extends WarehouseElement {
	
	private String vat, name, taxAddress;
	protected Person contactPerson;

	private HashMap<String, Callable<String>> getters = new HashMap<String, Callable<String>>() {{
		put("vat", () -> getVat());
		put("name", () -> getName());
		put("taxAddress", () -> getTaxAddress());
		put("contactPerson", () -> getContactPerson().toString());
	}};


	public Provider() {
		this("", "", "", new Person());
	}
	public Provider(String vat, String name, String taxAddress) {
		this(vat, name, taxAddress, new Person());
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
		if (data.length != 7) {
			throw new IllegalArgumentException(String.format("Invalid data length %d, it must be 7.", data.length));
		}
		setVat(data[0]);
		setName(data[1]);
		setTaxAddress(data[2]);
		setContactPerson(new Person(data[3], data[4], data[5], data[6]));
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
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'toString'");
	}
	public WarehouseElement fromString() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'fromString'");
	}
	
}
