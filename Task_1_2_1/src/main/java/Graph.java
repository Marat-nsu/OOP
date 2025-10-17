import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Graph {
    void addVertex(String vertex);

    void removeVertex(String vertex);

    void addEdge(String from, String to);

    void removeEdge(String from, String to);

    List<String> getNeighbors(String vertex);

    Set<String> getVertices();

    void readFromFile(String filePath) throws IOException;

    List<String> topologicalSort();
}
