package app;

import warehouse.StoreManager;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@EnableAutoConfiguration
public class HomeController {


  @GetMapping("/")
  public String home(Model model) {
      model.addAttribute("storeName", " ");
      return "index";
  }


  @GetMapping("/create")
  public String create() { return "create"; }


  @GetMapping("manage/stock")
  public String stock(Model model) {
      model.addAttribute("menuName", "Stock");
      model.addAttribute("elementName", "stock");
      model.addAttribute("elementsName", "stock");
      return "manage";
  }

  @GetMapping("manage/orders/unprocessed")
  public String unprocessedOrders(Model model) {
      model.addAttribute("menuName", "Unprocessed Orders");
      model.addAttribute("elementName", "order");
      model.addAttribute("elementsName", "orders");
      return "manage";
  }

  @GetMapping("manage/orders/processed")
  public String processedOrders(Model model) {
      model.addAttribute("menuName", "Processed Orders");
      model.addAttribute("elementName", "order (processed)");
      model.addAttribute("elementsName", "orders (processed)");
      return "manage";
  }

  @GetMapping("manage/clients")
  public String clients(Model model) {
      model.addAttribute("menuName", "Clients");
      model.addAttribute("elementName", "client");
      model.addAttribute("elementsName", "clients");
      return "manage";
  }

  @GetMapping("manage/providers")
  public String providers(Model model) {
      model.addAttribute("menuName", "Providers");
      model.addAttribute("elementName", "provider");
      model.addAttribute("elementsName", "providers");
      return "manage";
  }

  @GetMapping("manage/employees")
  public String employees(Model model) {
      model.addAttribute("menuName", "Employees");
      model.addAttribute("elementName", "employee");
      model.addAttribute("elementsName", "employees");
      return "manage";
  }

  
  @GetMapping("print")
  public String print() { return "print"; }
}
