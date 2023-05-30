package store;

import java.io.File;
import java.util.ArrayList;
import dataStructures.*;

public class StoreManager extends WarehouseElement {

    protected class PersonBSTree extends SKLBSTree<Integer, Person> {
        public PersonBSTree() { this(null); }
        public PersonBSTree(Person person) { super(person, (Person p) -> p.getID()); }
    }
    protected class ProviderBSTree extends SKLBSTree<Integer, Provider> {
        public ProviderBSTree() { this(null); }
        public ProviderBSTree(Provider provider) { super(provider, (Provider p) -> p.getVat()); }
    }


    protected ProductList stock;
    protected LinkedQueue<Order> ordersToProcess;
    protected LinkedList<Order> ordersProcessed;
    protected PersonBSTree storeCustomers;
    protected ProviderBSTree storeProviders;
    protected PersonBSTree storeEmployees;

    protected String[] storeDataInfo;
    // 0 - name (String)
    // 1 - stock (txt)
    // 2 - ordersToProcess (dir)
    // 3 - ordersProcessed (dir)
    // 4 - storeCustomers (txt)
    // 5 - storeProviders (txt)
    // 6 - storeEmployees (txt)

    protected String dataDir;


    protected static final String[] DEF = {"Name", ""};


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


    //#region Getters and Setters
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
            String dir = storeData[1] + File.separator + name + File.separator;
            storeData = new String[] {
                name, dir + "stock.txt", dir + "ordersToProcess", dir + "ordersProcessed",
                dir + "storeCustomers.txt", dir + "storeProviders.txt", dir + "storeEmployees.txt"
            };
        }
        super.set(storeData);
    }

    public String getName() { return storeDataInfo[0]; }
    public void setName(String name) { storeDataInfo[0] = name; }

    public ProductList getStock() { return stock; }
    private void setStock(ProductList stock) { this.stock = stock; }
    public void setStock(String filepath) {
        storeDataInfo[1] = filepath;
        setStock(ProductList.readFromFile(filepath));
    }

    public LinkedQueue<Order> getOrdersToProcess() { return ordersToProcess; }
    public void setOrdersToProcess(LinkedQueue<Order> ordersToProcess) { this.ordersToProcess = ordersToProcess; }
    public void setOrdersToProcess(String path) {
        storeDataInfo[2] = path;
        setOrdersToProcess(new LinkedQueue<Order>());
        forFilesInDir(path, (File f) -> getOrdersToProcess().enqueue(Order.readFromFile(f.getAbsolutePath())));
    }

    public LinkedList<Order> getOrdersProcessed() { return ordersProcessed; }
    public void setOrdersProcessed(LinkedList<Order> ordersProcessed) { this.ordersProcessed = ordersProcessed; }
    public void setOrdersProcessed(String path) {
        storeDataInfo[3] = path;
        setOrdersProcessed(new LinkedList<Order>());
        forFilesInDir(path, (File f) -> getOrdersProcessed().add(Order.readFromFile(f.getAbsolutePath())) );
    }

    public PersonBSTree getStoreCustomers() { return storeCustomers; }
    public void setStoreCustomers(PersonBSTree storeCustomers) { this.storeCustomers = storeCustomers; }
    public void setStoreCustomers(String filepath) {
        storeDataInfo[4] = filepath;
        setStoreCustomers(new PersonBSTree());
        for (String s : stringsFromFile(filepath)) {
            Person customer = Person.readFromString(s);
            getStoreCustomers().insert(customer);
        }
    }

    public ProviderBSTree getStoreProviders() { return storeProviders; }
    public void setStoreProviders(ProviderBSTree storeProviders) { this.storeProviders = storeProviders; }
    public void setStoreProviders(String filepath) {
        storeDataInfo[5] = filepath;
        setStoreProviders(new ProviderBSTree());
        for (String s : stringsFromFile(filepath)) {
            Provider provider = Provider.readFromString(s);
            getStoreProviders().insert(provider);
        }
    }

    public PersonBSTree getStoreEmployees() { return storeEmployees; }
    public void setStoreEmployees(PersonBSTree storeEmployees) { this.storeEmployees = storeEmployees; }
    public void setStoreEmployees(String filepath) {
        storeDataInfo[6] = filepath;
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
    //#endregion Getters and Setters

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
