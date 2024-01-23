package tp_ed.structures;

import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.structures.interfaces.QueueADT;
import tp_ed.structures.nodes.Node;

/**
 * The LinkedQueue class implements a queue data structure using a linked list.
 * It offers standard queue operations such as enqueue, dequeue, and inspecting the first element.
 *
 * @param <T> the type of elements held in this queue
 */
public class LinkedQueue<T> implements QueueADT<T> {

    /**
     * The front node of the queue.
     */
    private Node<T> front;

    /**
     * The rear node of the queue.
     */
    private Node<T> rear;

    /**
     * The number of elements in the queue.
     */
    private int size;

    /**
     * Constructs an empty LinkedQueue.
     */
    public LinkedQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @return the size of the queue
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Adds a new element to the rear of the queue.
     *
     * @param element the element to be added to the queue
     */
    @Override
    public void enqueue(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            front = newNode;
        } else {
            rear.setNext(newNode);
        }
        rear = newNode;
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
        T element = front.getElement();
        front = front.getNext();
        size--;
        if (front == null) {
            rear = null;
        }
        return element;
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
            throw new EmptyCollectionException("A fila esta vazia");
        }
        return front.getElement();
    }

    /**
     * Returns a string representation of the queue.
     *
     * @return a string representing the queue
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "Queue : (empty)";
        }else {
            String result = "Queue: ";
            Node<T> current =  front;
            while (current != null) {
                result += current.getElement() + " ";
                current = current.getNext();
            }
            return result.trim();
        }
    }
}