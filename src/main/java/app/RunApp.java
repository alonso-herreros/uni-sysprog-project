package app;

import org.xml.sax.SAXException;

import warehouse.StoreManager;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RunApp {

	
	public static void main(String[] args) throws SAXException, IOException {
		SpringApplication.run(RunApp.class, args);
	}

}
