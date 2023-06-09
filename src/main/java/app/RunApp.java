package app;

import org.xml.sax.SAXException;

import store.StoreManager;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


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
    public static ModelAndView createStore(ModelMap model, @RequestParam(value = "storeName", defaultValue = "Store") String name) {
        storeManager = new StoreManager(name, DATA_DIR);
        model.addAttribute("feedback", "newStore");
        return new ModelAndView("redirect:home", model);
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


}
