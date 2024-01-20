package CaptureTheFlag.Estruturas;


import CaptureTheFlag.Exceptions.EmptyCollectionException;
import CaptureTheFlag.Interfaces.GraphADT;

import java.util.Iterator;

/**
 * Graph represents an adjacency matrix implementation of a graph.
 */
public class Graph<T> implements GraphADT<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices; // number of vertices in the graph
    protected boolean[][] adjMatrix; // adjacency matrix
    protected T[] vertices; // values of vertices

    public Graph() {
        numVertices = 0;
        this.adjMatrix = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    public void addEdge(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            addEdge(index1, index2); // Chama o método sobrecarregado baseado em índices
        } else {
            // Tratamento de erro: um ou ambos os vértices não foram encontrados
            throw new IllegalArgumentException("Vertex not found");
        }
    }

    protected void addEdge(int index1, int index2) {
        adjMatrix[index1][index2] = true;
        adjMatrix[index2][index1] = true;
    }

    public void removeEdge(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = false;
            adjMatrix[index2][index1] = false;
        }
    }

    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        int startIndex = getIndex(startVertex);
        if (indexIsValid(startIndex)) {
            try {
                return iteratorBFS(startIndex);
            } catch (EmptyCollectionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Iterator<T> iteratorDFS(T startVertex) {
        int startIndex = getIndex(startVertex);
        if (indexIsValid(startIndex)) {
            try {
                return iteratorDFS(startIndex);
            } catch (EmptyCollectionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Iterator<T> iteratorBFS(int startIndex) throws EmptyCollectionException {
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        if (!indexIsValid(startIndex)) return resultList.iterator();

        boolean[] visited = new boolean[numVertices];
        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            int x = traversalQueue.dequeue();
            resultList.addToRear(vertices[x]);

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[x][i] && !visited[i]) {
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }

    public Iterator<T> iteratorDFS(int startIndex) throws EmptyCollectionException {
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        boolean[] visited = new boolean[numVertices];
        if (!indexIsValid(startIndex)) {
            return resultList.iterator();
        }

        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;

            for (int i = 0; (i < numVertices) && !found; i++) {
                if (adjMatrix[x][i] && !visited[i]) {
                    traversalStack.push(i);
                    resultList.addToRear(vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }
            if (!found && !traversalStack.isEmpty()) {
                traversalStack.pop();
            }
        }
        return resultList.iterator();
    }


    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) throws EmptyCollectionException {
        int startIndex = getIndex(startVertex);
        int targetIndex = getIndex(targetVertex);

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
            return null;
        }

        double[] distances = new double[numVertices];
        int[] path = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
            path[i] = -1;
        }
        distances[startIndex] = 0;
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.addElement(startIndex, 0);

        while (!queue.isEmpty()) {
            int u = queue.removeNext();

            // Iterar sobre todos os vértices adjacentes a 'u'
            for (int v = 0; v < numVertices; v++) {
                if (adjMatrix[u][v]) {
                    double weight = 1; // Ou use o peso real da aresta, se aplicável
                    double distanceThroughU = distances[u] + weight;

                    if (distanceThroughU < distances[v]) {
                        distances[v] = distanceThroughU;
                        path[v] = u;
                        queue.addElement(v, (int)distanceThroughU); // Adicionar ou atualizar na fila
                    }
                }
            }
        }

        // Reconstruir e retornar o caminho mais curto usando ArrayUnorderedList
        ArrayUnorderedList<T> pathList = new ArrayUnorderedList<T>();
        int at = targetIndex;
        while (at != -1) {
            pathList.addToFront(vertices[at]); // Adiciona ao início para inverter a ordem
            at = path[at];
        }

        // Verifica se o caminho é válido
        if (pathList.isEmpty() || !pathList.iterator().next().equals(startVertex)) {
            return null; // Retorna null se não houver caminho
        }

        return pathList.iterator();
    }

    // Implementações adicionais
    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();
        vertices[numVertices] = vertex;
        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = false;
            adjMatrix[i][numVertices] = false;
        }
        numVertices++;
    }

    @Override
    public void removeVertex(T vertex) {
        int index = getIndex(vertex);
        if (!indexIsValid(index)) {
            return;
        }

        // Remove vertex and shift vertices in array
        for (int i = index; i < numVertices - 1; i++) {
            vertices[i] = vertices[i + 1];
        }

        // Shift rows in adjacency matrix
        for (int i = index; i < numVertices - 1; i++) {
            for (int j = 0; j < numVertices; j++) {
                adjMatrix[i][j] = adjMatrix[i + 1][j];
            }
        }

        // Shift columns in adjacency matrix
        for (int i = 0; i < numVertices; i++) {
            for (int j = index; j < numVertices - 1; j++) {
                adjMatrix[i][j] = adjMatrix[i][j + 1];
            }
        }

        numVertices--;
    }

    // Verifica se já existe uma aresta entre dois vertices
    public boolean edgeExists(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            return adjMatrix[index1][index2];
        } else {
            return false;
        }
    }

    public boolean containsVertex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                return true;
            }
        }
        return false;
    }

    protected void expandCapacity() {
        T[] largerVertices = (T[]) (new Object[vertices.length * 2]);
        boolean[][] largerAdjMatrix = new boolean[vertices.length * 2][vertices.length * 2];

        for (int i = 0; i < numVertices; i++) {
            largerVertices[i] = vertices[i];
            System.arraycopy(adjMatrix[i], 0, largerAdjMatrix[i], 0, numVertices);
        }

        vertices = largerVertices;
        adjMatrix = largerAdjMatrix;
    }

    @Override
    public boolean isEmpty() {
        return numVertices == 0;
    }

    @Override
    public boolean isConnected() throws EmptyCollectionException {
        //Realiza uma travessia BFS a partir do primeiro vértice e verifica se todos os vértices foram visitados.
        if (numVertices == 0) {
            return false;
        }

        boolean[] visited = new boolean[numVertices];
        LinkedQueue<Integer> queue = new LinkedQueue<>();

        visited[0] = true;
        queue.enqueue(0);

        while (!queue.isEmpty()) {
            int v = queue.dequeue();

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[v][i] && !visited[i]) {
                    visited[i] = true;
                    queue.enqueue(i);
                }
            }
        }

        for (boolean visit : visited) {
            if (!visit) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int size() {
        return numVertices;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            s.append(vertices[i].toString()).append(": ");
            for (boolean j : adjMatrix[i]) {
                s.append(j ? "1 " : "0 ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    // Método para obter o índice de um vértice
    protected int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                return i;
            }
        }
        return -1;
    }

    // Método para verificar se um índice é válido
    protected boolean indexIsValid(int index) {
        return index >= 0 && index < numVertices;
    }
}
