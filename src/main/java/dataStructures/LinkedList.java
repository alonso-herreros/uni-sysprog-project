package dataStructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LinkedList<E> implements List<E> {

    private class Node<T> {
        private T data;
        private Node<T> next, prev = null;

        public Node(T data) {
            this.data = data;
        }
        public T setData(T data) {
            T old = this.data;
            this.data = data;
            return old;
        }
        public Node<T> linkNext(Node<T> next) {
            this.next = next;
            try { next.prev = this; } catch (NullPointerException e) {} // linking to null
            return this;
        }
        public Node<T> linkPrev(Node<T> prev) {
            this.prev = prev;
            try { prev.next = this; } catch (NullPointerException e) {} // linking to null
            return this;
        }
    }

    private Node<E> first = null;
    private Node<E> last = null;
    private int size = 0;

    // Empty constructor is implicit

    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }

        Node<E> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    // List interface methods

    @Override
    public int size() {
        return size;
    }
    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public E get(int index) {
        return getNode(index).data;
    }
    @Override
    public E set(int index, E e) {
        return getNode(index).setData(e);
    }
    @Override
    public void clear() {
        first = last = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (isEmpty()) {
            first = last = new Node<E>(e);
        } else { // Set last.next, and change last to point to that new node
            last = last.linkNext(new Node<E>(e)).next;
        }
        size++;
        return true;
    }
    @Override
    public void add(int index, E e) {
        if (index == size) {
            add(e);
            return;
        }
        Node<E> displaced = getNode(index);
        new Node<E>(e).linkNext(displaced).linkPrev(displaced.prev);
        if (index == 0) first = first.prev;
        size++;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;

        Iterator<? extends E> it = c.iterator();

        if (isEmpty()) { // I could use add() one by one, but I only need to check this once.
            add(it.next()); // Includes size++
        }
        for (; it.hasNext(); size++) { // Link a new node, then point to that node
            last = last.linkNext(new Node<E>(it.next())).next;
        }
        return true;
    }
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        
        if (index == size) { // addAll(c) is more efficient
            return addAll(c);
        }

        Iterator<? extends E> it = c.iterator();

        Node<E> displaced = getNode(index);
        Node<E> lastAdded;
        for (lastAdded = displaced.prev; it.hasNext(); size++) {
            lastAdded = lastAdded.linkNext(new Node<E>(it.next())).next;
        }
        displaced.linkPrev(lastAdded);
        return true;
    }

    // This method isn't from the list interface, but it's here because it's related to remove()
    private E remove(Node<E> node) { // This method doesn't know if the node is in the list. Beware.
        if (node == null) return null; // We should never get here

        if (size == 1) { // node == first == last
            first = last = null;
        } else {
            try {
                node.prev.linkNext(node.next); // Link the nodes before and after the node to be removed
                if (node == last) last = last.prev; // last.prev is the new last, with next = node.next = last.next = null
            } catch (NullPointerException e) { // node.prev == null <=> node == first
                first = node.next.linkPrev(null); // first.next is the new first, with prev = null
            }
        }
        
        size--;
        return node.data; // We never changed the given node, so we can just return its data here
    }

    @Override
    public boolean remove(Object o) {
        // Iterates through nodes, keeping the reference to each node (not just their data) to be able to remove them
        for (Node<E> node = first; node != null; node = node.next) {
            if (node.data.equals(o)) {
                remove(node);
                return true;
            }
        }
        return false;
    }
    @Override
    public E remove(int index) {
        return remove(getNode(index));
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed = remove(o) || changed;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (Node<E> node = first; node != null; node = node.next) {
            if (!c.contains(node.data)) {
                remove(node);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public int indexOf(Object o) {
        Node<E> node = first;
        for (int i = 0; i<size; i++) {
            if (node.data.equals(o))  return i;
            node = node.next;
        }
        return -1;
    }
    @Override
    public int lastIndexOf(Object o) {
        Node<E> node = last;
        for (int i = size-1; i>=0; i--) {
            if (node.data.equals(o))  return i;
            node = node.prev;
        }
        return -1;
    }
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        LinkedList<E> subList = new LinkedList<E>();
        Node<E> node = getNode(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(node.data);
            node = node.next;
        }
        return subList;
    }

    @Override
    public Object[] toArray() {
        return toArray(new Object[0]);
    }
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) new Object[size];
        }
        Node<E> node = first;
        for (int i = 0; i < size; i++) {
            a[i] = (T) node.data;
            node = node.next;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> node = first;
            @Override
            public boolean hasNext() {
                return node != null;
            }
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new RuntimeException("No more elements.");
                }
                E data = node.data;
                node = node.next;
                return data;
            }
        };
    }
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }
    @Override
    public ListIterator<E> listIterator(int index) {
        LinkedList<E> list = this;
        return new ListIterator<E>() {
            int i = index;
            Node<E> node = getNode(index);
            Node<E> lastReturned = null;
            @Override
            public boolean hasNext() {
                return node != null;
            }
            @Override
            public boolean hasPrevious() {
                return node.prev != null;
            }
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new RuntimeException("No more elements.");
                }
                lastReturned = node;
                node = node.next;
                i++;
                return lastReturned.data;
            }
            @Override
            public E previous() {
                if (!hasPrevious()) {
                    throw new RuntimeException("No more elements.");
                }
                lastReturned = node.prev;
                node = node.prev;
                i--;
                return lastReturned.data;
            }
            @Override
            public int nextIndex() {
                return i;
            }
            @Override
            public int previousIndex() {
                return i-1;
            }
            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException("No element to remove.");
                }
                list.remove(lastReturned);
                lastReturned = null;
            }
            @Override
            public void set(E e) {
                if (lastReturned == null) {
                    throw new IllegalStateException("No element to set.");
                }
                lastReturned.data = e;
                lastReturned = null;
            }
            @Override
            public void add(E e) {
                list.add(i, e);
                i++;
            }
            
        };
    }
    

    // Required by specs
    public void insert(E e) {
        add(e);
    }
    public void insert(E e, Node<E> previous) { // Assumes previous is in the list
        if (previous != null) { // Insert at the beginning
            new Node<E>(e).linkNext(previous.next).linkPrev(previous);
            size++;
        }
    }

    public E extract() {
        try {
            return remove(0);
        } catch (IndexOutOfBoundsException e) { // List is empty
            return null;
        }
    }
    public E extract(Node<E> previous) { // Assumes previous is in the list
        if (previous != null) { // Extract at the beginning
            return remove(previous.next);
        }
        return null;

    }

    public Node<E> searchNode(E data) {
        for (Node<E> node = first; node != null; node = node.next) {
            if (node.data.equals(data)) {
                return node;
            }
        }
        return null;
    }
    public Node<E> searchNode(int index) {
        try {
            return getNode(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    public Node<E> searchLastNode() {
        return last;
    }
    public int search(E data) {
        return indexOf(data);
    }

    public String toString() {
        return String.join(" -> ", toArray(new String[0]));
    }
    public void print() {
        System.out.println(toString());
    }

}
