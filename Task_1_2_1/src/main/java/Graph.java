import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Graph<T> {
    void addVertex(T vertex);

    void removeVertex(T vertex);

    void addEdge(T from, T to);

    void removeEdge(T from, T to);

    List<T> getNeighbors(T vertex);

    Set<T> getVertices();

    void readFromFile(String filePath) throws IOException;

    List<T> topologicalSort();
}
