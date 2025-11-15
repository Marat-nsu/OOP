import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class IncidenceMatrixGraphTest {
    private IncidenceMatrixGraph graph;
    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
        graph = new IncidenceMatrixGraph();
    }

    @AfterEach
    void tearDown() {
        graph = null;
    }

    @Test
    void testAddVertex() {
        graph.addVertex("A");
        Set<String> vertices = graph.getVertices();
        Assertions.assertEquals(1, vertices.size());
        Assertions.assertTrue(vertices.contains("A"));
    }

    @Test
    void testAddVertexDuplicate() {
        graph.addVertex("A");
        graph.addVertex("A");
        Set<String> vertices = graph.getVertices();
        Assertions.assertEquals(1, vertices.size());
    }

    @Test
    void testRemoveVertex() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        graph.removeVertex("A");
        Set<String> vertices = graph.getVertices();
        Assertions.assertEquals(1, vertices.size());
        Assertions.assertFalse(vertices.contains("A"));
        List<String> neighborsB = graph.getNeighbors("B");
        Assertions.assertEquals(0, neighborsB.size());
    }

    @Test
    void testRemoveNonExistentVertex() {
        graph.addVertex("A");
        graph.removeVertex("B");
        Set<String> vertices = graph.getVertices();
        Assertions.assertEquals(1, vertices.size());
        Assertions.assertTrue(vertices.contains("A"));
    }

    @Test
    void testAddEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        List<String> neighbors = graph.getNeighbors("A");
        Assertions.assertEquals(1, neighbors.size());
        Assertions.assertEquals("B", neighbors.get(0));
    }

    @Test
    void testAddEdgeCreatesVertices() {
        graph.addEdge("A", "B");
        Set<String> vertices = graph.getVertices();
        Assertions.assertTrue(vertices.contains("A"));
        Assertions.assertTrue(vertices.contains("B"));
        List<String> neighbors = graph.getNeighbors("A");
        Assertions.assertEquals(1, neighbors.size());
        Assertions.assertEquals("B", neighbors.get(0));
    }

    @Test
    void testRemoveEdge() {
        graph.addEdge("A", "B");
        graph.removeEdge("A", "B");
        List<String> neighbors = graph.getNeighbors("A");
        Assertions.assertEquals(0, neighbors.size());
    }

    @Test
    void testRemoveNonExistentEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.removeEdge("A", "B");
        List<String> neighbors = graph.getNeighbors("A");
        Assertions.assertEquals(0, neighbors.size());
    }

    @Test
    void testGetNeighbors() {
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        List<String> neighbors = graph.getNeighbors("A");
        Assertions.assertEquals(2, neighbors.size());
        Set<String> neighSet = new HashSet<>(neighbors);
        Assertions.assertTrue(neighSet.contains("B"));
        Assertions.assertTrue(neighSet.contains("C"));
    }

    @Test
    void testGetNeighborsNonExistentVertex() {
        List<String> neighbors = graph.getNeighbors("X");
        Assertions.assertEquals(0, neighbors.size());
    }

    @Test
    void testGetVerticesEmpty() {
        Set<String> vertices = graph.getVertices();
        Assertions.assertEquals(0, vertices.size());
    }

    @Test
    void testReadFromFile() throws IOException {
        File testFile = new File(tempDir, "testGraph.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("2\n");
            writer.write("A\n");
            writer.write("B\n");
            writer.write("1\n");
            writer.write("A B\n");
        }
        graph.readFromFile(testFile.getPath());
        Set<String> vertices = graph.getVertices();
        Assertions.assertEquals(2, vertices.size());
        List<String> neighborsA = graph.getNeighbors("A");
        Assertions.assertEquals(1, neighborsA.size());
        Assertions.assertEquals("B", neighborsA.get(0));
    }

    @Test
    void testReadFromFileEmpty() throws IOException {
        File testFile = new File(tempDir, "emptyGraph.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("0\n");
            writer.write("0\n");
        }
        graph.readFromFile(testFile.getPath());
        Assertions.assertEquals(0, graph.getVertices().size());
    }

    @Test
    void testTopologicalSort() {
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        List<String> order = graph.topologicalSort();
        Assertions.assertEquals(3, order.size());
        Assertions.assertEquals("A", order.get(0));
        Assertions.assertEquals("B", order.get(1));
        Assertions.assertEquals("C", order.get(2));
    }

    @Test
    void testTopologicalSortEmpty() {
        List<String> order = graph.topologicalSort();
        Assertions.assertEquals(0, order.size());
    }

    @Test
    void testTopologicalSortDisconnected() {
        graph.addEdge("A", "B");
        graph.addVertex("C");
        List<String> order = graph.topologicalSort();
        Set<String> orderSet = new HashSet<>(order);
        Assertions.assertTrue(orderSet.contains("A"));
        Assertions.assertTrue(orderSet.contains("B"));
        Assertions.assertTrue(orderSet.contains("C"));
    }

    @Test
    void testEqualsSame() {
        graph.addVertex("A");
        graph.addEdge("A", "B");
        Assertions.assertEquals(graph, graph);
    }

    @Test
    void testEqualsDifferentVertices() {
        IncidenceMatrixGraph other = new IncidenceMatrixGraph();
        graph.addVertex("A");
        other.addVertex("B");
        Assertions.assertNotEquals(graph, other);
    }

    @Test
    void testEqualsSameStructure() {
        IncidenceMatrixGraph other = new IncidenceMatrixGraph();
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        other.addEdge("A", "B");
        other.addEdge("B", "C");
        Assertions.assertEquals(graph, other);
    }

    @Test
    void testEqualsDifferentEdges() {
        IncidenceMatrixGraph other = new IncidenceMatrixGraph();
        graph.addEdge("A", "B");
        other.addEdge("A", "C");
        Assertions.assertNotEquals(graph, other);
    }

    @Test
    void testToString() {
        graph.addVertex("A");
        graph.addEdge("A", "B");
        String str = graph.toString();
        Assertions.assertTrue(str.contains("Vertices: [A"));
        Assertions.assertTrue(str.contains("A -> [B]"));
    }
}
