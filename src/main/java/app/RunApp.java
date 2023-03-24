package app;

import java.io.IOException;

import org.xml.sax.SAXException;

import app.gui.UserInterface;
import warehouse.StoreManager;


public class RunApp {

	static UserInterface ui;

	public static void main(String[] args) throws SAXException, IOException {
		ui = new UserInterface();

		ui.start();

	}

}
