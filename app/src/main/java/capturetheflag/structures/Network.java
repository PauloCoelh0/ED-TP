package capturetheflag.structures;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.interfaces.NetworkADT;

import java.util.Iterator;

public class Network<T> extends Graph<T> implements NetworkADT<T> {

    private double[][] weightMatrix; // Matriz para armazenar os pesos das arestas

    public Network() {
        super();
        weightMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        // Inicializar a matriz de pesos com um valor padrão, como Double.POSITIVE_INFINITY
        initializeWeightMatrix();
    }

    private boolean isBidirectional;

    public Network(boolean isBidirectional) {
        super();
        this.isBidirectional = isBidirectional;
        weightMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        initializeWeightMatrix();
    }

    private void initializeWeightMatrix() {
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            for (int j = 0; j < DEFAULT_CAPACITY; j++) {
                if (i == j) {
                    weightMatrix[i][j] = 0;
                } else {
                    weightMatrix[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    @Override
    protected void expandCapacity() {
        super.expandCapacity();

        double[][] largerWeightMatrix = new double[vertices.length][vertices.length];
        for (int i = 0; i < numVertices; i++) {
            System.arraycopy(weightMatrix[i], 0, largerWeightMatrix[i], 0, numVertices);
        }
        weightMatrix = largerWeightMatrix;
        // Inicializar novos valores com Double.POSITIVE_INFINITY
        for (int i = numVertices; i < weightMatrix.length; i++) {
            for (int j = numVertices; j < weightMatrix.length; j++) {
                weightMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    // TODO ver com o paulo se é necessário adicionar os métodos novos que adicionei à estrutura network à NetworkADT

    public boolean containsVertex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertex.equals(vertices[i])) {
                return true;
            }
        }
        return false;
    }

    // Método para obter o vértice em um índice específico
    public T getVertex(int index) {
        if (indexIsValid(index)) {
            return vertices[index];
        } else {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + index);
        }
    }

    // Método para obter o peso entre dois vértices
    public double getWeight(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            return weightMatrix[index1][index2];
        } else {
            throw new IllegalArgumentException("Vértice não encontrado");
        }
    }

    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = true; // Aresta de vertex1 para vertex2
            weightMatrix[index1][index2] = weight; // Peso da aresta de vertex1 para vertex2


            if (isBidirectional) {
                // Para grafos não direcionados, adiciona a aresta inversa
                adjMatrix[index2][index1] = true;
                weightMatrix[index2][index1] = weight;
            }
        }
    }

    @Override
    public double shortestPathWeight(T startVertex, T targetVertex) {
        int startIndex = getIndex(startVertex);
        int targetIndex = getIndex(targetVertex);

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
            return Double.POSITIVE_INFINITY; // Retorna um valor alto para indicar que não há caminho.
        }

        double[] distances = new double[numVertices];
        boolean[] visited = new boolean[numVertices];

        // Inicializa as distâncias como infinito e visitado como falso
        for (int i = 0; i < numVertices; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
        }

        distances[startIndex] = 0; // Distância do vértice inicial para ele mesmo é sempre 0

        // Encontre o caminho mais curto para todos os vértices
        for (int i = 0; i < numVertices - 1; i++) {
            // Seleciona o vértice de menor distância do conjunto de vértices ainda não processados
            int u = minDistance(distances, visited);
            visited[u] = true;

            // Atualiza a distância dos vértices adjacentes do vértice selecionado
            for (int v = 0; v < numVertices; v++) {
                // Atualiza distances[v] somente se não estiver em visited, há uma aresta de
                // u para v, e o peso total do caminho de startVertex até v através de u é
                // menor que o valor atual de distances[v]
                if (!visited[v] && adjMatrix[u][v] && distances[u] != Double.POSITIVE_INFINITY
                        && distances[u] + weightMatrix[u][v] < distances[v]) {
                    distances[v] = distances[u] + weightMatrix[u][v];
                }
            }
        }

        return distances[targetIndex];
    }

    // Método auxiliar para encontrar o vértice com a menor distância que ainda não foi processado
    private int minDistance(double[] distances, boolean[] visited) {
        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;

        for (int v = 0; v < numVertices; v++) {
            if (!visited[v] && distances[v] <= min) {
                min = distances[v];
                minIndex = v;
            }
        }

        return minIndex;
    }
}
