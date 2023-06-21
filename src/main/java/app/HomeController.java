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

    public static final String UICONFIG_PATH = "src\\main\\resources\\static\\uiConfig\\";

    public static final String CONTEXTS_SPATH = "contexts\\";
    public static final String LIST_SPATH = "list\\";
    public static final String EDITMENU_SPATH = "editMenu\\";

    
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


    // #region Manage menu
    @GetMapping("manage/{contextName}")
    public String manage(Model model, @PathVariable String contextName) {
        setupManagePage(model, contextName);
        return "manage";
    }

    public Model setupManagePage(Model model, String contextName) {
        final LinkedHashMap<String, Object> context = getContext(contextName);

        model.addAttribute("context", context);
        model.addAttribute("listConfig",
            readJSON(UICONFIG_PATH + LIST_SPATH + (String) context.get("elementType")  + ".json")
        );
        model.addAttribute("editConfig", 
            readJSON(UICONFIG_PATH + EDITMENU_SPATH + (String) context.get("elementType")  + ".json")
        );
        return model;
    }
    // #endregion

    // #region Utility methods
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, Object> getContext(String contextName) {
        final String dir = UICONFIG_PATH + CONTEXTS_SPATH;
        final Object context = readJSON(dir + contextName.replace("/", ".") + ".json");

        if (!(context instanceof LinkedHashMap))
            throw new RuntimeException("Invalid context file: " + contextName  + " in " + dir);

        return (LinkedHashMap<String, Object>) context;
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
