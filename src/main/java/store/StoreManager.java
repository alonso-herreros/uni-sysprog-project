package store;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import dataStructures.*;

public class StoreManager extends WarehouseElement {

    // Here are some simple class declarations adjusted to the needs of the StoreManager class.
    protected class PersonBSTree extends SKLBSTree<Integer, Person> implements SMContext<Person> {
        @Override public Class<Person> getElementClass() { return Person.class; }
        public PersonBSTree() { this(null); }
        public PersonBSTree(Person person) { super(person, (Person p) -> p.getID()); }

        @Override public Person remove(int ID, int amount) {
            if (amount > 1) throw new UnsupportedOperationException("Can't remove an amount of persons. Use remove(ID, 0).");
            return remove(ID);
        }
        @Override public Person remove() { return notAllowedPrint(); }
        @Override public Person search(int ID) { return super.search(ID).getInfo(); }
        @Override public void modify(int ID, Person person) { notAllowedPrint(); }
        protected Person notAllowedPrint() {
            System.err.println("Modifying people from Store isn't allowed.");
            return null;
        }
    }

    protected class ProviderBSTree extends SKLBSTree<Integer, Provider> implements SMContext<Provider> {
        @Override public Class<Provider> getElementClass() { return Provider.class; }
        public ProviderBSTree() { this(null); }
        public ProviderBSTree(Provider provider) { super(provider, (Provider p) -> p.getVat()); }

        @Override public Provider remove(int Vat, int amount) {
            if (amount > 1) throw new UnsupportedOperationException("Can't remove an amount of providers. Use remove(Vat, 0).");
            return remove(Vat);
        }
        @Override public Provider remove() { return notAllowedPrint(); }
        @Override public Provider search(int Vat) { return super.search(Vat).getInfo(); }
        @Override public void modify(int Vat, Provider person) { notAllowedPrint(); }
        protected Provider notAllowedPrint() {
            System.err.println("Modifying people from Store isn't allowed.");
            return null;
        }
    }

    protected class OrderQueue extends LinkedQueue<Order> implements SMContext<Order> {
        @Override public Class<Order> getElementClass() { return Order.class; }
        public OrderQueue() { super(); }

        @Override public void insert(Order data) { enqueue(data); }
        @Override public Order remove(int orderID, int amount) {
            if ((orderID != front().getOrderID() && orderID != 0) || amount > 1) { // remove(0) is allowed, so is removing the front element.
                throw new UnsupportedOperationException("Can't remove from a queue. Use remove(0, 0).");
            }
            return dequeue();
        }
        @Override public Order remove() { return remove(0,0); }
        @Override public Order search(int identifier) { 
            for (Order e : this) {
                if (e.getOrderID() == identifier)  return e;
            }
            return null;
        }
        @Override public void modify(int identifier, Order data) { throw new UnsupportedOperationException("Can't modify elements in a queue."); }
        
    }

    protected class OrderList extends LinkedList<Order> implements SMContext<Order> {
        @Override public Class<Order> getElementClass() { return Order.class; }
        public OrderList() { super(); }

        @Override public void insert(Order data) { add(data); }
        @Override public Order remove(int orderID, int amount) {
            if (amount > 1) throw new UnsupportedOperationException("Can't remove an amount from an order. Use remove(orderID, 0).");
            int index = indexOf(orderID);
            if (index == -1)  return null;
            return remove(index);
        }
        @Override public Order remove() { return remove(Integer.valueOf(stringFromStdio("Enter ID of the element to remove:")), 0); }
        @Override public Order search(int identifier) { throw new UnsupportedOperationException("Can't search in a list."); }
        @Override public void modify(int identifier, Order data) { throw new UnsupportedOperationException("Can't modify elements in a list."); }
        
        public int indexOf(int orderID) {
            int i = 0;
            for (Order e : this) {
                if (e.getOrderID() == orderID)  return i;
                i++;
            }
            return -1;
        }
    }


    protected ProductList stock;
    protected OrderQueue ordersToProcess;
    protected OrderList ordersProcessed;
    protected PersonBSTree storeCustomers;
    protected ProviderBSTree storeProviders;
    protected PersonBSTree storeEmployees;

    @SuppressWarnings("rawtypes")
    protected final HashMap<String, SMContext> contextMap = new HashMap<String, SMContext>() {{
        put("stock", stock);
        put("ordersToProcess", ordersToProcess);
        put("ordersProcessed", ordersProcessed);
        put("storeCustomers", storeCustomers);
        put("storeProviders", storeProviders);
        put("storeEmployees", storeEmployees);
    }};

    protected String storeDir;
    protected String[] storeDataInfo;

    protected static final String[] DEF = {"Name", ""};
    public static final String[] DEF_STRUCTURE = {
        "name", "stock.txt", "ordersToProcess", "ordersProcessed",
        "storeCustomers.txt", "storeProviders.txt", "storeEmployees.txt"
    };


    // Constructors
    public StoreManager(String data) {
        this(paramsFromString(data));
    } // vv FALL THROUGH vv
    public StoreManager(ArrayList<String> params) {
        this(params.toArray(new String[0]));
    } // vv FALL THROUGH vv
    public StoreManager(String... data) {
        super(data);
    }


