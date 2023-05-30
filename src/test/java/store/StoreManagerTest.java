package store;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataStructures.*;


public class StoreManagerTest {

    static final String TEST_DIR = "src\\test\\resources\\store";
    private StoreManager manager;

    @BeforeEach
    public void setUp() {
        manager = new StoreManager("TestStore", TEST_DIR);
    }


    @Test
    public void testConstructorEmpty() {
        StoreManager manager = new StoreManager();
        assertEquals("Name", manager.getName());
        assertEquals(0, manager.getStock().size());
        assertEquals(0, manager.getOrdersToProcess().size());
        assertEquals(0, manager.getOrdersProcessed().size());
        assertTrue(manager.getStoreCustomers().isEmpty());
        assertTrue(manager.getStoreProviders().isEmpty());
        assertTrue(manager.getStoreEmployees().isEmpty());
    }

    @Test
    public void testConstructorNameAndDir() {
        StoreManager manager = new StoreManager("TestStore", TEST_DIR);
        assertEquals("TestStore", manager.getName());
        assertEquals(3, manager.getStock().size());
        assertEquals(1, manager.getOrdersToProcess().size());
        assertEquals(2, manager.getOrdersProcessed().size());
        assertFalse(manager.getStoreCustomers().isEmpty());
        assertTrue(manager.getStoreProviders().isEmpty());
        assertFalse(manager.getStoreEmployees().isEmpty());
    }
    @Test
    public void testCosntructorNameAndAllDirs() {
        String dir = TEST_DIR + "\\" + "TestStore" + "\\";
        StoreManager manager = new StoreManager(
            "TestStore", dir + "stock.txt", dir + "ordersToProcess", dir + "ordersProcessed",
            dir + "storeCustomers.txt", dir + "storeProviders.txt", dir + "storeEmployees.txt");

        assertEquals("TestStore", manager.getName());
        assertEquals(3, manager.getStock().size());
        assertEquals(1, manager.getOrdersToProcess().size());
        assertEquals(2, manager.getOrdersProcessed().size());
        assertFalse(manager.getStoreCustomers().isEmpty());
        assertTrue(manager.getStoreProviders().isEmpty());
        assertFalse(manager.getStoreEmployees().isEmpty());
    }

    @Test
    public void testGetOrdersToProcess() {
        LinkedQueue<Order> orderQueue = manager.getOrdersToProcess();
        assertEquals(1, orderQueue.size());
        Order firstOrder = orderQueue.front();
        assertEquals(1, firstOrder.getOrderID());
        assertEquals(1, firstOrder.size());
        assertEquals(1, firstOrder.get(0).getProductID());
        assertEquals(3, firstOrder.get(0).getNumUnits());

        orderQueue.enqueue(new Order());
        assertEquals(2, orderQueue.size());

        assertEquals(firstOrder, orderQueue.dequeue());
        assertEquals(1, orderQueue.size());
    }

    @Test
    public void testGetOrdersProcessed() {
        LinkedList<Order> orderList = manager.getOrdersProcessed();
        assertEquals(2, orderList.size());

        Order firstOrder = orderList.get(0);
        assertEquals(2, firstOrder.getOrderID());
        assertEquals(1, firstOrder.size());
        assertEquals(2, firstOrder.get(0).getProductID());
        assertEquals("Cap'n Crunch", firstOrder.get(0).getBrand());

        assertEquals(3, orderList.get(1).getOrderID());
    }

    @Test
    public void testGetStock() {
        ProductList stock = manager.getStock();
        assertEquals(3, stock.size());

        StockableProduct firstProduct = stock.get(0);
        assertEquals(1, firstProduct.getProductID());
        assertEquals("Tide", firstProduct.getBrand());
        assertEquals(30, firstProduct.getNumUnits());
        assertEquals(10.0, firstProduct.getCostPerUnit());
        assertEquals(20.0, firstProduct.getPricePerUnit());

        assertEquals(2, stock.get(1).getProductID());
        assertEquals("Cap'n Crunch Original", stock.get(1).getName());
        assertEquals(3, stock.get(2).getProductID());

        assertEquals(800.0, stock.getTotalCost(), 0.01);
        assertEquals(1349.90, stock.getTotalPrice(), 0.01);
    }

    @Test
    public void testGetStockCost() {
        assertEquals(800.0, manager.getStockCost(), 0.01);
        assertEquals(800.0, manager.getStock().getTotalCost(), 0.01);
        assertEquals(0.0, new StoreManager().getStockCost(), 0.01);
    }

    @Test
    public void testGetStockBenefit() {
        assertEquals(549.90, manager.getStock().getTotalBenefit(), 0.01);
        assertEquals(549.90, manager.getStockBenefit(), 0.01);
        assertEquals(0.0, new StoreManager().getStockBenefit(), 0.01);
    }

    @Test
    public void testGetStoreCustomers() {
        SKLBSTree<Integer, Person> customers = manager.getStoreCustomers();
        assertFalse(customers.isEmpty());

        Person firstCustomer = customers.search(1).getInfo();
        assertEquals(1, firstCustomer.getID());
        assertEquals("John", firstCustomer.getFirstName());

        customers.insert(new Person("00000002", "Jane", "Doe", "janedoe@test.com"));
        assertEquals("janedoe@test.com", customers.search(2).getInfo().getEmail());

        assertEquals(null, customers.search(3));
    }

    @Test
    public void testGetStoreDataInfo() {
        String[] info = manager.getStoreDataInfo();
        assertEquals(7, info.length);
        assertEquals("TestStore", info[0]);
        assertEquals(TEST_DIR + "\\TestStore\\stock.txt", info[1]);
        assertEquals(TEST_DIR + "\\TestStore\\ordersToProcess", info[2]);
        assertEquals(TEST_DIR + "\\TestStore\\ordersProcessed", info[3]);
        assertEquals(TEST_DIR + "\\TestStore\\storeCustomers.txt", info[4]);
        assertEquals(TEST_DIR + "\\TestStore\\storeProviders.txt", info[5]);
        assertEquals(TEST_DIR + "\\TestStore\\storeEmployees.txt", info[6]);
    }

    @Test
    public void testSet() {
        manager.set("TestStore2", TEST_DIR);
        assertEquals("TestStore2", manager.getName());
        assertEquals(4, manager.getStock().size());
        assertEquals(2, manager.getOrdersToProcess().size());
        assertEquals(1, manager.getOrdersProcessed().size());
        assertEquals("Jane", manager.getStoreCustomers().search(2).getInfo().getFirstName());
        
        String[] info = manager.getStoreDataInfo();
        assertEquals(TEST_DIR + "\\TestStore2\\stock.txt", info[1]);
        assertEquals(TEST_DIR + "\\TestStore2\\ordersToProcess", info[2]);
        assertEquals(TEST_DIR + "\\TestStore2\\storeCustomers.txt", info[4]);
    }

    // TODO: test file writing

}
