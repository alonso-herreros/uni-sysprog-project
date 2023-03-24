package warehouse;

public class StoreManager {
	
	private String name;
	private ProductList stock;

	public StoreManager() {
		this("", 0, 0, new ProductList());
	}
	public StoreManager(String name, float stockCost, float stockBenefit, ProductList stock) {
		setName(name);
		setStock(stock);
	}

	// Getters and Setters
	// TODO: Add get(var) and set(data) methods
	public String get(String varId) throws IllegalArgumentException {
		switch (varId) {
		case "storeName":
        	return getName();
		case "stockCost":
			return Float.toString(getStockCost());
		case "stockBenefit":
			return Float.toString(getStockBenefit());
		default:
			throw new IllegalArgumentException();
		}
	}
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


	// TODO: Override toString() method
}
