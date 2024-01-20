package CaptureTheFlag.Interfaces;

import CaptureTheFlag.Exceptions.EmptyCollectionException;

public interface StackADT<T> {
    public void push(T element);
    public T pop() throws EmptyCollectionException;
    public T peek() throws EmptyCollectionException;
    public boolean isEmpty();
    int size();
    @Override
    public String toString();
}