package store;

import java.util.ArrayList;

public class StockableProduct extends Product implements Comparable<Object> {

    protected int productID, numUnits;
    protected float costPerUnit, pricePerUnit;
    protected float totalCost, totalPrice;

    protected static final String[] def = {"0", "Product", "Brand", "m", "false", "Units", "0", "0", "0"};


    // Constructors
    public StockableProduct() {
        this(def);
    } // vv FALL THROUGH vv
    public StockableProduct(String string) {
        this(paramsFromString(string));
    } // vv FALL THROUGH vv
    public StockableProduct(ArrayList<String> params) {
        this(params.toArray(new String[0]));
    } // vv FALL THROUGH (2) vv
    public StockableProduct(int productId, String name, String brand, char category, boolean isCountable, String measurementUnit, int numUnits, float costPerUnit, float pricePerUnit) {
        this(Integer.toString(productId), name, brand, Character.toString(category), Boolean.toString(isCountable), measurementUnit, Integer.toString(numUnits), Float.toString(costPerUnit), Float.toString(pricePerUnit));
    } // vv FALL THROUGH vv
    public StockableProduct(String... data) {
        super(data);
        updateCosts();
    }


    private void updateCosts() {
        totalCost = getNumUnits() * getCostPerUnit();
        totalPrice = getNumUnits() * getPricePerUnit();
    }


    // Getters and Setters
    @Override
    public void defineGetters() {
        getters.put("productID", () -> Integer.toString(getProductID()));
        super.defineGetters();
        getters.put("numUnits", () -> Integer.toString(getNumUnits()));
        getters.put("costPerUnit", () -> Float.toString(getCostPerUnit()));
        getters.put("pricePerUnit", () -> Float.toString(getPricePerUnit()));

        getters.put("totalCost", () -> Float.toString(getTotalCost()));
        getters.put("totalPrice", () -> Float.toString(getTotalPrice()));
    }
    @Override
    public void defineSetters() {
        setters.put("productID", (String value) -> setProductID(Integer.parseInt(value)));
        super.defineSetters();
        setters.put("numUnits", (String value) -> setNumUnits(Integer.parseInt(value)));
        setters.put("costPerUnit", (String value) -> setCostPerUnit(Float.parseFloat(value)));
        setters.put("pricePerUnit", (String value) -> setPricePerUnit(Float.parseFloat(value)));
    }

    public int getProductID() { return productID; }
    public void setProductID(int productID) { this.productID = productID; }

    public int getNumUnits() { return numUnits; }
    public void setNumUnits(int numUnits) { this.numUnits = numUnits; }

    public float getCostPerUnit() { return costPerUnit; }
    public void setCostPerUnit(float costPerUnit) { this.costPerUnit = costPerUnit; }

    public float getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(float pricePerUnit) { this.pricePerUnit = pricePerUnit; }

    public float getTotalCost() { return totalCost; }

    public float getTotalPrice() { return totalPrice; }


    // String import methods
    public static StockableProduct fromString(String string) {
        return new StockableProduct(string);
    }

    public static StockableProduct fromStdio() {
        return new StockableProduct(stringFromStdio());
    }
    public static StockableProduct fromFile(String filepath) {
        return new StockableProduct(stringFromFile(filepath));
    }


    // Comparison methods
    @Override
    public int compareTo(Object o) {
        try {
            return Integer.valueOf(productID).compareTo(((StockableProduct) o).getProductID());
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot compare a StockableProduct with a " + o.getClass().getName());
        }
    }
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }
}
