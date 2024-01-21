package capturetheflag.structures;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.interfaces.StackADT;
import capturetheflag.nodes.Node;

public class LinkedStack<T> implements StackADT<T> {

    private Node<T> top;
    private int count;

    //Construtor
    public LinkedStack() {
        top = null;
        count = 0;
    }

    @Override
    public void push(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.setNext(top);
        top = newNode;
        count++;
    }

    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack is empty");
        }
        T data = top.getElement();
        top = top.getNext();
        count--;  // Corrigido para decrementar a contagem
        return data;
    }

    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack is empty");
        }
        return top.getElement();
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Stack : (empty)";
        }else {
            String result = "Stack: ";
            Node<T> current =  top;
            while (current != null) {
                result += current.getElement() + " ";
                current = current.getNext();
            }
            return result.trim(); // Remover o espaco em branco extra no final.
        }
    }
}
