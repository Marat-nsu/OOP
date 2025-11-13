import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IncidenceMatrixGraph<T> implements Graph<T> {
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
    @SuppressWarnings("unchecked")
    public void readFromFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int n = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < n; i++) {
                String vertexStr = br.readLine().trim();
                addVertex((T) vertexStr);
            }
            int m = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < m; i++) {
                String[] parts = br.readLine().trim().split("\\s+");
                if (parts.length == 2) {
                    addEdge((T) parts[0], (T) parts[1]);
                }
            }
        }
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

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Graph)) {
            return false;
        }
        Graph<T> other = (Graph<T>) o;
        Set<T> thisVertices = getVertices();
        Set<T> otherVertices = other.getVertices();
        if (!thisVertices.equals(otherVertices)) {
            return false;
        }
        for (T v : thisVertices) {
            Set<T> thisNeighborsSet = new HashSet<>(getNeighbors(v));
            List<T> otherList = other.getNeighbors(v);
            Set<T> otherNeighborsSet = new HashSet<>((Collection<T>) otherList);
            if (!thisNeighborsSet.equals(otherNeighborsSet)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = getVertices().hashCode();
        for (T v : getVertices()) {
            result = 31 * result + new HashSet<>(getNeighbors(v)).hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph (Incidence Matrix):\n");
        sb.append("Vertices: ").append(getVertices()).append("\n");
        for (T v : getVertices()) {
            sb.append(v).append(" -> ").append(getNeighbors(v)).append("\n");
        }
        return sb.toString();
    }
}