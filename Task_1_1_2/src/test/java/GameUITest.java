import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameUITest {
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private ByteArrayInputStream inputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }

    @Test
    void testStartGameAndImmediateExit() {
        // Симулируем ввод: 1 колода, затем выход (0)
        String input = "1\n0\n0\n";
        inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);

        GameUI gameUI = new GameUI();
        gameUI.startGame();
        gameUI.close();

        String output = outputStream.toString();
        assertTrue(output.contains("Добро пожаловать в Блэкджек!"),
                "Должно быть приветственное сообщение");
        assertTrue(output.contains("Раунд 1"), "Должен отобразиться номер раунда");
        assertTrue(output.contains("Игра завершена!"), "Должно быть сообщение о завершении");
    }
}