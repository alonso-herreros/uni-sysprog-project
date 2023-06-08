package app;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import store.StoreManager;


@Controller
@EnableAutoConfiguration
public class HomeController {

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
        setupManageMenu(model, "Stock", "stock", "stock");
        return "manage";
    }
    @GetMapping("manage/stock/list")
    public String stockList(Model model) {
        setupManageMenu(model, "Stock", "stock", "stock");
        return "stockList";
    }

    @GetMapping("manage/orders/unprocessed")
    public String unprocessedOrders(Model model) {
        setupManageMenu(model, "Unprocessed Orders", "order", "orders");
        return "manage";
    }

    @GetMapping("manage/orders/processed")
    public String processedOrders(Model model) {
        setupManageMenu(model, "Processed Orders", "order (processed)", "orders (processed)");
        return "manage";
    }

    @GetMapping("manage/clients")
    public String clients(Model model) {
        setupManageMenu(model, "Clients", "client", "clients");
        return "manage";
    }

    @GetMapping("manage/providers")
    public String providers(Model model) {
        setupManageMenu(model, "Providers", "provider", "providers");
        return "manage";
    }

    @GetMapping("manage/employees")
    public String employees(Model model) {
        setupManageMenu(model, "Employees", "employee", "employees");
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
    public Model setupManageMenu(Model model, String menuName, String elementName, String elementsName) {
        model.addAttribute("menuName", elementName);
        model.addAttribute("elementName", elementName);
        model.addAttribute("elementsName", elementsName);
        return model;
    }

}
