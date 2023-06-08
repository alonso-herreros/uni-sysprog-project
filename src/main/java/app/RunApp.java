package app;

import org.xml.sax.SAXException;

import store.StoreManager;
import store.WarehouseElement;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@SpringBootApplication
@RestController
public class RunApp {

    static final String DATA_DIR = "data";

    static StoreManager storeManager = new StoreManager("unnamed", DATA_DIR);

    public static void main(String[] args) throws SAXException, IOException {
      SpringApplication.run(RunApp.class, args);
    }

    @PostMapping("/create-store")
    public static ModelAndView createStore(ModelMap model, @RequestParam(value = "storeName", defaultValue = "Store") String name) {
        storeManager = new StoreManager(name, DATA_DIR);
        model.addAttribute("feedback", "newStore");
        return new ModelAndView("redirect:home", model);
    }

    @GetMapping("/store/getvar")
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

    @GetMapping("/manage/stock/list/listObject")
    public static String getStockList() {
        return storeManager.getStock().getJSON("products");
    }
    @GetMapping("/manage/orders/unprocessed/list/listObject")
    public static String getUPOrdersList() {
        return storeManager.getOrdersToProcess().toJSON();
    }

    // #region toJson
    public static String toJson(Object o) {
        if (o instanceof Iterable<?>)  return toJsonList((Iterable<?>) o);
        if (o instanceof WarehouseElement)  return toJsonWE((WarehouseElement) o);
        if (o instanceof String)  return (String) o;
        return "null";
    }
    public static String toJsonList(Iterable<?> list) {
        String out = "[";
        for (Object o : list) {
            try {
                out += toJsonWE((WarehouseElement) o) + ", ";
            }
            catch (ClassCastException e) {
                out += toJson(o) + ", ";
            }
        }
        return out.substring(0, Math.max(1, out.length()-2)) + "]";
    }
    public static String toJsonWE(WarehouseElement o) {
        String out = "{\n";
        for (String key : o.getters.keySet()) {
            String value = toJson(o.get(key));
            if (value == null) value = "null";
            value = value.replace("\\", "\\\\");
            out += "\"" + key + "\": \"" + value + "\",\n";
        }
        return out.substring(0, Math.max(1, out.length()-2)) + "\n}";
    }
    // #endregion

}
