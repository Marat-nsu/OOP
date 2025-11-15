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

public class AdjacencyListGraph implements Graph {
    private final Map<String, List<String>> adjacencyList = new HashMap<>();

    @Override
    public void addVertex(String vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }

    @Override
    public void removeVertex(String vertex) {
        if (adjacencyList.containsKey(vertex)) {
            adjacencyList.remove(vertex);
            for (List<String> neighbors : adjacencyList.values()) {
                neighbors.remove(vertex);
            }
        }
    }

    @Override
    public void addEdge(String from, String to) {
        addVertex(from);
        addVertex(to);
        adjacencyList.get(from).add(to);
    }

    @Override
    public void removeEdge(String from, String to) {
        if (adjacencyList.containsKey(from)) {
            adjacencyList.get(from).remove(to);
        }
    }

    @Override
    public List<String> getNeighbors(String vertex) {
        return new ArrayList<>(adjacencyList.getOrDefault(vertex, new ArrayList<>()));
    }

    @Override
    public Set<String> getVertices() {
        return new HashSet<>(adjacencyList.keySet());
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
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        for (String vertex : getVertices()) {
            if (!visited.contains(vertex)) {
                dfsTopo(vertex, visited, order);
            }
        }
        Collections.reverse(order);
        return order;
    }

    private void dfsTopo(String vertex, Set<String> visited, List<String> order) {
        visited.add(vertex);
        for (String neighbor : getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsTopo(neighbor, visited, order);
            }
        }
        order.add(vertex);
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
        StringBuilder sb = new StringBuilder("Graph (Adjacency List):\n");
        sb.append("Vertices: ").append(getVertices()).append("\n");
        for (String v : getVertices()) {
            sb.append(v).append(" -> ").append(getNeighbors(v)).append("\n");
        }
        return sb.toString();
    }
}