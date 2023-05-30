package dataStructures;

import java.util.function.Function;

// LBSTree of Self-Keying elements
public class SKLBSTree<T extends Comparable<T>, E> extends LBSTree<T,E> {

    protected Function<E, T> keyGetter;


    public SKLBSTree() {
        this(null);
    }
    public SKLBSTree(E rootData) {
        this(rootData, ((E data) -> null));
    }
    public SKLBSTree(E rootData, Function<E,T> keyGetter) {
        super(null, rootData);
        this.keyGetter = keyGetter;
    }
    

    @Override
    public boolean isEmpty() {
        return (rootData == null);
    }

    @Override
    public T getKey() {
        return keyGetter.apply(rootData);
    }

    public void insert(E data) {
        insert(new SKLBSTree<T,E>(data, keyGetter));
    }

}
