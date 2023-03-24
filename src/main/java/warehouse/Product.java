package warehouse;

public class Product {
	
	private String name, brand;
	private char category;
	private boolean isCountable;
	private String measurementUnit;

	public Product() {
		this("", "", ' ', false, "");
	}
	public Product(String name, String brand, char category, boolean isCountable, String measurementUnit) {
		setName(name);
		setBrand(brand);
		setCategory(category);
		setCountable(isCountable);
		setMeasurementUnit(measurementUnit);
	}

	// Getters and Setters
	// TODO: Add get(var) and set(data) methods
	public String getName() {
		return name;
	}
	private void setName(String name) {
		this.name = name;
	}
	public String getBrand() {
		return brand;
	}
	private void setBrand(String brand) {
		this.brand = brand;
	}
	public char getCategory() {
		return category;
	}
	private void setCategory(char category) {
		if ("fsem".indexOf(category)<0) {
			throw new IllegalArgumentException(String.format("Invalid category %c, it must be one of the following: f, s, e, m.", category));
		}
		this.category = category;
	}
	public boolean isCountable() {
		return isCountable;
	}
	private void setCountable(boolean isCountable) {
		this.isCountable = isCountable;
	}
	public String getMeasurementUnit() {
		return measurementUnit;
	}
	private void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	// TODO: Override toString() method
	// TODO: Add print() and writeToFile(String file) methods
	// TODO: Add static readFromStdio() and readFromFile(String file) methods
}