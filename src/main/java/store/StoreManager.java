package store;

import java.util.ArrayList;

public class StoreManager extends WarehouseElement {

    protected String name;
    protected ProductList stock;

    protected static final String[] def = {"Name", ""};


    // Constructors
    public StoreManager() {
        this(def);
    } // vv FALL THROUGH (3) vv
    public StoreManager(String data) {
        this(paramsFromString(data));
    } // vv FALL THROUGH vv
    public StoreManager(ArrayList<String> params) {
        this(params.toArray(new String[0]));
    } // vv FALL THROUGH vv
    public StoreManager(String... data) {
        super(data);
    }
    public StoreManager(String name, StockableProduct... products) {
        this(name, new ProductList(products));
    }
    public StoreManager(String name, ProductList stock) {
        super(name, stock.toString());
    }


    // Getters and Setters
    protected void defineGetters() {
        getters.put("name", () -> getName());
        getters.put("stock", () -> getStock().toString());
        getters.put("stockCost", () -> Double.toString(getStockCost()));
        getters.put("stockBenefit", () -> Double.toString(getStockBenefit()));
    }
    protected void defineSetters() {
        setters.put("name", (data) -> setName(data));
        setters.put("stock", (data) -> setStock(new ProductList(data)));
    }

    @Override
    public String[] getDef() { return def; }

    public String getName() { return name; }
    private void setName(String name) { this.name = name; }

    public ProductList getStock() { return stock; }
    private void setStock(ProductList stock) { this.stock = stock; }

    public double getStockCost() { return getStock().getTotalCost(); }

    public double getStockBenefit() { return getStock().getTotalBenefit(); }


    public static StoreManager fromString(String string) {
        return new StoreManager(string);
    }

    public static StoreManager fromStdio() {
        return new StoreManager(stringFromStdio());
    }
    public static StoreManager fromFile(String filepath) {
        return new StoreManager(stringFromFile(filepath));
    }
}
