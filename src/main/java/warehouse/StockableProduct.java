package warehouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class StockableProduct extends Product {
	
	protected HashMap<String, Callable<String>> getters = new HashMap<String, Callable<String>>() {{
		putAll(getters);
		put("productID", () -> Integer.toString(getProductID()));
		put("numUnits", () -> Integer.toString(getNumUnits()));
		put("costPerUnit", () -> Float.toString(getCostPerUnit()));
		put("pricePerUnit", () -> Float.toString(getPricePerUnit()));
		put("totalCost", () -> Float.toString(getTotalCost()));
		put("totalPrice", () -> Float.toString(getTotalPrice()));
	}};

	protected int productID, numUnits;
	protected float costPerUnit, pricePerUnit;
	protected float totalCost, totalPrice;


	// Constructors
	public StockableProduct() {
		this(0, " ", " ", 'm', false, " ", 0, 0, 0f);
	}
	public StockableProduct(String string) {
		this(paramsFromString(string));
	}
	public StockableProduct(ArrayList<String> params) {
		this(Integer.parseInt(
			params.get(0)), params.get(1), params.get(2), params.get(3).charAt(0), Boolean.parseBoolean(params.get(4)),
			params.get(5), Integer.parseInt(params.get(6)), Float.parseFloat(params.get(7)), Float.parseFloat(params.get(8)));
		if(params.size() != 9) {
			throw new IllegalArgumentException(String.format("Invalid data length %d, it must be 9.", params.size()));
		}
	}
	public StockableProduct(int productId, String name, String brand, char category, boolean isCountable, String measurementUnit, int numUnits, float costPerUnit, float pricePerUnit) {
		super(name, brand, category, isCountable, measurementUnit);
		setProductID(productId);
		setNumUnits(numUnits);
		setCostPerUnit(costPerUnit);
		setPricePerUnit(pricePerUnit);

		updateCosts();
	}


	private void updateCosts() {
		totalCost = getNumUnits() * getCostPerUnit();
		totalPrice = getNumUnits() * getPricePerUnit();
	}


	// Global getters and setters
	@Override
	public String get(String varId) {
		try {
			return getters.get(varId).call();
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(String.format("Invalid varId: %s.", varId));
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error retrieving variable: %s.", varId));
		}
	}
	@Override
	public void set(String[] data) {
		if (data.length != 9) {
			throw new IllegalArgumentException(String.format("Invalid data length %d, it must be 5.", data.length));
		}
		super.set(Arrays.copyOfRange(data, 1, 6));
		setProductID(Integer.parseInt(data[0]));
		setNumUnits(Integer.parseInt(data[6]));
		setCostPerUnit(Float.parseFloat(data[7]));
		setPricePerUnit(Float.parseFloat(data[8]));
	}

	// Getters and Setters
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
		return String.join("|", Integer.toString(getProductID()), super.toString(), Integer.toString(getNumUnits()), Float.toString(getCostPerUnit()), Float.toString(getPricePerUnit()));
	}
	public static StockableProduct fromString(String string) {
		return new StockableProduct(string);
	}
}