    //#region Getters/Setters Readers/Savers
    @Override
    protected void defineGetters() {
        getters.put("name", () -> getName());
        getters.put("stock", () -> getStock().toString());
        getters.put("ordersToProcess", () -> getOrdersToProcess().toString());
        getters.put("ordersProcessed", () -> getOrdersProcessed().toString());
        getters.put("storeCustomers", () -> getStoreCustomers().toString());
        getters.put("storeProviders", () -> getStoreProviders().toString());
        getters.put("storeEmployees", () -> getStoreEmployees().toString());
        getters.put("stockCost", () -> Double.toString(getStockCost()));
        getters.put("stockBenefit", () -> Double.toString(getStockBenefit()));
    }
    @Override
    protected void defineSetters() {
        setters.put("name", (data) -> setName(data));
        setters.put("stock", (filepath) -> setStock(filepath));
        setters.put("ordersToProcess", (dir) -> setOrdersToProcess(dir));
        setters.put("ordersProcessed", (dir) -> setOrdersProcessed(dir));
        setters.put("storeCustomers", (filepath) -> setStoreCustomers(filepath));
        setters.put("storeProviders", (filepath) -> setStoreProviders(filepath));
        setters.put("storeEmployees", (filepath) -> setStoreEmployees(filepath));
    }

    @Override
    public String[] getDef() { return DEF; }

    @Override
    public void set(String ...storeData) {
        storeDataInfo = new String[7];
        if (storeData.length == 0)  storeData = getDef();

        if (storeData.length == 2) { // Name and parent directory
            String name = storeData[0];
            // If the parent directory doesn't already end with the store name, add add a subdirectory to the path.
            String dir = storeData[1] + (storeData[1].endsWith(name)? "" : File.separator + name ) + File.separator;

            storeDir = dir; // It's fine to use assignment here, since the dir setters actually call this method.

            storeData = new String[] {
                name, dir + DEF_STRUCTURE[1], dir + DEF_STRUCTURE[2], dir + DEF_STRUCTURE[3],
                dir + DEF_STRUCTURE[4], dir + DEF_STRUCTURE[5], dir + DEF_STRUCTURE[6]
            };
        }
        super.set(storeData);
    }

    public String getName() { return storeDataInfo[0]; }
    public void setName(String name) { storeDataInfo[0] = name; }

    public String getStoreDir() { return storeDir; }
    public void setStoreDir(String storeDir) { set(getName(), storeDir); }

    /**
     * This method is intended for use after setting/saving an attribute.
     * It will check if the given directory fits in the standard store directory structure,
     * and if it doesn't, the storeDir will be set to null and the method will return false.
     * Then, it will update the storeDataInfo array.
     * @param index Index of the path in the storeDataInfo array. 0 isn't allowed, that's the name.
     * @param path New directory to be saved
     * @throws IllegalArgumentException if the index is out of bounds.
     * @return True if the path fits in the directory structure, false otherwise.
     */
    protected boolean updateStoredPath(int index, String path) {
        if (index < 1 || index > 6) throw new IllegalArgumentException("Index out of bounds.");
        storeDataInfo[index] = path;
        
        String expected = DEF_STRUCTURE[index];
        if (getStoreDir() != null && !new File(path).equals(new File(getStoreDir() + expected))){
            storeDir = null;
            return false;
        }
        return true;
    }

    
    public ProductList getStock() { return stock; }
    private void setStock(ProductList stock) { this.stock = stock; }
    public void setStock(String filepath) {
        updateStoredPath(1, filepath);
        setStock(ProductList.readFromFile(filepath));
    }

    public OrderQueue getOrdersToProcess() { return ordersToProcess; }
    public void setOrdersToProcess(OrderQueue ordersToProcess) { this.ordersToProcess = ordersToProcess; }
    public void setOrdersToProcess(String path) {
        updateStoredPath(2, path);
        setOrdersToProcess(new OrderQueue());
        forFilesInDir(path, (File f) -> getOrdersToProcess().enqueue(Order.readFromFile(f.getAbsolutePath())));
    }

    public OrderList getOrdersProcessed() { return ordersProcessed; }
    public void setOrdersProcessed(OrderList ordersProcessed) { this.ordersProcessed = ordersProcessed; }
    public void setOrdersProcessed(String path) {
        updateStoredPath(3, path);
        setOrdersProcessed(new OrderList());
        forFilesInDir(path, (File f) -> getOrdersProcessed().add(Order.readFromFile(f.getAbsolutePath())) );
    }

    public PersonBSTree getStoreCustomers() { return storeCustomers; }
    public void setStoreCustomers(PersonBSTree storeCustomers) { this.storeCustomers = storeCustomers; }
    public void setStoreCustomers(String filepath) {
        updateStoredPath(4, filepath);
        setStoreCustomers(new PersonBSTree());
        for (String s : stringsFromFile(filepath)) {
            Person customer = Person.readFromString(s);
            getStoreCustomers().insert(customer);
        }
    }

