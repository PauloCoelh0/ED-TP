package tp_ed.structures;

import tp_ed.structures.exceptions.ElementNotFoundException;
import tp_ed.structures.interfaces.UnorderedListADT;

public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {


// Isto vai ser utilizado para as arvores

//    public ArrayUnorderedList() {
//        super();
//    }
//
//    public ArrayUnorderedList(int initialCapacity) {
//        super(initialCapacity);
//    }

    @Override
    public void addToFront(T element) {
        if (rear == list.length) {
            expandCapacity();
        }

        // Shift elements to make space for the new element at the front
        for (int i = rear; i > 0; i--) {
            list[i] = list[i - 1];
        }

        list[0] = element;
        rear++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        if (rear == list.length) {
            expandCapacity();
        }

        list[rear] = element;
        rear++;
        modCount++;
    }

    @Override
    public void addAfter(T element, T target) throws ElementNotFoundException {
        int targetIndex = -1;

        // Find the index of the target element
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

        // Shift elements to make space for the new element
        for (int i = rear; i > targetIndex + 1; i--) {
            list[i] = list[i - 1];
        }

        list[targetIndex + 1] = element;
        rear++;
        modCount++;
    }

    // TODO ver
    public T getIndex(int index) {
        if (index >= 0 && index < rear) {
            return list[index];
        } else {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + index);
        }
    }

}
