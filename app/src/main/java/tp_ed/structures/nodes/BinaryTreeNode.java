package tp_ed.structures.nodes;

/**
 * The BinaryTreeNode class represents a node in a binary tree structure.
 * It contains references to the data it stores and to its left and right children.
 *
 * @param <T> the type of element held in the node
 */
public class BinaryTreeNode<T> {

    protected T element;
    protected BinaryTreeNode<T> left;
    protected BinaryTreeNode<T> right;

    /**
     * Constructs a BinaryTreeNode with the specified data and no children.
     *
     * @param element the data to be stored within the new node
     */
    public BinaryTreeNode(T element) {
        this.element = element;
        this.left = null;
        this.right = null;
    }

    /**
     * Sets the data of this node to the specified element.
     *
     * @param element the data to be set
     */
    public void setElement(T element) {
        this.element = element;
    }

    /**
     * Sets the left child of this node to the specified node.
     *
     * @param left the node to be set as the left child
     */
    public void setLeft(BinaryTreeNode<T> left) {
        this.left = left;
    }

    /**
     * Sets the right child of this node to the specified node.
     *
     * @param right the node to be set as the right child
     */
    public void setRight(BinaryTreeNode<T> right) {
        this.right = right;
    }


    /**
     * Returns the data stored in this node.
     *
     * @return the data stored in the node
     */
    public T getElement() {
        return this.element;
    }

    /**
     * Returns the left child of this node.
     *
     * @return the left child
     */
    public BinaryTreeNode<T> getLeft() {
        return this.left;
    }


    /**
     * Returns the right child of this node.
     *
     * @return the right child
     */
    public BinaryTreeNode<T> getRight() {
        return this.right;
    }

    /**
     * Returns the number of non-null children of this node. This method may be
     * able to be written more efficiently.
     *
     * @return the integer number of non-null children of this node
     */
    public int numChildren() {
        int children = 0;
        if (left != null) {
            children = 1 + left.numChildren();
        }
        if (right != null) {
            children = children + 1 + right.numChildren();
        }
        return children;
    }
}