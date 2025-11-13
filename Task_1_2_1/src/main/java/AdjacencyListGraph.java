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

public class AdjacencyListGraph<T> implements Graph<T> {
    private final Map<T, List<T>> adjacencyList = new HashMap<>();

    @Override
    public void addVertex(T vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }

    @Override
    public void removeVertex(T vertex) {
        if (adjacencyList.containsKey(vertex)) {
            adjacencyList.remove(vertex);
            for (List<T> neighbors : adjacencyList.values()) {
                neighbors.remove(vertex);
            }
        }
    }

    @Override
    public void addEdge(T from, T to) {
        addVertex(from);
        addVertex(to);
        adjacencyList.get(from).add(to);
    }

    @Override
    public void removeEdge(T from, T to) {
        if (adjacencyList.containsKey(from)) {
            adjacencyList.get(from).remove(to);
        }
    }

    @Override
    public List<T> getNeighbors(T vertex) {
        return new ArrayList<>(adjacencyList.getOrDefault(vertex, new ArrayList<>()));
    }

    @Override
    public Set<T> getVertices() {
        return new HashSet<>(adjacencyList.keySet());
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
        Set<T> visited = new HashSet<>();
        List<T> order = new ArrayList<>();
        for (T vertex : getVertices()) {
            if (!visited.contains(vertex)) {
                dfsTopo(vertex, visited, order);
            }
        }
        Collections.reverse(order);
        return order;
    }

    private void dfsTopo(T vertex, Set<T> visited, List<T> order) {
        visited.add(vertex);
        for (T neighbor : getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsTopo(neighbor, visited, order);
            }
        }
        order.add(vertex);
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
        StringBuilder sb = new StringBuilder("Graph (Adjacency List):\n");
        sb.append("Vertices: ").append(getVertices()).append("\n");
        for (T v : getVertices()) {
            sb.append(v).append(" -> ").append(getNeighbors(v)).append("\n");
        }
        return sb.toString();
    }
}