package warehouse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WarehouseElement {
    
    private static Scanner scanner = new Scanner(System.in);
    protected LinkedHashMap<String, Callable<String>> getters = new LinkedHashMap<String, Callable<String>>();
    protected LinkedHashMap<String, Consumer<String>> setters = new LinkedHashMap<String, Consumer<String>>();


    // Constructors
    public WarehouseElement() {
        defineGettersAndSetters();
    } // vv FALL THROUGH vv
    public WarehouseElement(String... data) {
        this();
        set(data);
    }


    // Global getters and setters
    // Must include getters and setters for all fields
	protected void defineGettersAndSetters() {
		defineGetters();
		defineSetters();
	}
    protected abstract void defineGetters();
    protected abstract void defineSetters();

    public String get(String varId) {
		try {
			return getters.get(varId).call();
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(String.format("Invalid varId: %s.", varId));
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error retrieving variable: %s.", varId));
		}
    }

    public void set(String varId, String value) {
        try {
            setters.get(varId).accept(value);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(String.format("Invalid varId: %s.", varId));
        }
    }
    public void set(String data) {
        set(paramsFromString(data));
    } // vv FALL THROUGH vv
    public void set(ArrayList<String> data) {
        set(data.toArray(new String[0]));
    } // vv FALL THROUGH vv
    public void set(String[] data) {
		if (data.length != setters.size()) {
			throw new IllegalArgumentException(String.format("Invalid data length %d, it must be %d.", data.length, setters.size()));
		}
        int i = 0;
        for (String key : setters.keySet()) {
            set(key, data[i++]);
        }
    }


    // Writing
    public String toString() {
		String out = "(";
		for (String key : getters.keySet()) {
            if(setters.containsKey(key)) {
			    out += get(key) + "|";
            }
		}
		return out.substring(0, Math.max(1, out.length()-1)) + ")";
    }
    public void print() {
        System.out.println(toString());
    }
    public void writeToFile(String file) {
        // TODO: Implement writing to file
        throw new UnsupportedOperationException("File I/O not implemented yet.");
    }

    // Reading
    public static WarehouseElement fromString(String string) {
        // MUST BE IMPLEMENTED BY THE CHILD CLASSES
        throw new UnsupportedOperationException("Not implemented by this class.");
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


    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WarehouseElement)) {
            return false;
        }
        if(toString() == o.toString()) {
            return true;
        }
        WarehouseElement other = (WarehouseElement) o;
        for (String key : getters.keySet()) {
            if (!get(key).equals(other.get(key))) {
                return false;
            }
        }
        return true;
    }


    // Utility methods    
    public static ArrayList<String> paramsFromString(String string) {
		ArrayList<String> params = new ArrayList<String>();
        string = string.replaceFirst("^\\(((\\(.*\\)|[^|()]+|\\|)+)\\)", "$1");
		Matcher m = Pattern.compile("\\(.*\\)|[^|()]+").matcher(string);
		while (m.find()) {
            if (!m.group().isEmpty()) {
                params.add(m.group());
            }
		}
        return params;
    }

}