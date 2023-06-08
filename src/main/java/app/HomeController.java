package app;

import java.util.HashMap;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import store.StoreManager;


@Controller
@EnableAutoConfiguration
public class HomeController {

    protected static final class MenuBuildData {
        public static final HashMap<String, String[]> DEFAULT_TEXTS = new HashMap<String, String[]>() {{
            put("default", new String[] {"Elements", "element", "elements"});
            put("stock", new String[] {"Stock", "stock", "stock"});
            put("orders/unprocessed", new String[] {"Unprocessed Orders", "order", "orders"});
            put("orders/processed", new String[] {"Processed Orders", "order (processed)", "orders (processed)"});
            put("clients", new String[] {"Clients", "client", "clients"});
            put("providers", new String[] {"Providers", "provider", "providers"});
            put("employees", new String[] {"Employees", "employee", "employees"});
        }};

        public final String name;
        public final String title;
        public final String elementName;
        public final String elementsName;

        public MenuBuildData(String name) {
            String[] texts = DEFAULT_TEXTS.get(name);
            this.name = name;
            this.title = texts[0];
            this.elementName = texts[1];
            this.elementsName = texts[2];
        }
    }

    
    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }
    @GetMapping("/home")
    public String home2(Model model) {
        return home(model);
    }


    @GetMapping("/create")
    public String create() { return "create"; }


    @GetMapping("manage/stock")
    public String stock(Model model) {
        setupManageMenu(model, "stock");
        return "manage";
    }
    @GetMapping("manage/stock/list")
    public String stockList(Model model) {
        setupManageMenu(model, "stock");
        return "stockList";
    }

    @GetMapping("manage/orders/unprocessed")
    public String unprocessedOrders(Model model) {
        setupManageMenu(model, "orders/unprocessed");
        return "manage";
    }

    @GetMapping("manage/orders/processed")
    public String processedOrders(Model model) {
        setupManageMenu(model, "orders/processed");
        return "manage";
    }

    @GetMapping("manage/clients")
    public String clients(Model model) {
        setupManageMenu(model, "clients");
        return "manage";
    }

    @GetMapping("manage/providers")
    public String providers(Model model) {
        setupManageMenu(model, "providers");
        return "manage";
    }

    @GetMapping("manage/employees")
    public String employees(Model model) {
        setupManageMenu(model, "employees");
        return "manage";
    }

    
    @GetMapping("print")
    public String print(Model model) {
        return "print";
    }

    @ModelAttribute("store")
    public StoreManager store() {
        return RunApp.storeManager;
    }

    // Utility methods
    public Model setupManageMenu(Model model, String menuName) {
        return setupManageMenu(model, new MenuBuildData(menuName));
    }
    public Model setupManageMenu(Model model, MenuBuildData menu) {
        return model.addAttribute("menu", menu);
    }

}
