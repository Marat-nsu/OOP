package snake.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LevelManagerTest {

    @Test
    public void testLevelManagerProgression() {
        LevelManager manager = new LevelManager();
        assertEquals(1, manager.getCurrentLevelNumber());
        assertEquals(5, manager.getCurrentLevel().getWinLength());
        
        assertTrue(manager.nextLevel());
        assertEquals(2, manager.getCurrentLevelNumber());
        assertEquals(10, manager.getCurrentLevel().getWinLength());
        
        manager.nextLevel();
        manager.nextLevel();
        assertFalse(manager.nextLevel());
    }
}
