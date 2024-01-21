package capturetheflag.structures;

import capturetheflag.structures.IteradoresArrayBinaryTree.InOrderIterator;
import capturetheflag.structures.IteradoresArrayBinaryTree.LevelOrderIterator;
import capturetheflag.structures.IteradoresArrayBinaryTree.PostOrderIterator;
import capturetheflag.structures.IteradoresArrayBinaryTree.PreOrderIterator;
import capturetheflag.exceptions.ElementNotFoundException;
import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.interfaces.BinaryTreeADT;

import java.util.Iterator;

public class ArrayBinaryTree<T> implements BinaryTreeADT<T> {

    protected int count;
    protected T[] tree;
    private final int CAPACITY = 50;

    public ArrayBinaryTree() {
        count = 0;
        tree = (T[]) new Object[CAPACITY];
    }

    public ArrayBinaryTree(T element) {
        count = 1;
        tree = (T[]) new Object[CAPACITY];
        tree[0] = element;
    }

    @Override
    public T getRoot() {
        if (isEmpty()) {
            return null;
        }
        return tree[0];
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
    public boolean contains(T targetElement) throws ElementNotFoundException {
        return find(targetElement) != null;
    }

    public T find (T targetElement) throws ElementNotFoundException {
        T temp=null;
        boolean found = false;

        for (int ct=0; ct<count && !found; ct++)
            if (targetElement.equals(tree[ct])) {
                found = true;
                temp = tree[ct];
            }
        if (!found)
            throw new ElementNotFoundException("binary tree");
        return temp;
    }

    // Implementacao das travessias
    @Override
    public Iterator<T> iteratorInOrder() {
        return new InOrderIterator<>(tree);
    }

    @Override
    public Iterator<T> iteratorPreOrder() {
        return new PreOrderIterator<>(tree);
    }

    @Override
    public Iterator<T> iteratorPostOrder() {
        return new PostOrderIterator<>(tree);
    }

    @Override
    public Iterator<T> iteratorLevelOrder() throws EmptyCollectionException {
        return new LevelOrderIterator<>(tree);
    }
}
