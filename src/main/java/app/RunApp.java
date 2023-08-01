package app;

import org.xml.sax.SAXException;

import store.SMContext;
import store.StoreManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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


    // #region Create @ /store/create 
    @PostMapping(
        value="/create",
        consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
        produces=MediaType.APPLICATION_JSON_VALUE)
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
    // #endregion

    // #region Load @ /store/load 
    @PostMapping(
        value="/load",
        consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
        produces=MediaType.APPLICATION_JSON_VALUE)
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
    // #endregion

    // #region Save @ /store/save 
    @PostMapping(
        value="/save",
        produces=MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<?> saveStore() {

        Map<String, Object> responseBody = new HashMap<String, Object>();

        if (storeManager.getName().equals("") || storeManager.getName().equals("unnamed")) {
            responseBody.put("message", "Store must have a name.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
        responseBody.put("storeName", storeManager.getName());

        storeManager.saveStore();
        if (!checkSavedStore()) {
            responseBody.put("message", "Store could not be saved.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        responseBody.put("message", "Store saved.");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
    // #endregion

    // #region (Deprecated) Get variable @ /store/getvar
    @GetMapping(
        value="/getvar",
        produces=MediaType.TEXT_PLAIN_VALUE)
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
    // #endregion

    // #region Get list of elements @ /store/{contextName}/list
    @GetMapping(
        value="/{contextName}/list-object",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public static String getStockList(@PathVariable String contextName) {
        switch (contextName) {
        case "stock":
            return storeManager.getStock().getJSON("products");
        case "orders-unprocessed":
            return storeManager.getOrdersToProcess().toJSON();
        case "orders-processed":
            return storeManager.getOrdersProcessed().toJSON();
        case "clients":
            return storeManager.getStoreCustomers().toJSON();
        case "employees":
            return storeManager.getStoreEmployees().toJSON();
        case "providers":
            return storeManager.getStoreProviders().toJSON();
        }
        return "null";
    }
    //#endregion

    // #region Edit element @ /store/{contextName}/edit/{elementID} 
    @PostMapping(
        value = "/{contextName}/edit/{elementID}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<Map<String,Object>> editElement(
            @RequestBody List<Map<String,Object>> changes,
            @PathVariable String contextName,
            @PathVariable int elementID) {

        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("contextName", contextName);
        responseBody.put("elementID", elementID);

        List<Map<String, Object>> changeList = applyChangesToElement(contextName, elementID, changes);
        responseBody.put("changes", changeList);

        responseBody.put("message", "Element edited.");

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    private static List<Map<String, Object>> applyChangesToElement(
            String contextName, int elementID, List<Map<String, Object>> changes) {

        List<Map<String, Object>> changeList = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> change : changes) {
            String mode = (String) change.get("mode");
            switch (mode) {
                case "elementAttribute":
                    try { changeList.add(setElementAttribute(contextName, elementID, change)); }
                    catch (IllegalArgumentException e) { addErrorStatus(change, e.getMessage()); }
                    break;
                default:
                    addErrorStatus(change, "Invalid set mode: " + mode);
            }
        }
        return changeList;
    }

    private static Map<String, Object> setElementAttribute(
            String contextName, int elementID, Map<String, Object> changeMap) {
        SMContext<?> context = storeManager.getContext(contextName);
        String attributeID = (String) changeMap.get("attribute");
        String value = (String) changeMap.get("value");

        context.search(elementID).setVar(attributeID, value);

        changeMap.put("status", "set");
        changeMap.put("newValue", context.search(elementID).get(attributeID));

        return changeMap;
    }

    private static Map<String, Object> addErrorStatus(Map<String, Object> changeMap, String message) {
        changeMap.put("status", "error");
        changeMap.put("message", message);
        return changeMap;
    }
    // #endregion


    public static boolean deleteFile(File dir) {
        if (dir.isDirectory()) {
            for (File child : dir.listFiles()) {
                deleteFile(child);
            }
        }
        return dir.delete();
    }

    public static boolean checkSavedStore() {
        File storeDir = new File(DATA_DIR + File.separator + storeManager.getName());
        if (!storeDir.exists())  return false;
        StoreManager savedStoreManager = new StoreManager(storeManager.getName(), DATA_DIR);
        return storeManager.equals(savedStoreManager);
    }

}
