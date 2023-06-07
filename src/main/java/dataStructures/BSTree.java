package dataStructures;

import java.util.Iterator;
import java.util.Stack;


public interface BSTree<T extends Comparable<T>, E> extends Iterable<E> {

    public abstract class TreeIterator<T extends Comparable<T>, E> implements Iterator<E> {
        protected BSTree<T,E> current;
        protected Stack<BSTree<T,E>> stack = new Stack<BSTree<T,E>>();
    
        public TreeIterator(BSTree<T,E> top) {
            stack = new Stack<BSTree<T,E>>() {{ push(top); }};
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty() || current != null;
        }
    }

    
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

    default public boolean hasLeft() { return getLeft() != null; }
    default public boolean hasRight() { return getRight() != null; }
    default public boolean hasChildren() { return hasLeft() || hasRight(); }


    default public Iterator<E> iterator() {
        return iteratorPreOrder();
    }

    default public Iterator<E> iteratorPreOrder() {
        return new TreeIterator<T,E>(BSTree.this) {
            @Override
            public E next() {
                current = stack.pop();
                if (current.hasRight())  stack.push(current.getRight());
                if (current.hasLeft())  stack.push(current.getLeft());
                return current.getInfo();
            }
        };
    }

    default public Iterator<E> iteratorInOrder() {
        return new TreeIterator<T, E>(BSTree.this) {
            @Override
            public E next() {
                current = stack.pop();
                while (current != null) {
                    stack.push(current);
                    current = current.getLeft();
                }
                current = stack.pop();
                stack.push(current.getRight());
                return current.getInfo();
            }
        };
    }

    default public Iterator<E> iteratorPostOrder() {
        return new TreeIterator<T,E>(BSTree.this) {
            @Override
            public E next() {
                current = stack.pop();
                while (current != null) {
                    stack.push(current);
                    if (current.hasChildren())  stack.push(null);
                    if (current.hasRight())  stack.push(current.getRight());
                    current = current.getLeft();
                }
                current = stack.pop();
                return current.getInfo();
            }
        };
    }
    
}
