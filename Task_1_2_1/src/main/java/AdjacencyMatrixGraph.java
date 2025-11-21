import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AdjacencyMatrixGraph<T> extends Graph<T> {
    private final Map<T, Integer> vertexToIndex = new HashMap<>();
    private final List<T> indexToVertex = new ArrayList<>();
    private boolean[][] matrix;

    public AdjacencyMatrixGraph() {
        matrix = new boolean[0][0];
    }

    @Override
    public void addVertex(T vertex) {
        if (!vertexToIndex.containsKey(vertex)) {
            int newIndex = indexToVertex.size();
            vertexToIndex.put(vertex, newIndex);
            indexToVertex.add(vertex);
            resizeMatrix(indexToVertex.size());
        }
    }

    private void resizeMatrix(int newSize) {
        boolean[][] newMatrix = new boolean[newSize][newSize];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix.length);
        }
        matrix = newMatrix;
    }

    @Override
    public void removeVertex(T vertex) {
        Integer index = vertexToIndex.remove(vertex);
        if (index != null) {
            indexToVertex.remove(index.intValue());
            boolean[][] newMatrix = new boolean[indexToVertex.size()][indexToVertex.size()];
            Map<T, Integer> newVertexToIndex = new HashMap<>();
            int newIndex = 0;
            for (int i = 0; i < indexToVertex.size(); i++) {
                newVertexToIndex.put(indexToVertex.get(i), newIndex);
                for (int j = 0; j < indexToVertex.size(); j++) {
                    int oldI = (i < index) ? i : i + 1;
                    int oldJ = (j < index) ? j : j + 1;
                    newMatrix[i][j] = matrix[oldI][oldJ];
                }
                newIndex++;
            }
            vertexToIndex.clear();
            vertexToIndex.putAll(newVertexToIndex);
            matrix = newMatrix;
        }
    }

    @Override
    public void addEdge(T from, T to) {
        addVertex(from);
        addVertex(to);
        int fromIndex = vertexToIndex.get(from);
        int toIndex = vertexToIndex.get(to);
        matrix[fromIndex][toIndex] = true;
    }

    @Override
    public void removeEdge(T from, T to) {
        Integer fromIndex = vertexToIndex.get(from);
        Integer toIndex = vertexToIndex.get(to);
        if (fromIndex != null && toIndex != null) {
            matrix[fromIndex.intValue()][toIndex.intValue()] = false;
        }
    }

    @Override
    public List<T> getNeighbors(T vertex) {
        List<T> neighbors = new ArrayList<>();
        Integer index = vertexToIndex.get(vertex);
        if (index != null) {
            for (int j = 0; j < matrix[index].length; j++) {
                if (matrix[index][j]) {
                    neighbors.add(indexToVertex.get(j));
                }
            }
        }
        return neighbors;
    }

    @Override
    public Set<T> getVertices() {
        return new HashSet<>(indexToVertex);
    }

    @Override
    protected String getGraphTypeName() {
        return "Adjacency Matrix";
    }

    @Override
    public List<T> topologicalSort() {
        boolean[] visited = new boolean[indexToVertex.size()];
        List<T> order = new ArrayList<>();
        for (int i = 0; i < indexToVertex.size(); i++) {
            if (!visited[i]) {
                dfsTopo(i, visited, order);
            }
        }
        Collections.reverse(order);
        return order;
    }

    private void dfsTopo(int index, boolean[] visited, List<T> order) {
        visited[index] = true;
        for (int j = 0; j < matrix.length; j++) {
            if (matrix[index][j] && !visited[j]) {
                dfsTopo(j, visited, order);
            }
        }
        order.add(indexToVertex.get(index));
    }
}