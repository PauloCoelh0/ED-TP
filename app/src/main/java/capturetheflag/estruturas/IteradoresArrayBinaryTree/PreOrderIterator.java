package capturetheflag.estruturas.IteradoresArrayBinaryTree;

import capturetheflag.estruturas.ArrayUnorderedList;
import java.util.Iterator;

public class PreOrderIterator<T> implements Iterator<T> {
    private ArrayUnorderedList<T> tempList;
    private Iterator<T> iter;
    private T[] tree;

    public PreOrderIterator(T[] tree) {
        this.tree = tree;
        tempList = new ArrayUnorderedList<T>();
        preorder(0, tempList);
        iter = tempList.iterator();
    }

    private void preorder(int node, ArrayUnorderedList<T> tempList) {
        if (node < tree.length && tree[node] != null) {
            tempList.addToRear(tree[node]);
            preorder(node * 2 + 1, tempList);
            preorder((node + 1) * 2, tempList);
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