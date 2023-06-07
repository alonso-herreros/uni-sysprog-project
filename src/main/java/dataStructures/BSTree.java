package dataStructures;

import java.util.Iterator;
import java.util.Stack;


public interface BSTree<T extends Comparable<T>, E> extends Iterable<E> {

    public abstract class TreeIterator<T extends Comparable<T>, E> implements Iterator<E> {
        protected BSTree<T,E> current;
        protected Stack<BSTree<T,E>> stack = new Stack<BSTree<T,E>>();

        abstract protected void processNext();

        public TreeIterator(BSTree<T,E> top) {
            stack = new Stack<BSTree<T,E>>() {{ push(top); }};
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        @Override
        public E next() {
            if (!hasNext())  throw new java.util.NoSuchElementException();
            current = stack.pop();
            processNext();
            return current.getInfo();
        }
    }

    
    boolean isEmpty();
    E getInfo();
    T getKey();
    BSTree<T,E> getLeft();
    BSTree<T,E> getRight();
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
            protected void processNext() {
                if (current.hasRight())  stack.push(current.getRight());
                if (current.hasLeft())  stack.push(current.getLeft());
            }
        };
    }

    default public Iterator<E> iteratorInOrder() {
        return new TreeIterator<T, E>(BSTree.this) {
            @Override
            protected void processNext() {
                while (current != null) {
                    stack.push(current);
                    current = current.getLeft();
                }
                current = stack.pop();
                stack.push(current.getRight());
            }
        };
    }

    default public Iterator<E> iteratorPostOrder() {
        return new TreeIterator<T,E>(BSTree.this) {
            @Override
            protected void processNext() {
                while (current != null) {
                    stack.push(current);
                    if (current.hasChildren())  stack.push(null);
                    if (current.hasRight())  stack.push(current.getRight());
                    current = current.getLeft();
                }
                current = stack.pop();
            }
        };
    }

    default String toStringPreOrder() {
        return joinedIterable(iteratorPreOrder(), ", ");
    }
    default String toStringInOrder() {
        return joinedIterable(iteratorInOrder(), ", ");
    }
    default String toStringPostOrder() {
        return joinedIterable(iteratorPostOrder(), ", ");
    }

    default String joinedIterable(Iterator<E> iter, String delimiter) {
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext())  sb.append(iter.next().toString()).append(delimiter);
        return sb.substring(0, sb.length() - delimiter.length());
    }

}
