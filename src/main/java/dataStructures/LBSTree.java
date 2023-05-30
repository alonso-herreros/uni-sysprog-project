package dataStructures;

public class LBSTree<T extends Comparable<T>, E> implements BSTree<T, E>, Comparable<LBSTree<T,E>> {

    private T rootKey;
    private E rootData;
    private LBSTree<T, E> left;
    private LBSTree<T, E> right;


    public LBSTree() {
        rootKey = null;
        rootData = null;
    }
    public LBSTree(T rootKey, E rootData) {
        this.rootKey = rootKey;
        this.rootData = rootData;
    }


    @Override
    public boolean isEmpty() {
        return (rootData == null || rootKey == null);
    }

    @Override
    public E getInfo() {
        return rootData;
    }
    @Override
    public T getKey() {
        return rootKey;
    }
    @Override
    public BSTree<T,E> getLeft() {
        return left;
    }
    @Override
    public BSTree<T,E> getRight() {
        return right;
    }

    @Override
    public void insert(T key, E data) {
        if (isEmpty()) {
            rootKey = key;
            rootData = data;
            return;
        }
        int comparison = key.compareTo(rootKey);
        if (comparison < 0)  left.insert(key, data);
        else if (comparison > 0)  right.insert(key, data);
        else  throw new IllegalArgumentException("This key is already in the tree.");
    }
    @Override
    public BSTree<T,E> search(T key) {
        try {
            int comparison = key.compareTo(rootKey);
            if (comparison < 0)  return left.search(key);
            else if (comparison > 0)  return right.search(key);
            return this;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public String toStringPreOrder() {
        return toString(-1);
    }
    @Override
    public String toStringInOrder() {
        return toString(0);
    }
    @Override
    public String toStringPostOrder() {
        return toString(1);
    }
    @Override
    public String toString() {
        return toString(-1);
    }

    private String toString(int mode) {
        if (isEmpty())  return "";

        final String D = rootData.toString();
        final String L = (left != null) ? left.toString(mode) : "";
        final String R = (right != null) ? right.toString(mode) : "";
        switch (mode) {
            case 0 : return String.join(", ", L, D, R);
            case 1 : return String.join(", ", L, R, D);
            default: return String.join(", ", D, L, R);
        }
    }

    
    @Override
    public int compareTo(LBSTree<T,E> other) {
        return rootKey.compareTo(other.rootKey);
    }
    
}
