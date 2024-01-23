package tp_ed.structures;

import tp_ed.structures.exceptions.ElementNotFoundException;
import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.structures.exceptions.ModificationException;
import tp_ed.structures.interfaces.ListADT;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The ArrayList class represents a dynamic array implementation of a list.
 * It provides methods for common list operations, including adding and removing elements,
 * checking size and emptiness, and iterating over elements.
 *
 * @param <T> the type of elements held in this list
 */
public abstract class ArrayList<T> implements ListADT<T> {

    protected static final int DEFAULT_CAPACITY = 10;// Default initial capacity
    protected int rear;// Number of elements in the list
    protected T[] list;// Array to store list elements
    protected int modCount;// Count of modifications made to the list

    /**
     * Constructs an empty ArrayList with default capacity.
     */
    public ArrayList() {
        list = (T[]) new Object[DEFAULT_CAPACITY];
        rear = 0;
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return (rear == 0);
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the number of elements
     */
    @Override
    public int size() {
        return rear;
    }

    /**
     * Returns a string representation of the list.
     *
     * @return a string representation
     */
    public String toString() {

        String result = " [";

        for (int i = 0; i < this.size(); i++) {
            result += list[i];

            if (i < this.size() - 1) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }

    /**
     * Removes and returns the first element from the list.
     *
     * @return the first element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Lista esta vazia.");
        }

        T result = list[0];
        rear--;

        for (int i = 0; i < rear; i++) {
            list[i] = list[i + 1];
        }

        list[rear] = null;

        return result;
    }

    /**
     * Returns the last element from the list without removing it.
     *
     * @return the last element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T last() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Lista esta vazia.");
        }
        return list[rear - 1];
    }

    /**
     * Removes and returns the last element from the list.
     *
     * @return the last element
     */
    @Override
    public T removeLast() {

        T result;

        rear--;
        result = list[rear];
        list[rear] = null;

        return result;

    }

    /**
     * Removes and returns a specific element from the list.
     *
     * @param element the element to be removed
     * @return the removed element
     * @throws ElementNotFoundException if the element is not found
     */
    @Override
    public T remove(T element) throws ElementNotFoundException {
        if (isEmpty()) {
            throw new ElementNotFoundException("Lista esta vazia.");
        }

        int index = -1;

        for (int i = 0; i < rear; i++) {
            if (list[i].equals(element)) {
                index = i;
                break;
            };
        }

        if (index == -1) {
            throw new ElementNotFoundException("Elemento nao encontrado.");
        }

        T result = list[index];
        rear--;

        for (int i = index; i < rear; i++) {
            list[i] = list[i + 1];
        }

        list[rear] = null;

        return result;
    }

    /**
     * Returns the first element from the list without removing it.
     *
     * @return the first element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T first() throws EmptyCollectionException {

        if (isEmpty()) {
            throw new EmptyCollectionException("Lista esta vazia.");
        }

        return list[0];
    }

    /**
     * Checks if the list contains a specific element.
     *
     * @param target the element to search for
     * @return true if the list contains the element, false otherwise
     */
    @Override
    public boolean contains(T target) {

        for (int i = 0; i < rear; i++) {
            if (list[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an iterator over the elements in the list in proper sequence.
     *
     * @return an iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    /**
     * Expands the capacity of the list.
     * This method is called internally when the array is full.
     */
    protected void expandCapacity() {
        T[] larger = (T[]) (new Object[list.length * 2]);
        for (int i = 0; i < list.length; i++) {
            larger[i] = list[i];
        }
        list = larger;
    }

    /**
     * ArrayListIterator is an inner class that provides an iterator over the elements of ArrayList.
     */
    private class ArrayListIterator implements Iterator<T> {

        private int current;
        private int expectedModCount;
        private boolean okToRemove;

        public ArrayListIterator() {
            current = 0;
            expectedModCount = modCount;
            okToRemove = false;
        }

        @Override
        public boolean hasNext() {
            return current < rear;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (expectedModCount != modCount) {
                try {
                    throw new ModificationException("A lista foi modificada.");
                } catch (ModificationException e) {
                    throw new RuntimeException(e);
                }
            }

            okToRemove = true;
            return list[current++];
        }

        @Override
        public void remove() {
            if (expectedModCount != modCount) {
                try {
                    throw new ModificationException("A lista foi modificada.");
                } catch (ModificationException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!okToRemove) {
                throw new IllegalStateException("Chamada inválida a remove().");
            }

            try {
                ArrayList.this.remove(list[current - 1]);

                rear--;

                expectedModCount = modCount;

                okToRemove = false;

            } catch (ElementNotFoundException e) {
                System.out.println("Elemento não encontrado: " + e.getMessage());
            }
        }
    }
}
