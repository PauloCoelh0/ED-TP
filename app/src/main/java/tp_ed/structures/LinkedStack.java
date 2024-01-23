package tp_ed.structures;

import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.structures.interfaces.StackADT;
import tp_ed.structures.nodes.Node;

/**
 * The LinkedStack class represents a stack data structure implemented using a linked list.
 * It provides standard stack operations such as push, pop, and peek.
 *
 * @param <T> the type of elements held in this stack
 */
public class LinkedStack<T> implements StackADT<T> {

    /**
     * The top node of the stack.
     */
    private Node<T> top;

    /**
     * The number of elements in the stack.
     */
    private int count;

    /**
     * Constructs an empty LinkedStack.
     */
    public LinkedStack() {
        top = null;
        count = 0;
    }

    /**
     * Adds a new element to the top of the stack.
     *
     * @param element the element to be added to the stack
     */
    @Override
    public void push(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.setNext(top);
        top = newNode;
        count++;
    }

    /**
     * Removes and returns the top element of the stack.
     *
     * @return the element removed from the top of the stack
     * @throws EmptyCollectionException if the stack is empty
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack is empty");
        }
        T data = top.getElement();
        top = top.getNext();
        count--;
        return data;
    }

    /**
     * Returns the top element of the stack without removing it.
     *
     * @return the element at the top of the stack
     * @throws EmptyCollectionException if the stack is empty
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack is empty");
        }
        return top.getElement();
    }

    /**
     * Checks if the stack is empty.
     *
     * @return true if the stack is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return the size of the stack
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Returns a string representation of the stack.
     *
     * @return a string representing the stack
     */
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
            return result.trim();
        }
    }
}
