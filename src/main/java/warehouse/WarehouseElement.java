package warehouse;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WarehouseElement {
    // Must include empty constructor and full constructor

    // Must include getters and setters for all fields
    // Global getters and setters
    abstract String get(String varId);
    abstract void set(String[] data);

    // Read/write
    abstract public String toString();
    void print() {
        System.out.println(toString());
    }
    void writeToFile(String file) {
        // TODO: Implement writing to file

        throw new UnsupportedOperationException("File I/O not implemented yet.");
    }

    static WarehouseElement readFromStdio() {
        // TODO: Implement stdio reading
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    static WarehouseElement readFromFile(String file) {
        // TODO: Implement file reading
        throw new UnsupportedOperationException("File I/O not implemented yet.");
    }

    // Utility methods    
    static ArrayList<String> paramsFromString(String string) {
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