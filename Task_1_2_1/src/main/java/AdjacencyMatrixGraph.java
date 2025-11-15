import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AdjacencyMatrixGraph implements Graph {
    private final Map<String, Integer> vertexToIndex = new HashMap<>();
    private final List<String> indexToVertex = new ArrayList<>();
    private boolean[][] matrix;

    public AdjacencyMatrixGraph() {
        matrix = new boolean[0][0];
    }

    @Override
    public void addVertex(String vertex) {
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
    public void removeVertex(String vertex) {
        Integer index = vertexToIndex.remove(vertex);
        if (index != null) {
            indexToVertex.remove(index.intValue());
            boolean[][] newMatrix = new boolean[indexToVertex.size()][indexToVertex.size()];
            Map<String, Integer> newVertexToIndex = new HashMap<>();
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
    public void addEdge(String from, String to) {
        addVertex(from);
        addVertex(to);
        int fromIndex = vertexToIndex.get(from);
        int toIndex = vertexToIndex.get(to);
        matrix[fromIndex][toIndex] = true;
    }

    @Override
    public void removeEdge(String from, String to) {
        Integer fromIndex = vertexToIndex.get(from);
        Integer toIndex = vertexToIndex.get(to);
        if (fromIndex != null && toIndex != null) {
            matrix[fromIndex.intValue()][toIndex.intValue()] = false;
        }
    }

    @Override
    public List<String> getNeighbors(String vertex) {
        List<String> neighbors = new ArrayList<>();
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
    public Set<String> getVertices() {
        return new HashSet<>(indexToVertex);
    }

    @Override
    public void readFromFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int n = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < n; i++) {
                String vertexStr = br.readLine().trim();
                addVertex(vertexStr);
            }
            int m = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < m; i++) {
                String[] parts = br.readLine().trim().split("\\s+");
                if (parts.length == 2) {
                    addEdge(parts[0], parts[1]);
                }
            }
        }
    }

    @Override
    public List<String> topologicalSort() {
        boolean[] visited = new boolean[indexToVertex.size()];
        List<String> order = new ArrayList<>();
        for (int i = 0; i < indexToVertex.size(); i++) {
            if (!visited[i]) {
                dfsTopo(i, visited, order);
            }
        }
        Collections.reverse(order);
        return order;
    }

    private void dfsTopo(int index, boolean[] visited, List<String> order) {
        visited[index] = true;
        for (int j = 0; j < matrix.length; j++) {
            if (matrix[index][j] && !visited[j]) {
                dfsTopo(j, visited, order);
            }
        }
        order.add(indexToVertex.get(index));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Graph)) {
            return false;
        }
        Graph other = (Graph) o;
        Set<String> thisVertices = getVertices();
        Set<String> otherVertices = (Set<String>) other.getVertices();
        if (!thisVertices.equals(otherVertices)) {
            return false;
        }
        for (String v : thisVertices) {
            Set<String> thisNeighborsSet = new HashSet<>(getNeighbors(v));
            List<String> otherList = other.getNeighbors(v);
            Set<String> otherNeighborsSet = new HashSet<>((Collection<String>) otherList);
            if (!thisNeighborsSet.equals(otherNeighborsSet)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = getVertices().hashCode();
        for (String v : getVertices()) {
            result = 31 * result + new HashSet<>(getNeighbors(v)).hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph (Adjacency Matrix):\n");
        sb.append("Vertices: ").append(getVertices()).append("\n");
        for (String v : getVertices()) {
            sb.append(v).append(" -> ").append(getNeighbors(v)).append("\n");
        }
        return sb.toString();
    }
}