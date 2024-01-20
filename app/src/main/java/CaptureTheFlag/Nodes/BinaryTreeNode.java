package CaptureTheFlag.Nodes;

public class BinaryTreeNode<T> {

    protected T element;
    protected BinaryTreeNode<T> left;
    protected BinaryTreeNode<T> right;

    public BinaryTreeNode(T element) {
        this.element = element;
        this.left = null;
        this.right = null;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public void setLeft(BinaryTreeNode<T> left) {
        this.left = left;
    }

    public void setRight(BinaryTreeNode<T> right) {
        this.right = right;
    }

    public T getElement() {
        return this.element;
    }

    public BinaryTreeNode<T> getLeft() {
        return this.left;
    }

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
