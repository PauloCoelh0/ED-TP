package capturetheflag.structures;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.interfaces.QueueADT;
import capturetheflag.nodes.Node;

public class LinkedQueue<T> implements QueueADT<T> {

    private Node<T> front; // inicio da fila
    private Node<T> rear; // final da fila
    private int size; // tamanho da fila

    public LinkedQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

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

    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException("A fila esta vazia");
        }
        return front.getElement();
    }

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
            return result.trim(); // Remover o espaco em branco extra no final.
        }
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("LinkedQueue [");
//        Node<T> current = front;
//        while (current != null) {
//            sb.append(current.element);
//            if (current.next != null) {
//                sb.append(", ");
//            }
//            current = current.next;
//        }
//        sb.append("]");
//        return sb.toString();
//    }
}