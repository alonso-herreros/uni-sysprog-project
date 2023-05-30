package dataStructures;

public class LBSTree<T extends Comparable<T>, E> implements BSTree<T, E>, Comparable<LBSTree<T,E>> {

    protected T rootKey;
    protected E rootData;
    protected LBSTree<T, E> left;
    protected LBSTree<T, E> right;


    public LBSTree() {
        this(null, null);
    }
    public LBSTree(T rootKey, E rootData) {
        setRoot(rootKey, rootData);
    }


    @Override
    public boolean isEmpty() {
        return (getInfo() == null || getKey() == null);
    }


    @Override
    public E getInfo() {
        return rootData;
    }
    @Override
    public T getKey() {
        return rootKey;
    }
    
    protected E setRoot(LBSTree<T,E> tree) {
        return setRoot(tree.getKey(), tree.getInfo());
    }
    protected E setRoot(T key, E data) {
        E oldData = rootData;
        rootKey = key;
        rootData = data;
        return oldData;
    }

    @Override
    public LBSTree<T,E> getLeft() {
        return left;
    }
    protected void setLeft(LBSTree<T,E> left) {
        this.left = left;
    }

    @Override
    public LBSTree<T,E> getRight() {
        return right;
    }
    protected void setRight(LBSTree<T,E> right) {
        this.right = right;
    }

    @Override
    public void insert(T key, E data) {
        insert(new LBSTree<T,E>(key, data));
    }
    protected void insert(LBSTree<T,E> tree) {
        if (isEmpty()) {
            setRoot(tree);
            return;
        }
        int comparison = compareTo(tree);
        if (comparison < 0) {
            try { getLeft().insert(tree); }
            catch (NullPointerException e) { setLeft(tree); }
        }
        else if (comparison > 0) {
            try { getRight().insert(tree); }
            catch (NullPointerException e) { setRight(tree); }
        }
        else  throw new IllegalArgumentException("There is an insertion conflict with key '" + tree.getKey() + "''");
    }

    public E remove(T key) {
        try {
            return search(key).setRoot(null, null);
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public LBSTree<T,E> search(T key) {
        try {
            int comparison = getKey().compareTo(key);
            if (comparison < 0)  return getLeft().search(key);
            else if (comparison > 0)  return getLeft().search(key);
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

        final String D = getInfo().toString();
        final String L = (getLeft() != null) ? getLeft().toString(mode) : "";
        final String R = (getRight() != null) ? getRight().toString(mode) : "";
        switch (mode) {
            case 0 : return String.join(", ", L, D, R);
            case 1 : return String.join(", ", L, R, D);
            default: return String.join(", ", D, L, R);
        }
    }

    
    @Override
    public int compareTo(LBSTree<T,E> other) {
        return getKey().compareTo(other.getKey());
    }
    
}
