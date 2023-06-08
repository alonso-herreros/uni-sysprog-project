package store;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataStructures.JSONAble;


public abstract class WarehouseElement implements JSONAble {
    
    private static Scanner scanner = new Scanner(System.in);
    public LinkedHashMap<String, Callable<String>> getters = new LinkedHashMap<String, Callable<String>>();
    public LinkedHashMap<String, Callable<String>> gettersJSON = new LinkedHashMap<String, Callable<String>>();
    public LinkedHashMap<String, Consumer<String>> setters = new LinkedHashMap<String, Consumer<String>>();


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
        gettersJSON = new LinkedHashMap<String, Callable<String>>();
        getters = new LinkedHashMap<String, Callable<String>>();
        setters = new LinkedHashMap<String, Consumer<String>>();
        defineGetters();
        defineGettersJSON();
        defineSetters();
    }
    protected abstract void defineGetters();
    protected abstract void defineGettersJSON();
    protected abstract void defineSetters();
    public abstract String[] getDef();

    // Generalized get(var), set(var, value), and set(data)
    public String get(String varID) {
        try {
            return getters.get(varID).call();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(String.format("Invalid varID: %s.", varID));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error retrieving variable: %s.", varID));
        }
    }
    public String getJSON(String varID) {
        try {
            return gettersJSON.get(varID).call();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(String.format("Invalid varID: %s.", varID));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error retrieving variable: %s.", varID));
        }
    }

    public void setVar(String varID, String value) {
        try {
            setters.get(varID).accept(value);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(String.format("Invalid varID: %s.", varID));
        }
    }
    public void set(String data) {
        set(paramsFromString(data));
    } // vv FALL THROUGH vv
    public void set(ArrayList<String> data) {
        set(data.toArray(new String[0]));
    } // vv FALL THROUGH vv
    public void set(String... data) {
        if(data.length == 0) {
            data = getDef();
        }
        if (data.length != setters.size()) {
            throw new IllegalArgumentException(String.format("Invalid data length %d, it must be %d.", data.length, setters.size()));
        }
        int i = 0;
        for (String key : setters.keySet()) {
            setVar(key, data[i++]);
        }
    }


    // #region Writing
    public String toString() { // Generalized
        String out = "(";
        for (String key : getters.keySet()) {
            if(setters.containsKey(key)) {
                out += get(key) + "|";
            }
        }
        return out.substring(0, Math.max(1, out.length()-1)) + ")";
    }
    @Override
    public String toJSON() { // Generalized
        String out = "{\n";
        for (String key : gettersJSON.keySet()) {
            String value = getJSON(key);
            value = value.replace("\\", "\\\\");
            out += "\"" + key + "\": " + value + ",\n";
        }
        return out.substring(0, Math.max(1, out.length()-2)) + "\n}";
    }
    public void print() { // Generalized
        System.out.println(toString());
    }
    public void writeToFile(String filepath) { // Generalized
        stringToFile(filepath, toString());
    }
    // #endregion
    // Read methods: to be implemented by subclasses, using utility methods (can't generalize these static methods)


    // #region Utility methods
    public static ArrayList<String> paramsFromString(String string) {
        if(string == null || string.isEmpty()) {
            return new ArrayList<String>();
        }
        ArrayList<String> params = new ArrayList<String>();
        string = string.replaceFirst("(?<!\\|)\\(((\\(.*\\)|[^|()]+|\\|)+)\\)(?!\\|)", "$1");
        Matcher m = Pattern.compile("(?<!\\()\\(.*?\\)(?!\\))|[^|()]+").matcher(string);
        while (m.find()) {
            if (!m.group().isEmpty()) {
                params.add(m.group());
            }
        }
        return params;
    }
    
    public static void forFilesInDir(String path, Consumer<File> consumer) {
        File dir = new File(path);
        if (!dir.exists())  return;
        if (!dir.isDirectory())  throw new IllegalArgumentException("Path must be a directory");

        for (File f : dir.listFiles()) {
            consumer.accept(f);
        }
    }

    public static String stringFromStdio() {
        return stringFromStdio("Enter full object string representation:");
    }
    public static String stringFromStdio(String prompt) {
        System.out.println(prompt);
        String string = scanner.nextLine();
        return string;
    }
    
    public static String stringFromFile(String filepath) {
        try {
            return stringsFromFile(filepath).get(0);
        }
        catch (IndexOutOfBoundsException e) {
            return "";
        }
    }
    public static ArrayList<String> stringsFromFile(String filepath) {
        ArrayList<String> strings = new ArrayList<String>();
        try {
            Scanner reader = new Scanner(new File(filepath));
            while (reader.hasNextLine()) {
                strings.add(reader.nextLine());
            }
            reader.close();
        }
        catch (FileNotFoundException e) { } // Will return the empty ArrayList
        catch (Exception e) { // Something went very wrong
            throw new RuntimeException(String.format("Exception while reading from '%s': %s", filepath, e.getMessage()));
        }
        return strings;
    }
    public void stringToFile(String filepath, String string) {
        File file = new File(filepath);
        if (! file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(string);
            writer.close();
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("Exception while writing to '%s': %s", filepath, e.getMessage()));
        }
    }


    public static <E extends JSONAble> String JsonListFromIterable(Iterable<E> iterable) {
        String out = "[\n";
        for (E element : iterable) {
            out += element.toJSON() + ",\n";
        }
        return out.substring(0, Math.max(1, out.length()-2)) + "\n]";
    }
    public static <E extends JSONAble> String JsonDictFromSKIterable(Iterable<E> iterable, Function<E, String> keyGetter) {
        String out = "{\n";
        for (E element : iterable) {
            out += quote(keyGetter.apply(element)) + ": " + element.toJSON() + ",\n";
        }
        return out.substring(0, Math.max(1, out.length()-2)) + "\n}";
    }


    public static String quote(String string) {
        return enclose(string, "\"");
    }
    public static String enclose(String string, String ends) {
        return enclose(string, ends, ends);
    }
    public static String enclose(String string, String open, String close) {
        return open + string + close;
    }
    // #endregion


    // Default equals
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
        
    
}