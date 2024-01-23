package tp_ed.structures.nodes;

/**
 * The Node class represents a node in a linked structure.
 * It contains a reference to the element it stores and to the next node in the structure.
 *
 * @param <T> the type of element held in the node
 */
public class Node<T> {

    private Node<T> next; // Reference to the next node
    private T element; // Element stored in this node

    /**
     * Constructs an empty node with no element and no next node reference.
     */
    public Node() {
        next = null;
        element = null;
    }

    /**
     * Constructs a node storing the specified element and with no next node reference.
     *
     * @param elem the element to be stored in the node
     */
    public Node(T elem) {
        next = null;
        element = elem;
    }

    /**
     * Returns the next node this node refers to.
     *
     * @return the next node
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * Sets the next node reference to the given node.
     *
     * @param node the node to set as the next node
     */
    public void setNext(Node<T> node) {
        next = node;
    }

    /**
     * Returns the element stored in this node.
     *
     * @return the element stored in the node
     */
    public T getElement() {
        return element;
    }

    /**
     * Sets the element of this node to the specified element.
     *
     * @param elem the element to be stored in this node
     */
    public void setElement(T elem) {
        element = elem;
    }

}