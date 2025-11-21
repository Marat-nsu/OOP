import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Graph<T> {
    public abstract void addVertex(T vertex);

    public abstract void removeVertex(T vertex);

    public abstract void addEdge(T from, T to);

    public abstract void removeEdge(T from, T to);

    public abstract List<T> getNeighbors(T vertex);

    public abstract Set<T> getVertices();

    protected abstract String getGraphTypeName();

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

    public abstract List<T> topologicalSort();

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
        StringBuilder sb = new StringBuilder("Graph (").append(getGraphTypeName()).append("):\n");
        sb.append("Vertices: ").append(getVertices()).append("\n");
        for (T v : getVertices()) {
            sb.append(v).append(" -> ").append(getNeighbors(v)).append("\n");
        }
        return sb.toString();
    }
}
