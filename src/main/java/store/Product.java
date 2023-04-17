package store;

import java.util.ArrayList;

public class Product extends WarehouseElement {

	//protected final String DEFAULT = " | |m|false| ";

	protected String name, brand;
	protected char category;
	protected boolean isCountable;
	protected String measurementUnit;

	protected static final String[] def = {" ", " ", "m", "false", " "};


	// Constructors
	public Product() {
		this(def);
	} // vv FALL THROUGH (3) vv
	public Product(String string) {
        this(paramsFromString(string));
	} // vv FALL THROUGH vv
	public Product(ArrayList<String> params) {
		this(params.toArray(new String[0]));
	} // vv FALL THROUGH (2) vv
	public Product(String name, String brand, char category, boolean isCountable, String measurementUnit) {
		this(name, brand, Character.toString(category), Boolean.toString(isCountable), measurementUnit);
	} // vv FALL THROUGH vv
	public Product(String... data) {
		super(data);
	}


	// Getters and Setters
	@Override
	protected void defineGetters() {
		getters.put("name", () -> getName());
		getters.put("brand", () -> getBrand());
		getters.put("category", () -> Character.toString(getCategory()));
		getters.put("isCountable", () -> Boolean.toString(isCountable()));
		getters.put("measurementUnit", () -> getMeasurementUnit());
	}
	@Override
	protected void defineSetters() {
		setters.put("name", (String value) -> {setName(value);});
		setters.put("brand", (String value) -> setBrand(value));
		setters.put("category", (String value) -> setCategory(value.charAt(0)));
		setters.put("isCountable", (String value) -> setCountable(Boolean.parseBoolean(value)));
		setters.put("measurementUnit", (String value) -> setMeasurementUnit(value));
	}

	public String getName() { return name; }
	protected void setName(String name) { this.name = name; }

	public String getBrand() { return brand; }
	protected void setBrand(String brand) { this.brand = brand; }

	public char getCategory() { return category; }
	protected void setCategory(char category) {
		if ("fsem".indexOf(category)<0) {
			throw new IllegalArgumentException(String.format("Invalid category %c, it must be one of the following: f, s, e, m.", category));
		}
		this.category = category;
	}

	public boolean isCountable() { return isCountable; }
	protected void setCountable(boolean isCountable) { this.isCountable = isCountable; }

	public String getMeasurementUnit() { return measurementUnit; }
	protected void setMeasurementUnit(String measurementUnit) { this.measurementUnit = measurementUnit; }


	public static Product fromString(String string) {
		return new Product(paramsFromString(string));
	}

	public static Product fromStdio() {
		return new Product(stringFromStdio());
	}
	public static Product fromFile(String filepath) {
		return new Product(stringFromFile(filepath));
	}
}
