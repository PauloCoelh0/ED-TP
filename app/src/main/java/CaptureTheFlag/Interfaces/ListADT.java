package CaptureTheFlag.Interfaces;

import java.util.Iterator;
import CaptureTheFlag.Exceptions.ElementNotFoundException;
import CaptureTheFlag.Exceptions.EmptyCollectionException;

public interface ListADT<T> extends Iterable{
    public T removeFirst() throws EmptyCollectionException;
    public T removeLast() throws EmptyCollectionException;
    public T remove(T element) throws ElementNotFoundException;
    public T first() throws EmptyCollectionException;
    public T last() throws EmptyCollectionException;
    public boolean contains (T target);
    public boolean isEmpty();
    public int size();
    public Iterator<T> iterator();
    @Override
    public String toString();
}
