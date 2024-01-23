package tp_ed.structures;

import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.structures.interfaces.QueueADT;

/**
 * The CircularArrayQueue class implements a queue data structure using a circular array.
 * This implementation allows for efficient use of space, as elements are enqueued at the rear
 * and dequeued from the front, with the indices wrapping around the array.
 *
 * @param <T> the type of elements held in this queue
 */
public class CircularArrayQueue<T> implements QueueADT<T> {

    private static final int DEFAULT_CAPACITY = 10; // Default initial capacity
    private T[] array; // Array to store queue elements
    private int front; // Index of the front element
    private int rear; // Index of the rear element
    private int size;// Number of elements in the queue

    /**
     * Constructs an empty queue with default capacity.
     */
    public CircularArrayQueue(){
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructs an empty queue with the specified initial capacity.
     *
     * @param capacity the initial capacity of the queue
     */
    @SuppressWarnings("unchecked")
    public CircularArrayQueue(int capacity) {

        array = (T[]) new Object[capacity];
        front = 0;
        rear = capacity - 1;
        size = 0;
    }

    /**
     * Adds a new element to the rear of the queue.
     *
     * @param element the element to be added
     */
    @Override
    public void enqueue(T element) {
        if (size() == array.length) {
            expandCapacity();
        }
        rear = (rear + 1) % array.length;
        array[rear] = element;
        size++;
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return the element removed from the front of the queue
     * @throws EmptyCollectionException if the queue is empty
     */
    @Override
    public T dequeue() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("A fila esta vazia.");
        }
        T element = array[front];
        array[front] = null;
        front = (front + 1) % array.length;
        size--;
        return element;
    }

    /**
     * Retrieves the element at a specific index from the front of the queue.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public T get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + index);
        }
        int actualIndex = (front + index) % array.length;
        return array[actualIndex];
    }

    /**
     * Returns the element at the front of the queue without removing it.
     *
     * @return the element at the front of the queue
     * @throws EmptyCollectionException if the queue is empty
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException("A fila esta vazia.");
        }
        return array[front];
    }

    /**
     * Returns a string representation of the queue.
     *
     * @return a string representing the queue
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("CircularArrayQueue [");
        int count = 0;
        int index = front;
        while (count < size()) {
            sb.append(array[index]);
            if (count < size() - 1) {
                sb.append(", ");
            }
            index = (index + 1) % array.length;
            count++;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise
     */
    @Override
    public boolean isEmpty(){
        return size() == 0;
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @return the size of the queue
     */
    @Override
    public int size(){
        return size;
    }

    /**
     * Expands the capacity of the queue.
     * This method is called internally when the array is full.
     */
    public void expandCapacity() {
        @SuppressWarnings("unchecked")
        T[] largerArray = (T[]) new Object[array.length * 2];
        int index = front;
        for (int i = 0; i < size(); i++) {
            largerArray[i] = array[index];
            index = (index + 1) % array.length;
        }
        array = largerArray;
        front = 0;
        rear = size() - 1;
    }
}