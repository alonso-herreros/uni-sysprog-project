package warehouse;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class Product extends WarehouseElement {
	
	private HashMap<String, Callable<String>> getters = new HashMap<String, Callable<String>>() {{
		put("name", () -> getName());
		put("brand", () -> getBrand());
		put("category", () -> Character.toString(getCategory()));
		put("isCountable", () -> Boolean.toString(isCountable()));
		put("measurementUnit", () -> getMeasurementUnit());
	}};
	
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

	// Global getters and Setters
	// TODO: Add get(var) and set(data) methods
	public String[] getVarIds() {
		return getters.keySet().toArray(new String[0]);
	}
	public String get(String varId) {
		try {
			return getters.get(varId).call();
		} catch (Exception e) {
			return "";
		}
	}
	public void set(String[] data) {
		if (data.length != 5) {
			throw new IllegalArgumentException(String.format("Invalid data length %d, it must be 5.", data.length));
		}
		setName(data[0]);
		setBrand(data[1]);
		setCategory(data[2].charAt(0));
		setCountable(Boolean.parseBoolean(data[3]));
		setMeasurementUnit(data[4]);
	}
	// Getters and Setters
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'toString'");
	}
	public static WarehouseElement fromString() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'fromString'");
	}

}
