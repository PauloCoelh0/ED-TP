package tp_ed.structures.IteradoresArrayBinaryTree;

import tp_ed.structures.ArrayUnorderedList;
import tp_ed.structures.LinkedQueue;
import tp_ed.structures.exceptions.EmptyCollectionException;

import java.util.Iterator;

public class LevelOrderIterator<T> implements Iterator<T> {
    private ArrayUnorderedList<T> tempList;
    private Iterator<T> iter;
    private T[] tree;

    public LevelOrderIterator(T[] tree) throws EmptyCollectionException {
        this.tree = tree;
        tempList = new ArrayUnorderedList<T>();
        if (tree == null || tree.length == 0 || tree[0] == null) {
            throw new EmptyCollectionException("binary tree");
        }

        LinkedQueue<Integer> nodeQueue = new LinkedQueue<>();
        nodeQueue.enqueue(0);

        while (!nodeQueue.isEmpty()) {
            int node = nodeQueue.dequeue();
            if (node < tree.length && tree[node] != null) {
                tempList.addToRear(tree[node]);
                int left = node * 2 + 1;
                int right = left + 1;
                if (left < tree.length && tree[left] != null) {
                    nodeQueue.enqueue(left);
                }
                if (right < tree.length && tree[right] != null) {
                    nodeQueue.enqueue(right);
                }
            }
        }

        iter = tempList.iterator();
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
