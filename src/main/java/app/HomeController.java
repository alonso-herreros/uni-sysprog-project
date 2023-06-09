package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import store.StoreManager;


@Controller
@EnableAutoConfiguration
public class HomeController {

    public static final String DETAILS_PATH = "src\\main\\resources\\static\\details\\";

    public static final String MANAGE_PAGE_PATH = "manage\\";
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
    @GetMapping("manage/{variantName}")
    public String stock(Model model, @PathVariable String variantName) {
        setupManageMenu(model, variantName);
        return "manage";
    }
    // #endregion


    // #region List menus
    @GetMapping("manage/{variantName}/list")
    public String stockList(Model model, @PathVariable String variantName) {
        setupElementList(model, variantName);
        return "list";
    }
    // #endregion


    // #region Setup methods
    public Model setupManageMenu(Model model, String variantName) {
        LinkedHashMap<String, Object> variant = getVariant(MANAGE_PAGE_PATH, variantName);
        model.addAttribute("variant", variant);
        return model;
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