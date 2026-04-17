package snake.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Snake {
    private final Deque<Cell> segments;
    private int growthBuffer;

    public Snake(Cell startCell) {
        segments = new ArrayDeque<>();
        segments.addFirst(startCell);
        growthBuffer = 0;
    }

    public Cell getHead() {
        return segments.peekFirst();
    }

    public Cell getTail() {
        return segments.peekLast();
    }

    public int length() {
        return segments.size();
    }

    public List<Cell> getSegments() {
        return new ArrayList<>(segments);
    }

    public boolean contains(Cell cell) {
        return segments.contains(cell);
    }

    public void move(Cell newHead, int growth) {
        growthBuffer += Math.max(0, growth);
        segments.addFirst(newHead);

        if (growthBuffer > 0) {
            growthBuffer--;
        } else {
            segments.removeLast();
        }
    }
}
