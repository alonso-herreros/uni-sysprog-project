package dataStructures;

public interface Queue<E> {
    public boolean isEmpty();
    public int size();
    public void enqueue(E element);
    public E dequeue();
    public E front(); 
}
