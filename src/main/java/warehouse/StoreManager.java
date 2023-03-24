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

}
