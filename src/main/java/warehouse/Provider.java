package warehouse;

import java.util.ArrayList;

public class Provider extends WarehouseElement {
	
	protected String vat, name, taxAddress;
	protected Person contactPerson;

	protected static final String[] def = {"VAT", "Name", "TaxAddress", "00000001A|John|Doe|email@example.com"};


	// Constructors
	public Provider() {
		this(def);
	} // vv FALL THROUGH (3) vv
	public Provider(String string) {
		this(paramsFromString(string));
	} // vv FALL THROUGH vv
	public Provider(ArrayList<String> params) {
		this(params.toArray(new String[0]));
	} // vv FALL THROUGH vv
	public Provider(String... data) {
		super(data);
	}
	public Provider(String vat, String name, String taxAddress) {
		this(vat, name, taxAddress, new Person());
	} // vv FALL THROUGH vv
	public Provider(String vat, String name, String taxAddress, String id, String firstName, String lastName, String email) {
		this(vat, name, taxAddress, new Person(id, firstName, lastName, email));
	} // vv FALL THROUGH vv
	public Provider(String vat, String name, String taxAddress, Person contactPerson) {
		this(vat, name, taxAddress, contactPerson.toString());
	}


	// Getters and Setters
	@Override
	protected void defineGetters() {
		getters.put("vat", () -> getVat());
		getters.put("name", () -> getName());
		getters.put("taxAddress", () -> getTaxAddress());
		getters.put("contactPerson", () -> getContactPerson().toString());
	}
	@Override
	protected void defineSetters() {
		setters.put("vat", (String vat) -> setVat(vat));
		setters.put("name", (String name) -> setName(name));
		setters.put("taxAddress", (String taxAddress) -> setTaxAddress(taxAddress));
		setters.put("contactPerson", (String contactPerson) -> setContactPerson(new Person(contactPerson)));
	}

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
	

	public static Provider fromString(String string) {
		return new Provider(string);
	}
	
	public static Provider fromStdio() {
		return new Provider(stringFromStdio());
	}
	public static Provider fromFile(String filepath) {
		return new Provider(stringFromFile(filepath));
	}
}
