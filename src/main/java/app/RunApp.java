package app;

import org.xml.sax.SAXException;

import store.StoreManager;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class RunApp {

    public static void main(String[] args) throws SAXException, IOException {
      SpringApplication.run(RunApp.class, args);
    }

    @GetMapping("/helloworld")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello %s!", name);
    }

}
