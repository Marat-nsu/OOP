import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjacencyListGraph<T> extends Graph<T> {
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
    protected String getGraphTypeName() {
        return "Adjacency List";
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
}