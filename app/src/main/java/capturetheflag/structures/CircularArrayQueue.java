package capturetheflag.structures;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.interfaces.QueueADT;

public class CircularArrayQueue<T> implements QueueADT<T> {

    private static final int DEFAULT_CAPACITY = 10;
    private T[] array;
    private int front; // indice de inicio da queue
    private int rear; // indice do final da queue
    private int size; // tamanho atual da queue

    public CircularArrayQueue(){
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public CircularArrayQueue(int capacity) {

        array = (T[]) new Object[capacity];
        front = 0;
        rear = capacity - 1;
        size = 0;
    }

    @Override
    public void enqueue(T element) {
        if (size() == array.length) {
            expandCapacity();
        }
        rear = (rear + 1) % array.length;
        array[rear] = element;
        size++;
    }

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

    public T get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + index);
        }
        int actualIndex = (front + index) % array.length;
        return array[actualIndex];
    }


    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException("A fila esta vazia.");
        }
        return array[front];
    }

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


    @Override
    public boolean isEmpty(){
        return size() == 0;
    }

    @Override
    public int size(){
        return size;
    }

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
