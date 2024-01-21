package capturetheflag.estruturas.IteradoresArrayBinaryTree;

import java.util.Iterator;
import capturetheflag.estruturas.ArrayUnorderedList;

public class InOrderIterator<T> implements Iterator<T> {
    private ArrayUnorderedList<T> tempList;
    private Iterator<T> iter;
    private T[] tree;

    public InOrderIterator(T[] tree) {
        this.tree = tree;
        tempList = new ArrayUnorderedList<T>();
        inorder(0, tempList);
        iter = tempList.iterator();
    }

    private void inorder(int node, ArrayUnorderedList<T> tempList) {
        if (node < tree.length && tree[node] != null) {
            inorder(node * 2 + 1, tempList);
            tempList.addToRear(tree[node]);
            inorder((node + 1) * 2, tempList);
        }
    }

    @Override
    public boolean hasNext() {
        return iter.hasNext();
    }

    @Override
    public T next() {
        return iter.next();
    }
}
