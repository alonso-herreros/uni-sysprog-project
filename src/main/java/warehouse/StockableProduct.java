package warehouse;

public class StockableProduct extends Product {
	
	private int productID;
	private int numUnits;
	private float costPerUnit, pricePerUnit;
	private float totalCost, totalPrice;

	// Constructors
	public StockableProduct() {
		this(0, new Product(), 0, 0, 0);
	}
	public StockableProduct(int productId, String name, String brand, char category, boolean isCountable, String measurementUnit, int numUnits, float costPerUnit, float pricePerUnit) {
		this(productId, new Product(name, brand, category, isCountable, measurementUnit), numUnits, costPerUnit, pricePerUnit);
	}
	public StockableProduct(int productId, Product product, int numUnits, float costPerUnit, float pricePerUnit) {
		setProductID(productId);
		setNumUnits(numUnits);
		setCostPerUnit(costPerUnit);
		setPricePerUnit(pricePerUnit);

		updateCosts();
	}

	public StockableProduct(String string) {
		// TODO: implement this
	}
	private void updateCosts() {
		totalCost = getNumUnits() * getCostPerUnit();
		totalPrice = getNumUnits() * getPricePerUnit();
	}


	// Getters and Setters
	// TODO: Add get(var) and set(data) methods
	public int getProductID() {
		return productID;
	}
	private void setProductID(int productID) {
		this.productID = productID;
	}
	public int getNumUnits() {
		return numUnits;
	}
	private void setNumUnits(int numUnits) {
		this.numUnits = numUnits;
	}
	public float getCostPerUnit() {
		return costPerUnit;
	}
	private void setCostPerUnit(float costPerUnit) {
		this.costPerUnit = costPerUnit;
	}
	public float getPricePerUnit() {
		return pricePerUnit;
	}
	private void setPricePerUnit(float pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	public float getTotalCost() {
		return totalCost;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'toString'");
	}
	public static StockableProduct fromString(String string) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'fromString'");
	}
}
