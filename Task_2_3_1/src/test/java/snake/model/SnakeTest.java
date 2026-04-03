package snake.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SnakeTest {

    @Test
    public void testSnakeCreation() {
        Cell start = new Cell(10, 10);
        Snake snake = new Snake(start);
        assertEquals(1, snake.length());
        assertEquals(start, snake.getHead());
        assertEquals(start, snake.getTail());
    }

    @Test
    public void testMoveWithoutGrowth() {
        Cell start = new Cell(10, 10);
        Snake snake = new Snake(start);
        Cell next = new Cell(10, 11);
        
        snake.move(next, 0);
        
        assertEquals(1, snake.length());
        assertEquals(next, snake.getHead());
        assertFalse(snake.contains(start));
        assertTrue(snake.contains(next));
    }

    @Test
    public void testMoveWithGrowth() {
        Cell start = new Cell(10, 10);
        Snake snake = new Snake(start);
        Cell next = new Cell(10, 11);
        
        snake.move(next, 1);
        
        assertEquals(2, snake.length());
        assertEquals(next, snake.getHead());
        assertEquals(start, snake.getTail());
        assertTrue(snake.contains(start));
        assertTrue(snake.contains(next));
    }

    @Test
    public void testContinuousGrowth() {
        Cell start = new Cell(10, 10);
        Snake snake = new Snake(start);
        
        snake.move(new Cell(10, 11), 2);
        assertEquals(2, snake.length());
        
        snake.move(new Cell(10, 12), 0);
        assertEquals(3, snake.length());
        
        snake.move(new Cell(10, 13), 0);
        assertEquals(3, snake.length());
    }

    @Test
    public void testContains() {
        Cell start = new Cell(10, 10);
        Snake snake = new Snake(start);
        snake.move(new Cell(10, 11), 1);
        
        assertTrue(snake.contains(new Cell(10, 10)));
        assertTrue(snake.contains(new Cell(10, 11)));
        assertFalse(snake.contains(new Cell(10, 12)));
    }
}
