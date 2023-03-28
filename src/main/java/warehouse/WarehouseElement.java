package warehouse;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WarehouseElement {
    // Must include empty constructor and full constructor

    // Must include getters and setters for all fields
    // Global getters and setters
    public abstract String get(String varId);
    public abstract void set(String[] data);

    // Read/write
    public abstract String toString();
    public void print() {
        System.out.println(toString());
    }
    public void writeToFile(String file) {
        // TODO: Implement writing to file

        throw new UnsupportedOperationException("File I/O not implemented yet.");
    }

    // abstract public static WarehouseElement fromString(String string);
    public static WarehouseElement readFromStdio() {
        // TODO: Implement stdio reading
        throw new UnsupportedOperationException("Not implemented yet.");
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