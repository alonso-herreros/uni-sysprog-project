package warehouse;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WarehouseElement {

    private static Scanner scanner = new Scanner(System.in);

    // Must include empty constructor and full constructor

    // Must include getters and setters for all fields
    // Global getters and setters
    public abstract String get(String varId);
    public abstract void set(String[] data);

    // Writing
    public abstract String toString();
    public void print() {
        System.out.println(toString());
    }
    public void writeToFile(String file) {
        // TODO: Implement writing to file

        throw new UnsupportedOperationException("File I/O not implemented yet.");
    }

    // Reading
    public static WarehouseElement fromString(String string) {
        throw new UnsupportedOperationException("Not implemented by the class.");
    }
    public static WarehouseElement readFromStdio() {
        System.out.println("Enter full object string representation:");
        String string = scanner.nextLine();
        return fromString(string);
    }
    public static WarehouseElement readFromFile(String file) {
        // TODO: Implement file reading
        throw new UnsupportedOperationException("File I/O not implemented yet.");
    }

    // Utility methods    
    public static ArrayList<String> paramsFromString(String string) {
		ArrayList<String> params = new ArrayList<String>();
		Matcher m = Pattern.compile("\\(.*\\)|[^|()]*").matcher(string);
		while (m.find()) {
			if (!m.group().isEmpty()) {
				params.add(m.group());
			}
		}
        return params;
    }

}