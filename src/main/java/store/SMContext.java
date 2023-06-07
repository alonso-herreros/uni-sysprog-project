package store;

import java.lang.reflect.InvocationTargetException;
import static store.WarehouseElement.stringFromStdio;


public interface SMContext<E extends WarehouseElement> {

    public Class<E> getElementClass();

    /**
     * Inserts the specified element into the data structure. The position is
     * to be decided by the implementation. 
     *
     * @param data Element to be inserted.
     * 
     * @throws IllegalArgumentException if the element already exists and the
     * existing element can't be combined with the new element.
     */
    public void insert(E data);

    default public void insert(String data) {
        insert(fromString(data));
    }
    default public void insert() {
        insert(stringFromStdio("Enter new data: "));
    }


    /**
     * Returns the element with the specified identifier. 
     *
     * @param identifier Identifier of the element to be removed.
     * @param amount Amount of the element to be removed. Use -1 to remove all.
     * 
     * @return The element with the specified identifier.
     * 
     * @throws IllegalArgumentException if the ID can't be found.
     */
    public E remove(int identifier, int amount);

    default public E remove() {
        return remove(
            Integer.valueOf(stringFromStdio("Enter ID of the element to remove:")),
            Integer.valueOf(stringFromStdio("Enter amount to remove:"))
            );
    }


    /**
     * Returns the element with the specified identifier. 
     *
     * @param identifier Identifier of the element to be retrieved.
     * 
     * @return The element with the specified identifier.
     * 
     * @throws IllegalArgumentException if the ID can't be found.
     */
    public E search(int identifier);

    default public E search() {
        return search(Integer.valueOf(stringFromStdio("Enter ID of the element to search:")));
    }


    /**
     * Replaces the element with this identifier by the given element data.
     * 
     * @param identifier Identifier of the element to be changed.
     * @param data Element to be inserted.
     * 
     * @throws IllegalArgumentException if the ID can't be found.
     */
    public void modify(int identifier, E data);
    
    default public void modify(int identifier, String data) {
        modify(identifier, fromString(data));
    }
    default public void modify() {
        int identifier = Integer.valueOf(stringFromStdio("Enter ID of the element to modify:"));
        modify(identifier, stringFromStdio("Object selected: " + search(identifier).toString() + "\nEnter new data: "));
    }


    // This should go inside WarehouseElement, probably
    @SuppressWarnings("unchecked")
    default E fromString(String string) {
        try {
            return (E) getElementClass().getMethod("readFromString", String.class).invoke(null, string);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e); // I really hope we don't get here
        }
    }

}
