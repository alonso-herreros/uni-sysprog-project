package warehouse;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class StoreManager extends WarehouseElement {
	
	private String name;
	private ProductList stock;

	private HashMap<String, Callable<String>> getters = new HashMap<String, Callable<String>>() {{
		put("storeName", () -> getName());
		put("stockCost", () -> Float.toString(getStockCost()));
		put("stockBenefit", () -> Float.toString(getStockBenefit()));
	}};


	// Constructors
	public StoreManager() {
		this("", new ProductList());
	}
	public StoreManager(String name) {
		this(name, new ProductList());
	}
	public StoreManager(String name, StockableProduct... products) {
		this(name, new ProductList(products));
	}
	public StoreManager(String name, ProductList stock) {
		setName(name);
		setStock(stock);
	}

	// Global getters and Setters
	// TODO: Add get(var) and set(data) methods
	public String get(String varId) {
		try {
			return getters.get(varId).call();
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format("Invalid varId %s.", varId));
		}
	}
	public void set(String[] data) {
		if (data.length != 3) {
			throw new IllegalArgumentException(String.format("Invalid data length %d, it must be 3.", data.length));
		}
		setName(data[0]);
		setStock(new ProductList(data[1], data[2]));
	}
	// Getters and Setters
	public String getName() {
		return name;
	}
	private void setName(String name) {
		this.name = name;
	}
	public float getStockCost() {
		return getStock().getTotalCost();
	}
	public float getStockBenefit() {
		return getStock().getTotalBenefit();
	}
	public ProductList getStock() {
		return stock;
	}
	private void setStock(ProductList stock) {
		this.stock = stock;
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
