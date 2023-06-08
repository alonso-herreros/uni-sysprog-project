package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import store.StoreManager;


@Controller
@EnableAutoConfiguration
public class HomeController {

    protected static final class MenuBuildData {
        public static final HashMap<String, String[]> DEFAULT_TEXTS = new HashMap<String, String[]>() {{
            put("default", new String[] {"Elements", "element", "elements"});
            put("stock", new String[] {"Stock", "stock", "stock"});
            put("orders-unprocessed", new String[] {"Unprocessed Orders", "order", "orders"});
            put("orders-processed", new String[] {"Processed Orders", "order (processed)", "orders (processed)"});
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

    public static final String DETAILS_PATH = "src\\main\\resources\\static\\details\\";

    public static final String LIST_PAGE_PATH = "list\\";

    public static final String VARIANTS_PATH = "variants\\";
    public static final String TABLE_COLS_PATH = "tableCols\\";

    
    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }
    @GetMapping("/home")
    public String home2(Model model) {
        return home(model);
    }


    @ModelAttribute("store")
    public StoreManager store() {
        return RunApp.storeManager;
    }


    @GetMapping("/create")
    public String create() {
        return "create";
    }
    
    @GetMapping("print")
    public String print(Model model) {
        return "print";
    }


    // #region Manage menus
    @GetMapping("manage/stock")
    public String stock(Model model) {
        setupManageMenu(model, "stock");
        return "manage";
    }
    @GetMapping("manage/orders-unprocessed")
    public String unprocessedOrders(Model model) {
        setupManageMenu(model, "orders-unprocessed");
        return "manage";
    }
    @GetMapping("manage/orders-processed")
    public String processedOrders(Model model) {
        setupManageMenu(model, "orders-processed");
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
    // #endregion


    // #region List menus
    @GetMapping("manage/stock/list")
    public String stockList(Model model) {
        setupElementList(model, "stock");
        return "list";
    }
    @GetMapping("manage/orders-unprocessed/list")
    public String upOrdersList(Model model) {
        setupElementList(model, "orders-unprocessed");
        return "list";
    }
    @GetMapping("manage/orders-processed/list")
    public String pOrdersList(Model model) {
        setupElementList(model, "orders-processed");
        return "list";
    }
    // #endregion


    // #region Setup methods
    public Model setupManageMenu(Model model, String variantName) {
        return setupManageMenu(model, new MenuBuildData(variantName));
    }
    public Model setupManageMenu(Model model, MenuBuildData menu) {
        return model.addAttribute("menu", menu);
    }

    public Model setupElementList(Model model, String variantName) {
        LinkedHashMap<String, Object> variant = getVariant(LIST_PAGE_PATH, variantName);

        model.addAttribute("variant", variant);
        model.addAttribute("tableCols",
            readJSON(DETAILS_PATH + LIST_PAGE_PATH + TABLE_COLS_PATH + (String) variant.get("tableColsClass")  + ".json")
        );
        return model;
    }
    // #endregion

    // #region Utility methods
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, Object> getVariant(String pagePath, String variantName) {
        final String dir = DETAILS_PATH + pagePath + VARIANTS_PATH;
        final Object variant = readJSON(dir + variantName.replace("/", ".") + ".json");

        if (!(variant instanceof LinkedHashMap))
            throw new RuntimeException("Invalid variant file: " + variantName  + " in " + dir);

        return (LinkedHashMap<String, Object>) variant;
    }

    public static Object readJSON(String jsonFilePath) {
        try {
            String jsonString = new String(Files.readString(Paths.get(jsonFilePath)));
            return new ObjectMapper().readValue(jsonString, new TypeReference<Object>() {});
        } catch (IOException e) {
            return "IOException: " + e.getMessage();
        }
    }
    // #endregion

}