    public ProviderBSTree getStoreProviders() { return storeProviders; }
    public void setStoreProviders(ProviderBSTree storeProviders) { this.storeProviders = storeProviders; }
    public void setStoreProviders(String filepath) {
        updateStoredPath(5, filepath);
        setStoreProviders(new ProviderBSTree());
        for (String s : stringsFromFile(filepath)) {
            Provider provider = Provider.readFromString(s);
            getStoreProviders().insert(provider);
        }
    }

    public PersonBSTree getStoreEmployees() { return storeEmployees; }
    public void setStoreEmployees(PersonBSTree storeEmployees) { this.storeEmployees = storeEmployees; }
    public void setStoreEmployees(String filepath) {
        updateStoredPath(6, filepath);
        setStoreEmployees(new PersonBSTree());
        for (String s : stringsFromFile(filepath)) {
            Person employee = Person.readFromString(s);
            getStoreEmployees().insert(employee);
        }
    }

    public String[] getStoreDataInfo() { return storeDataInfo; }
    public void setStoreDataInfo(String[] storeDataInfo) { set(storeDataInfo); }

    public double getStockCost() { return getStock().getTotalCost(); }

    public double getStockBenefit() { return getStock().getTotalBenefit(); }
    //#endregion  Getters/Setters Readers/Savers


    //#region Processing Orders
    public void processOrders() throws Exception {
        while (!getOrdersToProcess().isEmpty()) {
            processOrder(getOrdersToProcess().dequeue()); // Careful! The order is extracted right here.
        }
    }

    public void processOrder(Order order) throws Exception {
        // Instead of throwing an Exception, if the order is invalid, it is reinserted into the queue.
        // Perhaps it will be valid later, or it can be fixed.
        switch (validateOrder(order)) {
            case -1:
            case -2:
            case -3:
            case -5:
                getOrdersToProcess().enqueue(order);
                return;
        } // Non-negative values mean the order is valid.

        // We may loop over this twice, but the performace should be similar than looping once and performing all operations in the same loop.
        // In fact, if the validation fails, only the checks are performed, and no items are changed.
        for (StockableProduct product : order) { 
            // This remove method is the one that removes a selected amount, unlike remove(product), which removes the exact product.
            getStock().remove(product.getProductID(), product.getNumUnits());
        }
        getOrdersProcessed().add(order); // If we don't do this, the order will be lost forever.

        try { getStoreCustomers().insert(order.getClient()); }
        catch (IllegalArgumentException e) { } // Customer already exists
    }

    public int validateOrder(Order order) {
        if (order == null) return -1;
        else if (getStoreEmployees().search(order.getEmployee().getID())==null)  return -2;
        // Neither Orders or StockableProducts have a provider field. I would assume that this refers to the brand,
        // but the brand is only a string, and the provider is a Provider object. If I have time, I'll change the Product class
        // to have a Provider field, and then make this search for the provider in the store's providers.

        for (StockableProduct product : order) {
            if (!productInStock(product))  return -5;
        }
        return 1;
    }

    public boolean productInStock(StockableProduct product) {
        try {
            return getStock().search(product.getProductID()).getNumUnits() < product.getNumUnits();
        }
        catch (NullPointerException e) {
            return false;
        }
    }
    //#endregion Processing Orders


    public void insert(String context) {
        contextMap.get(context).insert();
    }
    public Object remove(String context) {
        return contextMap.get(context).remove();
    }
    public Object search(String context) {
        return contextMap.get(context).search();
    }
    public void modify(String context) {
        contextMap.get(context).modify();
    }


    public static StoreManager readFromString(String string) {
        return new StoreManager(string);
    }

    public static StoreManager readFromStdio() {
        return new StoreManager(stringFromStdio("Insert store data in one of the following formats:\n" +
                "name|stock.txt|ordersToProcess|ordersProcessed|storeCustomers.txt|storeProviders.txt|storeEmployees.txt\n" +
                "name|parentDir\n"
            ));
    }
    public static StoreManager readFromFile(String filepath) {
        return new StoreManager(stringFromFile(filepath));
    }


    public ArrayList<Person> readPersonsFromFile(String file) {
        ArrayList<Person> persons = new ArrayList<Person>();
        for (String s : stringsFromFile(file)) {
            persons.add(Person.readFromString(s));
        }
        return persons;
    }
    public ArrayList<Provider> readProvidersFromFile(String file) {
        ArrayList<Provider> providers = new ArrayList<Provider>();
        for (String s : stringsFromFile(file)) {
            providers.add(Provider.readFromString(s));
        }
        return providers;
    }
    public ArrayList<Order> readOrdersFromFolder(String folder) {
        ArrayList<Order> orders = new ArrayList<Order>();
        forFilesInDir(folder, (File f) -> orders.add(Order.readFromFile(f.getAbsolutePath())));
        return orders;
    }


    @Override
    public String toString() {
        return String.join("|", storeDataInfo);
    }

    @Override
    public void print() {
        super.print();
        System.out.println("Stock cost: " + getStockCost());
        System.out.println("Stock benefit: " + getStockBenefit());
    }
}
