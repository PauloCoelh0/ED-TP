package CaptureTheFlag.Estruturas;

import CaptureTheFlag.Exceptions.ElementNotFoundException;
import CaptureTheFlag.Exceptions.EmptyCollectionException;
import CaptureTheFlag.Interfaces.ListADT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class ArrayList<T> implements ListADT<T> {

    protected static final int DEFAULT_CAPACITY = 10;
    protected int rear;
    protected T[] list;
    protected int modCount;

    public ArrayList() {
        list = (T[]) new Object[DEFAULT_CAPACITY];
        rear = 0;
    }

    @Override
    public boolean isEmpty() {
        return (rear == 0);
    }

    @Override
    public int size() {
        return rear;
    }

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

    @Override
    public T last() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Lista esta vazia.");
        }
        return list[rear - 1];
    }

    @Override
    public T removeLast() {

        T result;

        rear--;
        result = list[rear];
        list[rear] = null;

        return result;

    }

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

    @Override
    public T first() throws EmptyCollectionException {

        if (isEmpty()) {
            throw new EmptyCollectionException("Lista esta vazia.");
        }

        return list[0];
    }

    @Override
    public boolean contains(T target) {

        for (int i = 0; i < rear; i++) {
            if (list[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    protected void expandCapacity() {
        T[] larger = (T[]) (new Object[list.length * 2]);
        for (int i = 0; i < list.length; i++) {
            larger[i] = list[i];
        }
        list = larger;
    }

    private class ArrayListIterator implements Iterator<T> {

        private int current; // indice do elemento atual
        private int expectedModCount; // Mantém o registro de modificações estruturais
        private boolean okToRemove; // Flag para garantir remoção após um next()

        public ArrayListIterator() {
            current = 0;
            expectedModCount = modCount; // Registra a contagem atual
            okToRemove = false;
        }

        @Override
        public boolean hasNext() {
            return current < rear; // Verifica se ainda há elementos na lista
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("A lista foi modificada.");
            }

            okToRemove = true; // Permite a remoção após este next()
            return list[current++];
        }

        @Override
        public void remove() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("A lista foi modificada.");
            }
            if (!okToRemove) {
                throw new IllegalStateException("Chamada inválida a remove().");
            }

            try {
                ArrayList.this.remove(list[current - 1]); // Remove o elemento

                rear--; // Atualiza o tamanho da lista

                expectedModCount = modCount; // Atualiza a contagem

                okToRemove = false; // Reseta a flag para remoção

            } catch (ElementNotFoundException e) {
                // Trate a exceção ElementNotFoundException aqui
                // Por exemplo, relance ou manipule de acordo com sua lógica
                System.out.println("Elemento não encontrado: " + e.getMessage());
            }
        }
    }
}
