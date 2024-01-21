package capturetheflag.estruturas.IteradoresArrayBinaryTree;

import capturetheflag.estruturas.ArrayUnorderedList;
import java.util.Iterator;

public class PostOrderIterator<T> implements Iterator<T> {
    private ArrayUnorderedList<T> tempList;
    private Iterator<T> iter;
    private T[] tree;

    public PostOrderIterator(T[] tree) {
        this.tree = tree;
        tempList = new ArrayUnorderedList<T>();
        postorder(0, tempList);
        iter = tempList.iterator();
    }

    private void postorder(int node, ArrayUnorderedList<T> tempList) {
        if (node < tree.length && tree[node] != null) {
            postorder(node * 2 + 1, tempList);
            postorder((node + 1) * 2, tempList);
            tempList.addToRear(tree[node]);
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
