package tp_ed.structures;

import tp_ed.structures.IteradoresArrayBinaryTree.InOrderIterator;
import tp_ed.structures.IteradoresArrayBinaryTree.LevelOrderIterator;
import tp_ed.structures.IteradoresArrayBinaryTree.PostOrderIterator;
import tp_ed.structures.IteradoresArrayBinaryTree.PreOrderIterator;
import tp_ed.structures.exceptions.ElementNotFoundException;
import tp_ed.structures.exceptions.EmptyCollectionException;
import tp_ed.structures.interfaces.BinaryTreeADT;

import java.util.Iterator;

/**
 * The ArrayBinaryTree class represents a binary tree structure implemented using an array.
 * It provides basic operations for binary trees such as getting the root, checking if the tree is empty,
 * and performing various types of traversals.
 *
 * @param <T> the type of elements held in this binary tree
 */
public class ArrayBinaryTree<T> implements BinaryTreeADT<T> {

    protected int count;
    protected T[] tree;
    private final int CAPACITY = 50;

    /**
     * Constructs an empty binary tree with a default capacity.
     */
    public ArrayBinaryTree() {
        count = 0;
        tree = (T[]) new Object[CAPACITY];
    }

    /**
     * Constructs a binary tree with the specified element as its root.
     *
     * @param element the element that will become the root of the new tree
     */
    public ArrayBinaryTree(T element) {
        count = 1;
        tree = (T[]) new Object[CAPACITY];
        tree[0] = element;
    }

    /**
     * Returns the element stored at the root of the tree.
     *
     * @return the root element of the tree, or null if the tree is empty
     */
    @Override
    public T getRoot() {
        if (isEmpty()) {
            return null;
        }
        return tree[0];
    }

    /**
     * Checks if the binary tree is empty.
     *
     * @return true if the tree is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the number of elements in the binary tree.
     *
     * @return the size of the tree
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Checks if the binary tree contains a specific element.
     *
     * @param targetElement the element being sought in the tree
     * @return true if the tree contains the target element, false otherwise
     * @throws ElementNotFoundException if the target element is not found
     */
    @Override
    public boolean contains(T targetElement) throws ElementNotFoundException {
        return find(targetElement) != null;
    }

    /**
     * Returns the specified element if it is found in the binary tree.
     *
     * @param targetElement the element being sought in the tree
     * @return the found element
     * @throws ElementNotFoundException if the target element is not found
     */
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

    /**
     * Returns an iterator over the elements in this binary tree using in-order traversal.
     *
     * @return an in-order iterator over the tree
     */
    @Override
    public Iterator<T> iteratorInOrder() {
        return new InOrderIterator<>(tree);
    }

    /**
     * Returns an iterator over the elements in this binary tree using pre-order traversal.
     *
     * @return a pre-order iterator over the tree
     */
    @Override
    public Iterator<T> iteratorPreOrder() {
        return new PreOrderIterator<>(tree);
    }

    /**
     * Returns an iterator over the elements in this binary tree using post-order traversal.
     *
     * @return a post-order iterator over the tree
     */
    @Override
    public Iterator<T> iteratorPostOrder() {
        return new PostOrderIterator<>(tree);
    }

    /**
     * Returns an iterator over the elements in this binary tree using level-order traversal.
     *
     * @return a level-order iterator over the tree
     * @throws EmptyCollectionException if the tree is empty
     */
    @Override
    public Iterator<T> iteratorLevelOrder() throws EmptyCollectionException {
        return new LevelOrderIterator<>(tree);
    }
}