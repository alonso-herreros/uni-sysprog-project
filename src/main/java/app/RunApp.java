package app;

import org.xml.sax.SAXException;

import warehouse.StoreManager;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class RunApp {

    static StoreManager storeManager = new StoreManager("unnamed", "");

    public static void main(String[] args) throws SAXException, IOException {
      SpringApplication.run(RunApp.class, args);
    }

    @PostMapping("/create-store")
    public static String createStore(@RequestParam(value = "name", defaultValue = "Store") String name) {
        storeManager = new StoreManager(name, "");
        return "Store created";
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

}
