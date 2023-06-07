package dataStructures;

import java.util.Iterator;

public class LinkedQueue<E> implements Queue<E>, Iterable<E> {

    private class Node<T> {
        private T data;
        private Node<T> next = null;

        public Node(T data) {
            this.data = data;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public LinkedQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void enqueue(E element) {
        Node<E> newNode = new Node<E>(element);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }
        E element = head.data;
        head = head.next;
        size--;
        return element;
    }

    @Override
    public E front() {
        if (isEmpty()) {
            return null;
        }
        return head.data;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return (current != null);
            }

            @Override
            public E next() {
                E element = current.data;
                current = current.next;
                return element;
            }
        };
    }
    
}
