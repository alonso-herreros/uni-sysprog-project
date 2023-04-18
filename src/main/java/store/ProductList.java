package store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ProductList extends WarehouseElement implements Collection<StockableProduct> {

    protected ArrayList<StockableProduct> list = new ArrayList<StockableProduct>();
    protected double totalCost, totalPrice, totalBenefit;


    // Constructors
    public ProductList() {
        this(new StockableProduct[0]);
    } // vv FALL THROUGH (4) vv
    public ProductList(String string) {
        this(paramsFromString(string));
    } // vv FALL THROUGH vv
    public ProductList(String... productStrings) {
        this(new ArrayList<String>(Arrays.asList(productStrings)));
    } // vv FALL THROUGH vv
    public ProductList(ArrayList<String> productStrings) {
        this(productStrings.stream().map((String productString) -> new StockableProduct(productString)).toArray(StockableProduct[]::new));
    } // vv FALL THROUGH vv
    public ProductList(StockableProduct... products) {
        super();
        addAll(Arrays.asList(products));
    }


    public double calculateCost() {
        double cost = 0;
        for (StockableProduct product : list) {
            cost += product.getTotalCost();
        }
        return cost;
    }
    public double calculatePrice() {
        double price = 0;
        for (StockableProduct product : list) {
            price += product.getTotalPrice();
        }
        return price;
    }
    public double calculateBenefit() {
        return calculatePrice() - calculateCost();
    }

    public StockableProduct mostExpensiveProduct() {
        StockableProduct mostExpensive = null;
        for (StockableProduct product : list) {
            if (mostExpensive == null || product.getTotalPrice() > mostExpensive.getTotalPrice()) {
                mostExpensive = product;
            }
        }
        return mostExpensive;
    }
    public StockableProduct cheapestProduct() {
        StockableProduct cheapest = null;
        for (StockableProduct product : list) {
            if (cheapest == null || product.getTotalPrice() < cheapest.getTotalPrice()) {
                cheapest = product;
            }
        }
        return cheapest;
    }


    // Getters and Setters
    @Override
    protected void defineGetters() {
        getters.put("totalCost", () -> Double.toString(getTotalCost()));
        getters.put("totalPrice", () -> Double.toString(getTotalPrice()));
        getters.put("totalBenefit", () -> Double.toString(getTotalBenefit()));
    }
    @Override
    protected void defineSetters() { }
    @Override
    public void set(String... data) {
        throw new UnsupportedOperationException("Can't set ProductList data.");
    }

    @Override
    public String[] getDef() { return new String[0]; }

    public ArrayList<StockableProduct> getList() { return list; }
    public double getTotalCost() { return totalCost; }
    public double getTotalPrice() { return totalPrice; }
    public double getTotalBenefit() { return totalBenefit; }


    // Collection methods
    @Override
    public int size() {
        return list.size();
    }
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }
    @Override
    public Iterator<StockableProduct> iterator() {
        return list.iterator();
    }
    @Override
    public Object[] toArray() {
        return list.toArray();
    }
    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }
    @Override
    public boolean add(StockableProduct e) {
        if (list.add(e)) {
            getters.put(Integer.toString(list.size()-1), () -> e.toString());
            setters.put(Integer.toString(list.size()-1), (String data) -> e.set(data));
            totalCost += e.getTotalCost();
            totalPrice += e.getTotalPrice();
            totalBenefit = totalCost-totalPrice;
            return true;
        }
        return false;
    }
    @Override
    public boolean remove(Object o) {
        final int index = list.indexOf(o);
        if (list.remove(o)) {
            getters.remove(Integer.toString(index));
            setters.remove(Integer.toString(index));
            totalCost -= ((StockableProduct) o).getTotalCost();
            totalPrice -= ((StockableProduct) o).getTotalPrice();
            totalBenefit = totalCost-totalPrice;
            return true;
        }
        return false;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }
    @Override
    public boolean addAll(Collection<? extends StockableProduct> c) {
        boolean out = false;
        for (StockableProduct product : c) {
            out = add(product) || out;
        }
        return out;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean out = false;
        for (Object product : c) {
            out = remove(product) || out;
        }
        return out;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean out = false;
        for (Object product : c) {
            if (!list.contains(product)) {
                out = remove(product) | out;
            }
        }
        return out;
    }
    @Override
    public void clear() {
        list.clear();
        totalCost = totalPrice = totalBenefit = 0;
    }


    public static ProductList readFromString(String string) {
        return new ProductList(string);
    }

    public static ProductList readFromStdio() {
        return new ProductList(stringFromStdio());
    }
    public static ProductList readFromFile(String filepath) {
        return new ProductList(stringFromFile(filepath));
    }
}
