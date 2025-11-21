import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IncidenceMatrixGraph<T> extends Graph<T> {
    private final Map<T, Integer> vertexToIndex = new HashMap<>();
    private final List<T> indexToVertex = new ArrayList<>();
    private final List<AbstractMap.SimpleEntry<T, T>> edges = new ArrayList<>();
    private int[][] matrix;

    public IncidenceMatrixGraph() {
        matrix = new int[0][0];
    }

    @Override
    public void addVertex(T vertex) {
        if (!vertexToIndex.containsKey(vertex)) {
            int newIndex = indexToVertex.size();
            vertexToIndex.put(vertex, newIndex);
            indexToVertex.add(vertex);
            int numEdges = edges.size();
            int[][] newMatrix = new int[newIndex + 1][numEdges];
            for (int i = 0; i < newIndex; i++) {
                System.arraycopy(matrix[i], 0, newMatrix[i], 0, numEdges);
            }
            matrix = newMatrix;
        }
    }

    @Override
    public void removeVertex(T vertex) {
        Integer index = vertexToIndex.remove(vertex);
        if (index != null) {
            List<Integer> columnsToRemove = new ArrayList<>();
            for (int e = 0; e < edges.size(); e++) {
                if (matrix[index][e] != 0) {
                    columnsToRemove.add(e);
                }
            }
            for (int col : columnsToRemove) {
                removeEdgeColumn(col);
            }
            int numVertices = indexToVertex.size();
            int numEdges = edges.size();
            int[][] newMatrix = new int[numVertices - 1][numEdges];
            int newRow = 0;
            for (int i = 0; i < numVertices; i++) {
                if (i == index) {
                    continue;
                }
                System.arraycopy(matrix[i], 0, newMatrix[newRow], 0, numEdges);
                newRow++;
            }
            matrix = newMatrix;
            indexToVertex.remove(index.intValue());
            for (int i = index; i < indexToVertex.size(); i++) {
                vertexToIndex.put(indexToVertex.get(i), i);
            }
        }
    }

    private void removeEdgeColumn(int col) {
        int numVertices = indexToVertex.size();
        int numEdges = edges.size();
        int[][] newMatrix = new int[numVertices][numEdges - 1];
        for (int i = 0; i < numVertices; i++) {
            int newCol = 0;
            for (int j = 0; j < numEdges; j++) {
                if (j == col) {
                    continue;
                }
                newMatrix[i][newCol] = matrix[i][j];
                newCol++;
            }
        }
        matrix = newMatrix;
        edges.remove(col);
    }

    @Override
    public void addEdge(T from, T to) {
        addVertex(from);
        addVertex(to);
        int fromIndex = vertexToIndex.get(from);
        int toIndex = vertexToIndex.get(to);
        int numVertices = indexToVertex.size();
        int numEdges = edges.size();
        int[][] newMatrix = new int[numVertices][numEdges + 1];
        for (int i = 0; i < numVertices; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, numEdges);
        }
        newMatrix[fromIndex][numEdges] = -1;
        newMatrix[toIndex][numEdges] = 1;
        matrix = newMatrix;
        edges.add(new AbstractMap.SimpleEntry<>(from, to));
    }

    @Override
    public void removeEdge(T from, T to) {
        Integer fromIndex = vertexToIndex.get(from);
        Integer toIndex = vertexToIndex.get(to);
        if (fromIndex != null && toIndex != null) {
            for (int e = 0; e < edges.size(); e++) {
                if (matrix[fromIndex][e] == -1 && matrix[toIndex][e] == 1) {
                    removeEdgeColumn(e);
                    break;
                }
            }
        }
    }

    @Override
    public List<T> getNeighbors(T vertex) {
        List<T> neighbors = new ArrayList<>();
        Integer index = vertexToIndex.get(vertex);
        if (index != null) {
            for (int e = 0; e < edges.size(); e++) {
                if (matrix[index][e] == -1) {
                    for (int v = 0; v < indexToVertex.size(); v++) {
                        if (matrix[v][e] == 1) {
                            neighbors.add(indexToVertex.get(v));
                            break;
                        }
                    }
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
        return "Incidence Matrix";
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
        for (T neighbor : getNeighbors(indexToVertex.get(index))) {
            Integer neighIndex = vertexToIndex.get(neighbor);
            if (neighIndex != null && !visited[neighIndex]) {
                dfsTopo(neighIndex, visited, order);
            }
        }
        order.add(indexToVertex.get(index));
    }
}