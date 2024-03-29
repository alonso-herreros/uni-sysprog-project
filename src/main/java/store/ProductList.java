package store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ProductList extends WarehouseElement implements List<StockableProduct>, SMContext<StockableProduct> {

    @Override public Class<StockableProduct> getElementClass() { return StockableProduct.class; }

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
        getters.put("totalCost", () -> String.format("%.2f", getTotalCost()));
        getters.put("totalPrice", () -> String.format("%.2f", getTotalPrice()));
        getters.put("totalBenefit", () -> String.format("%.2f", getTotalBenefit()));
    }
    @Override
    protected void defineGettersJSON() {
        gettersJSON.put("totalCost", () -> String.format("%.2f", getTotalCost()));
        gettersJSON.put("totalPrice", () -> String.format("%.2f", getTotalPrice()));
        gettersJSON.put("totalBenefit", () -> String.format("%.2f", getTotalBenefit()));
        gettersJSON.put("products", () -> jsonListFromIterable(list) );
    }
    @Override
    protected void defineSetters() { }
    @Override
    public void set(String... data) {
        clear();
        String[] info = Arrays.copyOfRange(data, 0, setters.keySet().size());
        String[] productStrings = Arrays.copyOfRange(data, setters.keySet().size(), data.length);
        super.set(info);
        addAll(Arrays.asList(productStrings).stream().map((String productString) -> new StockableProduct(productString)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
    }

    @Override
    public String[] getDef() { return new String[0]; }

    public ArrayList<StockableProduct> getList() { return list; }
    public double getTotalCost() { return totalCost; }
    public double getTotalPrice() { return totalPrice; }
    public double getTotalBenefit() { return totalBenefit; }

    private void addCostAndPrice(StockableProduct product) {
        addCostAndPrice(product.getTotalCost(), product.getTotalPrice(), 1);
    }
    private void removeCostAndPrice(StockableProduct product) {
        addCostAndPrice(product.getTotalCost(), product.getTotalPrice(), -1);
    }
    private void addCostAndPrice(double cost, double price, int factor) {
        totalCost += factor * cost;
        totalPrice += factor * price;
        totalBenefit = totalPrice-totalCost;
    }


    // List methods
    @Override
    public StockableProduct get(int index) {
        return list.get(index);
    }
    @Override
    public StockableProduct set(int index, StockableProduct element) {
        StockableProduct out = remove(index);
        add(index, element);
        return out;
    }
    @Override
    public int size() {
        return list.size();
    }
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
    @Override
    public boolean add(StockableProduct e) {
        int indexFound = indexOf(e.getProductID());
        if (indexFound != -1) {
            StockableProduct p = remove(indexFound);
            p.setNumUnits(p.getNumUnits() + e.getNumUnits());
            add(indexFound, p);
        }
        else {
            add(size(), e);
        }
        return true;
    } // vv FALL THROUGH vv
    @Override
    public void add(int index, StockableProduct e) {
        int indexFound = indexOf(e.getProductID());
        if (indexFound > -1) {
            throw new IllegalArgumentException("Product with ID " + e.getProductID() + " already exists, found at index " + indexFound + ".");
        }
        list.add(index, e);
        getters.put(Integer.toString(list.size()-1), () -> e.toString());
        setters.put(Integer.toString(list.size()-1), (String data) -> e.set(data));
        addCostAndPrice(e);
    }
    @Override
    public boolean addAll(Collection<? extends StockableProduct> c) {
        return addAll(size(), c);
    } // vv FALL THROUGH vv
    @Override
    public boolean addAll(int index, Collection<? extends StockableProduct> c) {
        boolean out = false;
        int i = index;
        for (StockableProduct product : c) {
            add(i++, product);
            out = true;
        }
        return out;
    }
    @Override
    public boolean remove(Object o) {
        try {
            remove(indexOf(o));
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    } // vv FALL THROUGH vv
    @Override
    public StockableProduct remove(int index) {
        StockableProduct out = list.remove(index);
        removeCostAndPrice(out);
        getters.remove(Integer.toString(index));
        setters.remove(Integer.toString(index));
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
    public boolean contains(Object o) {
        return list.contains(o);
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
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
        defineGettersAndSetters();
        totalCost = totalPrice = totalBenefit = 0;
    }
    public int indexOf(int productID) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProductID() == productID) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }
    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
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
    public ListIterator<StockableProduct> listIterator() {
        return list.listIterator();
    }
    @Override
    public ListIterator<StockableProduct> listIterator(int index) {
        return list.listIterator(index);
    }
    @Override
    public List<StockableProduct> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    //#region StoreManagerContext
    @Override
    public void insert(StockableProduct product) {
        add(product);
    } // vv SAME vv
    @Override
    public StockableProduct remove(int productID, int numUnits) {
        int index = indexOf(productID);
        if (index == -1)  return null;
        StockableProduct out = remove(index);

        if (out.getNumUnits() < numUnits) {
            throw new IllegalArgumentException("Cannot remove " + numUnits + " units of product with ID " + productID + " because there are only " + out.getNumUnits() + " units.");
        }
        else if (out.getNumUnits() > numUnits){
            out.setNumUnits(out.getNumUnits() - numUnits);
            add(index, out);
        }
        return out;
    }
    @Override
    public StockableProduct search(int productID) {
        int index = indexOf(productID);
        if (index == -1) {
            return null;
        }
        return get(index);
    }
    @Override
    public void modify(int productID, StockableProduct product) {
        try {
            set(indexOf(productID), product);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Product with ID " + productID + " not found.");
        }
    }
    //#endregion


    // IO
    public static ProductList readFromString(String string) {
        return new ProductList(string);
    }

    public static ProductList readFromStdio() {
        return new ProductList(stringFromStdio());
    }
    public static ProductList readFromFile(String filepath) {
        return new ProductList(stringsFromFile(filepath));
    }

    @Override
    public void writeToFile(String filepath) {
        String string = String.join("\n", list.stream().map((StockableProduct product) -> product.toString()).toArray(String[]::new));
        stringToFile(filepath, string);
    }
}
