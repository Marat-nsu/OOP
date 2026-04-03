package snake.controller;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import snake.model.GameStatus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    @BeforeAll
    public static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testControllerInitializationAndInput() throws Exception {
        GameController controller = new GameController();
        
        injectField(controller, "gameCanvas", new Canvas(100, 100));
        injectField(controller, "statusLabel", new Label());
        injectField(controller, "scoreLabel", new Label());
        injectField(controller, "levelLabel", new Label());
        
        Method initMethod = GameController.class.getDeclaredMethod("initialize");
        initMethod.setAccessible(true);
        initMethod.invoke(controller);
        
        
        Method startMethod = GameController.class.getDeclaredMethod("onStartClicked");
        startMethod.setAccessible(true);
        startMethod.invoke(controller);
        
        Method handleKey = GameController.class.getDeclaredMethod("handleKeyPressed", KeyEvent.class);
        handleKey.setAccessible(true);
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false, false, false);
        handleKey.invoke(controller, keyEvent);
        
        
        Field engineField = GameController.class.getDeclaredField("engine");
        engineField.setAccessible(true);
        snake.model.GameEngine engine = (snake.model.GameEngine) engineField.get(controller);
        
        
        Method tickMethod = GameController.class.getDeclaredMethod("onTick");
        tickMethod.setAccessible(true);
        tickMethod.invoke(controller); 
        
        
        engine.getSnake().move(new snake.model.Cell(0, 0), 4); 
        tickMethod.invoke(controller); 
        
        
        tickMethod.invoke(controller);

        
        Field overrideStatus = snake.model.GameEngine.class.getDeclaredField("status");
        overrideStatus.setAccessible(true);
        overrideStatus.set(engine, GameStatus.LOST);
        tickMethod.invoke(controller); 
        
        
        Method restartMethod = GameController.class.getDeclaredMethod("onRestartClicked");
        restartMethod.setAccessible(true);
        restartMethod.invoke(controller);
    }

    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}

