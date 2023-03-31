package warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class Product extends WarehouseElement {
	
	protected HashMap<String, Callable<String>> getters = new HashMap<String, Callable<String>>() {{
		put("name", () -> getName());
		put("brand", () -> getBrand());
		put("category", () -> Character.toString(getCategory()));
		put("isCountable", () -> Boolean.toString(isCountable()));
		put("measurementUnit", () -> getMeasurementUnit());
	}};
	
	protected String name, brand;
	protected char category;
	protected boolean isCountable;
	protected String measurementUnit;


	// Constructors
	public Product() {
		this(" ", " ", 'm', false, " ");
	}
	public Product(String string) {
		this(paramsFromString(string));
	}
	public Product(ArrayList<String> params) {
		this();
		set(params.toArray(new String[0]));
		//this(params.get(0), params.get(1), params.get(2).charAt(0), Boolean.parseBoolean(params.get(3)), params.get(4));
		//if(params.size() != 5) {
		//	throw new IllegalArgumentException(String.format("Invalid data length %d, it must be 5.", params.size()));
		//}
	}
	public Product(String name, String brand, char category, boolean isCountable, String measurementUnit) {
		setName(name);
		setBrand(brand);
		setCategory(category);
		setCountable(isCountable);
		setMeasurementUnit(measurementUnit);
	}


	// Global getters and Setters
	public String get(String varId) {
		try {
			return getters.get(varId).call();
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(String.format("Invalid varId: %s.", varId));
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error retrieving variable: %s.", varId));
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
	protected void setName(String name) {
		this.name = name;
	}
	public String getBrand() {
		return brand;
	}
	protected void setBrand(String brand) {
		this.brand = brand;
	}
	public char getCategory() {
		return category;
	}
	protected void setCategory(char category) {
		if ("fsem".indexOf(category)<0) {
			throw new IllegalArgumentException(String.format("Invalid category %c, it must be one of the following: f, s, e, m.", category));
		}
		this.category = category;
	}
	public boolean isCountable() {
		return isCountable;
	}
	protected void setCountable(boolean isCountable) {
		this.isCountable = isCountable;
	}
	public String getMeasurementUnit() {
		return measurementUnit;
	}
	protected void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}


	@Override
	public String toString() {
		return String.join("|", getName(), getBrand(), Character.toString(getCategory()), Boolean.toString(isCountable()), getMeasurementUnit());
	}
	public static Product fromString(String string) {
		return new Product(paramsFromString(string));
	}

}
