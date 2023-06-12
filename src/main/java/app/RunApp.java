package app;

import org.xml.sax.SAXException;

import store.StoreManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@RequestMapping("/store")
public class RunApp {

    static final String DATA_DIR = "data";

    static StoreManager storeManager = new StoreManager("unnamed", DATA_DIR);

    public static void main(String[] args) throws SAXException, IOException {
      SpringApplication.run(RunApp.class, args);
    }

    @PostMapping("/create")
    public static ResponseEntity<?> createStore(
            @RequestParam(value = "storeName", defaultValue = "Store") String storeName,
            @RequestParam(value = "force", defaultValue = "false") boolean force) {

        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("storeName", storeName);

        if (storeName.equals("")) {
            responseBody.put("message", "Store name cannot be empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }

        File storeDir = new File(DATA_DIR + File.separator + storeName);
        if (storeDir.exists()) {
            if (!force) {
                responseBody.put("message", "Store already exists.");
                responseBody.put("errCode", "STORE_EXISTS");
                return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES).body(responseBody);
            }
            if (!deleteFile(storeDir)) {
                responseBody.put("message", "Store could not be deleted.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
            }
            else if (storeDir.exists()) {
                responseBody.put("message", "Store was deleted, but it was not.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
            }
            return createStore(storeName, false);
        }

        storeManager = new StoreManager(storeName, DATA_DIR);
        responseBody.put("message", "Store created.");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping("/load")
    public static ResponseEntity<?> loadStore(
            @RequestParam(value = "storeName", defaultValue = "Store") String storeName) {

        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("storeName", storeName);

        if (storeName.equals("")) {
            responseBody.put("message", "Store name cannot be empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }

        File storeDir = new File(DATA_DIR + File.separator + storeName);
        if (!storeDir.exists()) {
            responseBody.put("message", "Store does not exist.");
            responseBody.put("errCode", "STORE_NOT_FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

        storeManager = new StoreManager(storeName, DATA_DIR);
        responseBody.put("message", "Store loaded.");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @GetMapping("/getvar")
    public static String getStoreInfo(@RequestParam(value="varID", defaultValue="name") String varId) {
        try {
            return storeManager.get(varId);
        }
        catch (IllegalArgumentException e) {
            return storeManager.getStock().get(varId);
        }
        catch (Exception e) {
            return "null";
        }
    }

    // #region List Objects
    @GetMapping("/stock/list-object")
    public static String getStockList() {
        return storeManager.getStock().getJSON("products");
    }
    @GetMapping("/orders-unprocessed/list-object")
    public static String getUPOrdersList() {
        return storeManager.getOrdersToProcess().toJSON();
    }
    @GetMapping("/orders-processed/list-object")
    public static String getPOrdersList() {
        return storeManager.getOrdersProcessed().toJSON();
    }
    @GetMapping("/clients/list-object")
    public static String getClientsList() {
        return storeManager.getStoreCustomers().toJSON();
    }
    @GetMapping("/employees/list-object")
    public static String getEmployeesList() {
        return storeManager.getStoreEmployees().toJSON();
    }
    @GetMapping("/providers/list-object")
    public static String getProvidersList() {
        return storeManager.getStoreProviders().toJSON();
    }
    // #endregion

    public static boolean deleteFile(File dir) {
        System.out.println(dir);
        if (dir.isDirectory()) {
            for (File child : dir.listFiles()) {
                deleteFile(child);
            }
        }
        return dir.delete();
    }

}
