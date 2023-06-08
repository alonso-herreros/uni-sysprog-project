package store;

import java.util.ArrayList;

public class Person extends WarehouseElement implements Comparable<Person> {

    protected int id;
    protected String firstName, lastName, email;
    protected static final String[] DEF = {"00000001", "Name", "LastName", "email@example.com"};


    // Constructors
    public Person() {
        this(DEF);
    } // vv FALL THROUGH (3) vv
    public Person(String string) {
        this(paramsFromString(string));
    } // vv FALL THROUGH vv
    public Person(ArrayList<String> params) {
        this(params.toArray(new String[0]));
    } // vv FALL THROUGH vv
    public Person(String... data) {
        super(data);
    }
    public Person(String firstName, String lastName) {
        this(DEF[0], firstName, lastName, DEF[3]);
    }

    @Override
    public String[] getDef() { return DEF; }


    // Getters and Setters
    @Override
    protected void defineGetters() {
        getters.put("id", () -> String.format("%08d", getID()));
        getters.put("firstName", () -> getFirstName());
        getters.put("lastName", () -> getLastName());
        getters.put("email", () -> getEmail());
    }
    @Override
    protected void defineGettersJSON() {
        gettersJSON.put("id", () -> Integer.toString(getID()) );
        gettersJSON.put("firstName", () -> quote(getFirstName()) );
        gettersJSON.put("lastName", () -> quote(getLastName()) );
        gettersJSON.put("email", () -> quote(getEmail())) ;
    }
    @Override
    protected void defineSetters() {
        setters.put("id", (String value) -> setID(Integer.parseInt(value)));
        setters.put("firstName", (String value) -> setFirstName(value));
        setters.put("lastName", (String value) -> setLastName(value));
        setters.put("email", (String value) -> setEmail(value));
    }

    @Override
    public void set(String... data) {
        if(data.length == 1) {
            try {
                Integer.parseInt(data[0]);
                data = new String[]{data[0], DEF[1], DEF[2], DEF[3]};
            }
            catch (NumberFormatException e) {
                data = paramsFromString(data[0]).toArray(new String[0]);
            }
        }
        super.set(data);
    }

    public int getID() { return id; }
    protected void setID(int id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    protected void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    private void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    private void setEmail(String email) { this.email = email; }


    // Import methods
    public static Person readFromString(String string) {
        return new Person(paramsFromString(string));
    }

    public static Person readFromStdio() {
        return new Person(stringFromStdio());
    }
    public static Person readFromFile(String filepath) {
        return new Person(stringFromFile(filepath));
    }


    // Comparison methods
    @Override
    public int compareTo(Person o) {
        return Integer.valueOf(id).compareTo(o.getID());
    }
    @Override
    public boolean equals(Object o) {
        try {
            return compareTo((Person) o) == 0;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
