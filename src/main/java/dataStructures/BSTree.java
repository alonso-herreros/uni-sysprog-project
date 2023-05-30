package dataStructures;

public interface BSTree<T extends Comparable<T>, E> {
    boolean isEmpty();
    E getInfo();
    T getKey();
    BSTree<T,E> getLeft();
    BSTree<T,E> getRight();
    String toStringPreOrder();
    String toStringInOrder();
    String toStringPostOrder();
    String toString(); // pre-order
    void insert(T key, E info);
    BSTree<T,E> search(T key);
}
