package tp_ed.structures;

import tp_ed.structures.exceptions.ElementNotFoundException;
import tp_ed.structures.interfaces.UnorderedListADT;

/**
 * The ArrayUnorderedList class represents an unordered list implemented using an array.
 * It extends ArrayList and implements the UnorderedListADT interface, providing methods
 * to add elements to the front, rear, or after a specific target element in the list.
 *
 * @param <T> the type of elements held in this list
 */
public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {

    /**
     * Adds a new element to the front of the list.
     *
     * @param element the element to be added to the front of the list
     */
    @Override
    public void addToFront(T element) {
        if (rear == list.length) {
            expandCapacity();
        }

        for (int i = rear; i > 0; i--) {
            list[i] = list[i - 1];
        }

        list[0] = element;
        rear++;
        modCount++;
    }

    /**
     * Adds a new element to the rear of the list.
     *
     * @param element the element to be added to the rear of the list
     */
    @Override
    public void addToRear(T element) {
        if (rear == list.length) {
            expandCapacity();
        }

        list[rear] = element;
        rear++;
        modCount++;
    }

    /**
     * Adds a new element after a specific target element in the list.
     * If the target element is not found, an ElementNotFoundException is thrown.
     *
     * @param element the element to be added
     * @param target  the target element after which the new element should be added
     * @throws ElementNotFoundException if the target element is not found in the list
     */
    @Override
    public void addAfter(T element, T target) throws ElementNotFoundException {
        int targetIndex = -1;

        for (int i = 0; i < rear; i++) {
            if (target.equals(list[i])) {
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1) {
            throw new ElementNotFoundException("ArrayList");
        }

        if (rear == list.length) {
            expandCapacity();
        }

        for (int i = rear; i > targetIndex + 1; i--) {
            list[i] = list[i - 1];
        }

        list[targetIndex + 1] = element;
        rear++;
        modCount++;
    }

    /**
     * Retrieves the element at a specific index in the list.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public T getIndex(int index) {
        if (index >= 0 && index < rear) {
            return list[index];
        } else {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + index);
        }
    }
}