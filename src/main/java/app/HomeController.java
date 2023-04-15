package app;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableAutoConfiguration
public class HomeController {
    @GetMapping("/main-menu")
    public String mainMenu() {
        return "index";
    }
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("manage/stock")
    public String stock() {
        return "stock";
    }
    @GetMapping("manage/orders/unprocessed")
    public String unprocessedOrders() {
        return "unprocessedOrders";
    }
    @GetMapping("manage/orders/processed")
    public String processedOrders() {
        return "processedOrders";
    }
    @GetMapping("manage/clients")
    public String clients() {
        return "clients";
    }
    @GetMapping("manage/providers")
    public String providers() {
        return "providers";
    }
    @GetMapping("manage/employees")
    public String employees() {
        return "employees";
    }
}
