package warehouse;

public class Provider {
	
	private String vat, name, taxAddress;
	protected Person contactPerson;

	public Provider() {
		this("", "", "", new Person());
	}
	public Provider(String vat, String name, String taxAddress, Person contactPerson) {
		setVat(vat);
		setName(name);
		setTaxAddress(taxAddress);
		setContactPerson(contactPerson);
	}

	// Getters and Setters
	// TODO: Add get(var) and set(data) methods
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

	// TODO: Override toString() method
	// TODO: Add print() and writeToFile(String file) methods
	// TODO: Add static readFromStdio() and readFromFile(String file) methods
}