import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Тест для списка смежности
        Graph graph = new AdjacencyListGraph();
        try {
            graph.readFromFile("graph.txt"); // Читаем из файла
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        System.out.println(graph);
        System.out.println("Топосорт: " + graph.topologicalSort());

        // Добавление/удаление
        graph.addEdge("A", "C");
        graph.removeVertex("B");
        System.out.println("После изменений: " + graph);
        System.out.println("Соседи A: " + graph.getNeighbors("A"));
    }
}
